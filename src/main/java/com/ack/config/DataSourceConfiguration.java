package com.ack.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.pagehelper.PageHelper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * dataSource配置、分页配置
 * @author chenzhao @date Oct 16, 2019
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.jifen.recharge.mapper.*", sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfiguration {

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(name = "writeDataSource")//mall读写库
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "readOnlyDataSource")//mall只读库
    @ConfigurationProperties(prefix = "spring.datasource.druid.readonly")
    public DataSource readOnlyDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "dataSource")
    @Primary
    public AbstractRoutingDataSource dataSource() {
        ReadWriteRoutingDataSource proxy = new ReadWriteRoutingDataSource();
        Map<Object, Object> targetDataSource = new HashMap<Object, Object>();
        targetDataSource.put(DbContextHolder.DbType.WRITE, writeDataSource());
        targetDataSource.put(DbContextHolder.DbType.READONLY, readOnlyDataSource());
        proxy.setDefaultTargetDataSource(writeDataSource());
        proxy.setTargetDataSources(targetDataSource);
        return proxy;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
	 * 物理分页助手
	 * @return
	 */
	@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("dialect", "mysql"); 
		pageHelper.setProperties(properties);
		return pageHelper;
	}


}
