package com.nathan.customer.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nathan.customer.dto.CustomerRequest;
import com.nathan.customer.dto.CustomerResponse;
import com.nathan.customer.entity.Customer;
import com.nathan.customer.exception.CustomerNotFoundException;
import com.nathan.customer.repository.CustomerRepository;
import com.nathan.customer.resource.CustomerServiceResource;

/**
 * Customer Service 
 *
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public CustomerResponse getCustomer(Long id) {

		Customer entity = customerRepository.findById(id);
		return Optional.ofNullable(entity)
				.map(p -> toResponse(entity))
				.orElseThrow(() -> new CustomerNotFoundException(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CustomerResponse> getCustomers() {
		
		List<Customer> collection = customerRepository.findAll();
		
		return collection.stream()
			.map(p -> toResponse(p))
			.collect(Collectors.toList());
	}
	
	
	@Override 
	public CustomerResponse saveCustomer(final CustomerRequest request ) {
		
		Customer entity = toEntity(request);
		customerRepository.save(entity);
		CustomerResponse response = toResponse(entity);
		return response;
	}
	
	@Override
	public void updateCustomer(CustomerRequest request) {
		
		Optional<Customer> customer = Optional.ofNullable(customerRepository.findById(request.getCustomerId()));
		 
		customer.ifPresent(p -> {
					p.setFirstName(request.getFirstName());
					p.setLastName(request.getLastName());
					p.setAge(request.getAge());
					p.setEmailId(request.getEmailId());
					p.setServiceType(request.getServiceType());
					customerRepository.save(p);
				});
		customer.orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));
	}

	@Override
	public void deleteCustomer(Long id) {
	
		Optional<Customer> customer = Optional.ofNullable(customerRepository.findById(id));
		 
		customer.ifPresent(p -> customerRepository.delete(p));
		customer.orElseThrow(() -> new CustomerNotFoundException(id));
	}

	private CustomerResponse toResponse(Customer entity) {
		CustomerResponse response = modelMapper.map(entity, CustomerResponse.class);
		Link selfLink = linkTo(CustomerServiceResource.class).slash(entity.getId()).withSelfRel();
        response.add(selfLink);
        return response;
	}
	
	private Customer toEntity(final CustomerRequest request) {
		return modelMapper.map(request, Customer.class);
	}

}
