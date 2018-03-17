package com.nathan.customer.test.conf;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.nathan.customer.dto.CustomerRequest;
import com.nathan.customer.dto.CustomerResponse;
import com.nathan.customer.entity.Customer;

// Almost similar to CustomerRepositoryTestConf 
// not enabling repo and scanning repositories , minimal implementation 
// TODO : Refactor , create base class  for CustomerRepositoryTestConf
@Configuration
public class CustomerApplicationTestConf {
	 
	 @Bean
	  public DataSource dataSource() {

	    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
	    return builder.setType(EmbeddedDatabaseType.H2).build();
	  }

	 @Bean
	  public EntityManagerFactory entityManagerFactory() {
		    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		    vendorAdapter.setGenerateDdl(true);

		    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		    factory.setJpaVendorAdapter(vendorAdapter);
		    factory.setPackagesToScan("com.nathan.customer.entity");
		    factory.setDataSource(dataSource());
		    factory.afterPropertiesSet();

		    return factory.getObject();
	  }
	 
	  @Bean
	  public PlatformTransactionManager transactionManager() {

	    JpaTransactionManager txManager = new JpaTransactionManager();
	    txManager.setEntityManagerFactory(entityManagerFactory());
	    return txManager;
	  }
	  
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Customer, CustomerResponse> typeMap = modelMapper.createTypeMap(Customer.class, CustomerResponse.class);
		typeMap.addMapping(Customer::getId, CustomerResponse::setCustomerId);
		typeMap.addMapping(Customer::getEmailId, CustomerResponse::setEmailId);
		
		TypeMap<Customer, CustomerRequest> requestTypeMap = modelMapper.createTypeMap(Customer.class, CustomerRequest.class);
		requestTypeMap.addMapping(Customer::getId, CustomerRequest::setCustomerId);
		requestTypeMap.addMapping(Customer::getEmailId, CustomerRequest::setEmailId);
		
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		
		return modelMapper;
	}

}

 