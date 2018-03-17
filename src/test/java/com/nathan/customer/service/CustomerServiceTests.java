package com.nathan.customer.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathan.customer.dto.CustomerRequest;
import com.nathan.customer.dto.CustomerResponse;
import com.nathan.customer.entity.Customer;
import com.nathan.customer.exception.CustomerNotFoundException;
import com.nathan.customer.repository.CustomerRepository;
import com.nathan.customer.test.conf.CustomerApplicationTestConf;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = { CustomerServiceImpl.class , CustomerApplicationTestConf.class} )
public class CustomerServiceTests {


	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceTests.class);
	
	@MockBean
	private CustomerRepository repository;
	
	@Autowired
	private CustomerService service;
	
	 
	@Test
	public void shouldReturnCustomerById() {
		given(this.repository.findById(anyLong())).willReturn(mock(Customer.class));
		CustomerResponse response = service.getCustomer(10L);
		assertThat(response, notNullValue());
	}
	
	@Test
	public void verifyCustomerResponse() {
		
		given(this.repository.findById(anyLong())).willReturn(mockCustomerObject());
		CustomerResponse response = service.getCustomer(10L);
		assertThat(response.getFirstName(),  equalTo("TEST_FIRST"));
		assertThat(response.getLastName(),  equalTo("TEST_LAST"));
		
	}
	
	@Test
	public void shouldReturnListOfCustomers() {

		List<Customer> collection = Arrays.asList(mockCustomerObject());
		given(this.repository.findAll()).willReturn(collection);
		List<CustomerResponse> response = service.getCustomers();
		assertThat(response.get(0).getFirstName(), equalTo("TEST_FIRST"));
		assertThat(response.get(0).getLastName(), equalTo("TEST_LAST"));

	}
	
	@Test
	public void shouldPersistGivenCustomer() {
		given(this.repository.save(any(Customer.class))).willReturn(mockCustomerObject());
		CustomerResponse response = service.saveCustomer(mockCustomerRequest());
		LOGGER.info("Response id -- {} --- " , response.getId().getHref());
		assertThat(response.getFirstName(), equalTo("TEST_FIRST_REQ"));
	}
	
	@Test
	public void shouldUpdateCustomer() {
		given(this.repository.findById(anyLong())).willReturn(mockCustomerObject());
		service.updateCustomer(mockCustomerRequest());
	}
	
	@Test
	public void shouldDeleteCustomerById() {
		given(this.repository.findById(anyLong())).willReturn(mockCustomerObject());
		service.deleteCustomer(anyLong());
	}
	
	@Test(expected = CustomerNotFoundException.class) 
	public void shouldThrowExceptionWhenCustomerNotFound() { 
		given(this.repository.findById(anyLong())).willReturn(null);
		service.getCustomer(10L);
	}

	@Test(expected = CustomerNotFoundException.class) 
	public void shouldThrowExceptionCustomerNotFoundOnUpdate() { 
		given(this.repository.findById(anyLong())).willReturn(null);
		service.updateCustomer(mockCustomerRequest());
	}
	
	@Test(expected = CustomerNotFoundException.class) 
	public void shouldThrowExceptionCustomerNotFoundOnDelete() { 
		given(this.repository.findById(anyLong())).willReturn(null);
		service.deleteCustomer(anyLong());
	}
	
	private Customer mockCustomerObject() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setFirstName("TEST_FIRST");
		customer.setLastName("TEST_LAST");
		return customer;
	}
	
	
		
	private CustomerRequest mockCustomerRequest() {
		CustomerRequest customer = new CustomerRequest();
		//code coverage -- :D
		customer.setCustomerId(1L);
		customer.setFirstName("TEST_FIRST_REQ");
		customer.setLastName("TEST_LAST_REQ");
		customer.setAge(100);
		customer.setEmailId("test@test.com");
		customer.setServiceType("SP");
		return customer;
	}
}

