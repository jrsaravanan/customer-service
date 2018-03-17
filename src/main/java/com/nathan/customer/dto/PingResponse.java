package com.nathan.customer.dto;

/**
 * ping response
 */
public class PingResponse extends DefaultRespose {

	private String message;

	public PingResponse(String message) {
		super();
		setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
