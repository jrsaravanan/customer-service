package com.nathan.customer.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.nathan.customer.dto.CustomerResponse;
import com.nathan.customer.entity.Customer;

@Configuration
@EnableTransactionManagement
public class ApplicationConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Customer, CustomerResponse> typeMap = modelMapper.createTypeMap(Customer.class, CustomerResponse.class);
		typeMap.addMapping(Customer::getId, CustomerResponse::setCustomerId);
		typeMap.addMapping(Customer::getEmailId, CustomerResponse::setEmailId);
		
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		
		return modelMapper;
	}
}
