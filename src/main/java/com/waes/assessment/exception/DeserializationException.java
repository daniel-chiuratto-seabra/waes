package com.waes.assessment.exception;

import com.waes.assessment.common.Endpoint;
import com.waes.assessment.exception.handler.WAESExceptionHandler;

/**
 * This exception is thrown when any issues regarding the deserialization
 * process is thrown and catch by the {@link WAESExceptionHandler}
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public class DeserializationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Endpoint endpoint;

	public DeserializationException(final Endpoint endpoint, final Throwable t) {
		super(t);
		this.endpoint = endpoint;
	}

	/**
	 * Returns which memory flow thrown the exception
	 * 
	 * @return {@link String} containing the memory name (left or right)
	 */
	public String getMemoryName() {
		return this.endpoint.getValue();
	}
}
