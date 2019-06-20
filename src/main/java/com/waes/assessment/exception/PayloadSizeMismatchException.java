package com.waes.assessment.exception;

/**
 * This exception is thrown when both payloads has different size each
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public class PayloadSizeMismatchException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int leftSize;
	private int rightSize;

	public PayloadSizeMismatchException(final int leftSize, final int rightSize) {
		super();
		this.leftSize = leftSize;
		this.rightSize = rightSize;
	}
	
	/**
	 * This method returns the size of the payload set on the left memory
	 * 
	 * @return {@code int} value containing the amount of bytes present in the payload
	 */
	public int getLeftSize() {
		return this.leftSize;
	}

	/**
	 * This method returns the size of the payload set on the right memory
	 * 
	 * @return {@code int} value containing the amount of bytes present in the payload
	 */
	public int getRightSize() {
		return this.rightSize;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("PayloadSizeMismatchException:(")
				.append("leftSize: ").append(this.leftSize).append(", ")
				.append("rightSize: ").append(this.rightSize)
				.append(")")
				.toString();
	}
}
