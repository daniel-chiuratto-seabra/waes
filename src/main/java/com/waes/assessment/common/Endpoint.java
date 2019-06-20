package com.waes.assessment.common;

/**
 * This enumerator is used to force the implementation to know how many
 * endpoints are available in the application for POST http method, because
 * since both left and right endpoints acts like the same, then it was better
 * for code reuse to find a way to define when the flow is related with the left
 * and when is related with right call
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public enum Endpoint {

	/**
	 * This enum is related with the left post endpoint
	 */
	LEFT("left"),
	
	/**
	 * This enum is related with the right post endpoint
	 */
	RIGHT("right");
	
	private String value;

	private Endpoint(final String value) {
		this.value = value;
	}

	/**
	 * Retrieves the Endpoint enum as a {@link String}
	 * 
	 * @return {@link Endpoint} as {@link String}
	 */
	public String getValue() {
		return this.value;
	}
}
