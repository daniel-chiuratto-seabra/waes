package com.waes.assessment.exception;

import com.waes.assessment.common.Endpoint;

/**
 * This exception is thrown once a post request is made without any body on
 * the request
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public class RequiredPayloadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Endpoint endpoint;
	private String id;

	public RequiredPayloadException(final Endpoint endpoint, final String id) {
		this.endpoint = endpoint;
		this.id = id;
	}
	
	/**
	 * Returns which memory flow thrown the exception
	 * 
	 * @return {@link String} containing the memory name (left or right)
	 */
	public String getMemoryName() {
		return this.endpoint.getValue();
	}

	/**
	 * Returns which id does not contain any payload in it
	 *  
	 * @return {@link String} containing the id which does not contain any payload in it
	 */
	public String getId() {
		return this.id;
	}
}
