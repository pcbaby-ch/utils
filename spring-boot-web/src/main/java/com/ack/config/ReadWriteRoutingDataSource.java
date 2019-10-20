package com.ack.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ReadWriteRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }
}
