package com.ack.config.annotation;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ack.config.DbContextHolder;
import com.ack.config.DbContextHolder.DbType;




/**
 * 代码级别DAO层读写分离
 * @author chenzhao @date Oct 16, 2019
 */
@Order(0)//设置优先级让aspect在transaction之前执行
@Aspect
@Component
public class ReadWriteAspect {

    private static Logger logger = LoggerFactory.getLogger(ReadWriteAspect.class);

    /**
     * 切换到只读库
     */
    @Around("@annotation(readOnlyDatasource)")
    public Object proceedRead(ProceedingJoinPoint proceedingJoinPoint, ReadOnlyDataSource readOnlyDatasource) throws Throwable {
        try {
            logger.info("set database connection to memberReadonly");
            DbContextHolder.setDbType(DbContextHolder.DbType.READONLY);
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DbContextHolder.clearDbType();
            logger.info("restore database connection");
        }
    }

    /**
     * 切换到读写库
     */
    @Around("@annotation(writeDataSource)")//默认读写库
    public Object proceedWrite(ProceedingJoinPoint proceedingJoinPoint, WriteDataSource writeDataSource) throws Throwable {
        try {
            logger.info("set database connection to memberWriteRead");
            DbContextHolder.setDbType(DbContextHolder.DbType.WRITE);
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DbContextHolder.clearDbType();
            logger.info("restore database connection");
        }
    }

}
