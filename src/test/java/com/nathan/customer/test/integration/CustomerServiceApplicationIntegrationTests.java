package com.nathan.customer.test.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathan.customer.dto.CustomerRequest;
import com.nathan.customer.dto.CustomerResponse;
import com.nathan.customer.resource.CustomerServiceResource;
import com.nathan.customer.service.CustomerService;
import com.nathan.customer.test.conf.CustomerRepositoryTestConf;


/**
 * Customer Service Resource endpoint testing also generates REST documentation
 *
 */
@RunWith(SpringRunner.class)  
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@ContextConfiguration (classes = CustomerRepositoryTestConf.class )
@WebMvcTest(CustomerServiceResource.class)
public class CustomerServiceApplicationIntegrationTests {
	
	public static Logger LOGGER = LoggerFactory.getLogger(CustomerServiceApplicationIntegrationTests.class);

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Test /ping
	 * @throws Exception
	 * 
	 */
	
	@Test
	public void noParamPingShouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/v1.0/customers/ping")).andDo(print()).andExpect(status().isOk());
	}

	
	@Test
	public void postCustomerShouldSaveCustomer() throws Exception {
		
		MvcResult result = createNewCustomer();
		
		 String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
		 LOGGER.info(">>>>> Location {} ", location);
		 
		 CustomerResponse customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponse.class);
		 assertThat(customerResponse.getFirstName(), equalTo("INIT_TEST_FIRST_NAME"));
	}


	private MvcResult createNewCustomer() throws Exception, JsonProcessingException {
		CustomerRequest request = new CustomerRequest();
		request.setFirstName("INIT_TEST_FIRST_NAME");
		request.setLastName("INIT_TEST_LAST_NAME");
		
		 MvcResult result = mockMvc.perform(post("/v1.0/customers")
				.accept(MediaType.APPLICATION_JSON)
			      .contentType(MediaType.APPLICATION_JSON)
			      .content(objectMapper.writeValueAsString(request)))
			      .andExpect(status().isCreated())
			      .andReturn() ;
		return result;
	}
	
	
	@Test
	public void putCustomerShouldChangeTheExistingCustomer() throws Exception {

		// add a new customer 
		MvcResult newCustomer = createNewCustomer();
		CustomerResponse newCustomerResponse = objectMapper.readValue(newCustomer.getResponse().getContentAsString(), CustomerResponse.class);
		
		//change the customer 
		CustomerRequest request = new CustomerRequest();
		request.setCustomerId(newCustomerResponse.getCustomerId());
		request.setFirstName("INIT_CHANGED_FIRST_NAME");
		request.setLastName("INIT_TEST_LAST_NAME");
		
		//modify existing one 
		 mockMvc.perform(put("/v1.0/customers")
				.accept(MediaType.APPLICATION_JSON)
			      .contentType(MediaType.APPLICATION_JSON)
			      .content(objectMapper.writeValueAsString(request)))
			      .andExpect(status().isOk())
			      .andReturn() ;
		 
		
		 //get  modified customer
		 MvcResult retriveResult = mockMvc.perform(get("/v1.0/customers/" + request.getCustomerId())
					.accept(MediaType.APPLICATION_JSON)
				      .contentType(MediaType.APPLICATION_JSON))
				      .andExpect(status().isOk())
				      .andReturn();
		 
		 CustomerResponse modified = objectMapper.readValue(retriveResult.getResponse().getContentAsString(), CustomerResponse.class);
		 assertThat(modified.getFirstName(), equalTo("INIT_CHANGED_FIRST_NAME"));
	}
	
	
	@Test
	public void deleteCustomerShouldRemoveTheCustomer() throws Exception {
		
		// add new customer
		MvcResult result = createNewCustomer();
		CustomerResponse customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponse.class);
		
		// delete the customer 
		String customerUri = "/v1.0/customers/" + customerResponse.getCustomerId();
		mockMvc.perform(delete(customerUri)
				.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
		
		 //Should Return 404 after delete
		 mockMvc.perform(get(customerUri)
					.accept(MediaType.APPLICATION_JSON)
				      .contentType(MediaType.APPLICATION_JSON))
				      .andExpect(status().isNotFound())
				      .andReturn();
	}
	
	@Test
	public void performListCustomer() throws Exception {
		
		// add new customer
		createNewCustomer();
				
		MvcResult result = mockMvc.perform(get("/v1.0/customers")
				.accept(MediaType.APPLICATION_JSON)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andReturn() ;
		LOGGER.info(">> List all customers  response \n {} " , result.getResponse().getContentAsString() );
		
		CustomerResponse[] customers = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponse[].class);
		assertThat(customers.length, greaterThan(0));
		 
	}
 
}
