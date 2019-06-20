package com.waes.assessment.common;

import com.waes.assessment.config.Swagger2Configuration;
import com.waes.assessment.exception.handler.WAESExceptionHandler;

/**
 * This interface contains all the application constants, to keep all the
 * constant values into the same place
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public interface Constant {

	public static final String OK_STATUS = "OK";
	public static final String UNAVAILABLE_FIELD = "unavailable field";
	
	/**
	 * This interface is used on the application loggings
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	public interface LogMessage {
		
		public static final String MEMORY_SETTING_LOG_MESSAGE = "the {} memory with id {} has been set";
		public static final String LEFT_AND_RIGHT_PROCESS_LOG_MESSAGE = "processing left and right with id {}";
		public static final String CONTROLLER_LEFT_OR_RIGHT_LOG_MESSAGE = "POST request received on /{}/{} endpoint";
		public static final String CONTROLLER_LEFT_AND_RIGHT_PROCESS_LOG_MESSAGE = "GET request received on /{} endpoint";
		public static final String DESERIALIZATION_ERROR_MESSAGE = "an error occurred during the deserialization of the data";
		public static final String STARTING_VALIDATION = "starting the validation";
		public static final String RETRIEVING_CACHED_DATA = "retrieving previous processed data";
		public static final String THROWING_CACHED_EXCEPTION = "throwing a cached exception";
		public static final String NO_ERRORS_CACHED_DATA = "there is no errors to validate (cached result)";
		public static final String FINISHING_VALIDATION_NO_ISSUES = "finishing the validation without any issues";
		
	}
	
	/**
	 * This interface is used to keep constants related with response payloads
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	public interface ResponseMessage {
		
		public static final String LEFT_AND_RIGHT_PROCESS_RESPONSE_MESSAGE = "both payloads are equal";
		public static final String LEFT_OR_RIGHT_RESPONSE_MESSAGE_WITH_ID = "the %s value has been set on id %s";
		public static final String LEFT_OR_RIGHT_RESPONSE_MESSAGE_WITHOUT_ID = "the left value has been set";
		
	}
	
	/**
	 * This interface contains data used to construct the Swagger UI interface
	 * set on the {@link Swagger2Configuration} class
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	public interface SwaggerDetail {

		public static final String TITLE = "WAES API Assessment";
		public static final String DESCRIPTION = "API Application To Attend WAES Assessment";
		public static final String VERSION = "1.0.0";
		public static final String LICENSE = "Apache License Version 2.0";
		public static final String LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0";
		
	}

	/**
	 * This interface contains constants used to construct the error response payloads, mostly used
	 * on the {@link WAESExceptionHandler} class
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	public interface ExceptionHandlerMessage {

		public static final String DESERIALIZATION_EXCEPTION_MESSAGE = "the %s payload is invalid and cannot be deserialized";
		public static final String PAYLOAD_SIZE_MISMATCH_EXCEPTION_MESSAGE = "left and right payloads has different size: left %d bytes and right %d bytes";
		public static final String PAYLOAD_CONTENT_MISMATCH_EXCEPTION_MESSAGE = "both payloads contains different content/values";
		public static final String MISSING_PAYLOAD_EXCEPTION_MESSAGE = "the %s memory does not contain a payload set for the id %s";
		public static final String REQUIRED_PAYLOAD_EXCEPTION_MESSAGE = "a payload is required to be present on the request to have it set on memory";
		public static final String EXCEPTION_MESSAGE = "a generic error happened inside the server which has not been predicted";
		
	}

}
