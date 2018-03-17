package com.nathan.customer.response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.nathan.customer.dto.PingResponse;


public class ResponseTestCase {

	private PingResponse response;

	@Before
	public void init() {
		response = new PingResponse("Hello");
	}

	@Test
	public void testResponseMetaSize() {
		response.getMeta().put("TEST", "TEST_VALUE");
		assertThat(response.getMeta().size(), greaterThan(0));

		response.getMeta().put("TEST1", "TEST1_VALUE");
		assertThat(response.getMeta().get("TEST1"), equalTo("TEST1_VALUE"));
	}

}
