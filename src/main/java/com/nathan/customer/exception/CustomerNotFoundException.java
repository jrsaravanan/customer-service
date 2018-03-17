package com.nathan.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Throw when unable to find the customer
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException  extends RuntimeException {

	private Long id;
	
	public CustomerNotFoundException(Long id) {
		super("Unable to find the customer for given id. ");
		this.id = id;
	}
}
