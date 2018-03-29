package com.nathan.customer.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
    	System.out.println("Context : " + TenantContext.getCurrentTenant() );
        return TenantContext.getCurrentTenant();
    }
}