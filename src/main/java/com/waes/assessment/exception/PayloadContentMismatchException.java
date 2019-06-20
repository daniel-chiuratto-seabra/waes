package com.waes.assessment.exception;

import java.util.Map;

/**
 * This exception is thrown when both payloads has the same size but contains
 * different data
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public class PayloadContentMismatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Map<String, Object>> difference;

	public PayloadContentMismatchException(final Map<String, Map<String, Object>> result) {
		this.difference = result;
	}

	/**
	 * This method returns a {@link Map} containing the diff result between
	 * the two nodes which has same size but different content
	 * 
	 * @return {@link Map} containing the diffed data between both left and right
	 * nodes
	 */
	public Map<String, Map<String, Object>> getDifference() {
		return this.difference;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("difference: ").append(this.difference)
				.toString();
	}
}
