package com.ack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class PropertiesConfiguration {

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer  ppc = new PropertySourcesPlaceholderConfigurer ();
//        ppc.setIgnoreUnresolvablePlaceholders(true);
        ppc.setIgnoreResourceNotFound(true);
        final List<Resource> resourceLst = new ArrayList<>();
        resourceLst.add(new ClassPathResource("jdbc.properties"));
        resourceLst.add(new ClassPathResource("application.properties"));
        resourceLst.add(new ClassPathResource("config.properties"));

        ppc.setLocations(resourceLst.toArray(new Resource[]{}));

        return ppc;
    }
    
}
