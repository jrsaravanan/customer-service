package com.nathan.customer.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * DefaultRespose : is wrapper for  ResourceSupport , Resource support added to adopt HATEOS 
 * This class is base for all response type , it is possible to response only with {@link DefaultRespose}  in case meta or response code expected by the client.
 *  
 *  meta not used now , added to accommodate future needs
 */
@JsonInclude(Include.NON_EMPTY)
public class DefaultRespose extends ResourceSupport {

	
	private Map<String, String> meta = null;

	public Map<String, String> getMeta() {
		if (null == meta) {
			meta = new HashMap<>();
		}
		return meta;
	}

}
