package com.waes.assessment.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is a Data Transfer Object used as response template
 * for the entire application
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class ResponseDTO {

	private Serializable message;
	private Serializable detail;

	private ResponseDTO(final Serializable message, final Serializable detail) {
		this.message = message;
		this.detail = detail;
	}

	/**
	 * Returns the message related to the corresponding
	 * request
	 */
	public Serializable getMessage() {
		return this.message;
	}
	
	/**
	 * Returns the detail related to the corresponding
	 * request
	 * 
	 * @return the object containing the details of the
	 * corresponding request. It is a {@link Serializable}
	 * because it can be whatever object that can be serialized
	 * by Jackson
	 */
	public Serializable getDetail() {
		return this.detail;
	}
	
	/**
	 * Returns the {@link Builder} instance which allows to
	 * create a immutable instance of the {@link ResponseDTO}
	 * class
	 * 
	 * @return {@link Builder} instance
	 */
	public static Builder builder() {
		return Builder.builder();
	}

	@Override
	public String toString() {
		return new StringBuilder("ResponseDTO:(")
				.append("message: ").append(this.message).append(", ")
				.append("detail: ").append(this.detail)
				.append(")")
				.toString();
	}
	
	/**
	 * This inner class is to attend the needs of having the
	 * Builder pattern to create a new immutable {@link ResponseDTO}
	 * instance
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	public static class Builder {
		
		private Serializable message;
		private Serializable detail;

		private Builder() {}
		
		private static Builder builder() {
			return new Builder();
		}

		/**
		 * This methods defines the message value of the corresponding
		 * {@link ResponseDTO} object to be created
		 * 
		 * @param message
		 * @return {@link Builder} instance
		 */
		public Builder withMessage(final Serializable message) {
			this.message = message;
			return this;
		}

		/**
		 * This methods defines the detail value of the corresponding
		 * {@link ResponseDTO} object to be created
		 * 
		 * @param detail
		 * @return {@link Builder} instance
		 */
		public Builder withDetail(final Serializable detail) {
			this.detail = detail;
			return this;
		}
		
		/**
		 * This methods actually instantiates a new object of the
		 * {@link ResponseDTO} class with the informed attribute
		 * values
		 * 
		 * @return {@link ResponseDTO} instance
		 */
		public ResponseDTO build() {
			return new ResponseDTO(this.message, this.detail); 
		}
	}
}
