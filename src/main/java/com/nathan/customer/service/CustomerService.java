package com.nathan.customer.service;

import java.util.List;

import com.nathan.customer.dto.CustomerRequest;
import com.nathan.customer.dto.CustomerResponse;

/**
 *  Customer service 
 **/
public interface CustomerService {

	/**
	 * get customer by id , typical usecase
	 * @param id
	 * @return {@link CustomerResponse}
	 */
	public CustomerResponse getCustomer(Long id);
	
	/**
	 * get all customers
	 */
	public List<CustomerResponse> getCustomers();

	/**
	 * save customer 
	 * @param request
	 * @return {@link CustomerResponse}
	 */
	public CustomerResponse saveCustomer(CustomerRequest request);
	
	/**
	 * update customer
	 * @param request
	 */
	public void updateCustomer(CustomerRequest request);

	/**
	 * delete customer
	 * @param id
	 * @return
	 */
	public void deleteCustomer(Long id);
}
