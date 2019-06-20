package com.waes.assessment.exception;

import com.waes.assessment.common.Endpoint;

/**
 * This exception is thrown when the get method is executed in an id
 * which one of the memories does not have any payload set in it
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public class MissingInMemoryPayloadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Endpoint endpoint;
	private String id;

	public MissingInMemoryPayloadException(final Endpoint endpoint, final String id) {
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
	
	@Override
	public String toString() {
		return new StringBuilder("MissingInMemoryPayloadException:(")
				.append("endpoint: ").append(this.endpoint.getValue()).append(", ")
				.append("id: ").append(this.id)
				.append(")")
				.toString();
	}
}
