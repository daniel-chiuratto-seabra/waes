package com.waes.assessment.exception.handler;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.waes.assessment.common.Constant.ExceptionHandlerMessage;
import com.waes.assessment.common.JsonUtil;
import com.waes.assessment.dto.ResponseDTO;
import com.waes.assessment.exception.DeserializationException;
import com.waes.assessment.exception.MissingInMemoryPayloadException;
import com.waes.assessment.exception.PayloadContentMismatchException;
import com.waes.assessment.exception.PayloadSizeMismatchException;
import com.waes.assessment.exception.RequiredPayloadException;
import com.waes.assessment.logger.WAESLoggerFactory;

/**
 * This class is an Exception Handler which handles all the predicted
 * error scenarios that can happen during the application usage, and then
 * the error is parsed into the {@link ResponseDTO} entity to return
 * accordingly to the requestor
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
@RestControllerAdvice
public class WAESExceptionHandler {

	private static final Logger LOGGER = WAESLoggerFactory.getLogger(WAESExceptionHandler.class);
	
	/**
	 * This method is related with the {@link Exception} handling, where it works for
	 * unpredicted scenarios
	 * 
	 * @param e containing the {@link Exception} instance with the failure data to be returned to the requestor
	 * @return {@link ResponseEntity} containing the {@link ResponseDTO} with the details of what happened
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ResponseDTO> exception(final Exception e) {
		final String message = ExceptionHandlerMessage.EXCEPTION_MESSAGE;
		
		LOGGER.error(message, e);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							 .body(ResponseDTO.builder().withMessage(message)
									 					.withDetail(ExceptionUtils.getStackTrace(e))
									 					.build());
	}

	/**
	 * This method is related with the {@link DeserializationException} handling, where it handles
	 * situations where there is any payload deserialization error like receiving an invalid payload
	 * or a payload that was not encoded into Base64
	 * 
	 * @param e containing the {@link DeserializationException} instance with the failure data to be returned to the requestor
	 * @return {@link ResponseEntity} containing the {@link ResponseDTO} with the details of what happened
	 */
	@ExceptionHandler(value = DeserializationException.class)
	public ResponseEntity<ResponseDTO> deserializationException(final DeserializationException e) {
		final String message = String.format(ExceptionHandlerMessage.DESERIALIZATION_EXCEPTION_MESSAGE, e.getMemoryName());

		LOGGER.error(message, e);
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
							 .body(ResponseDTO.builder().withMessage(message)
									 				    .withDetail(e.getMessage())
									 				    .build());
	}

	/**
	 * This method is related with the {@link PayloadSizeMismatchException} handling, where it handles
	 * situations where when the GET Diff is requested for a specific ID, both payloads has different sizes
	 * 
	 * @param e containing the {@link PayloadSizeMismatchException} instance with the failure data to be returned to the requestor
	 * @return {@link ResponseEntity} containing the {@link ResponseDTO} with the details of what happened
	 */
	@ExceptionHandler(value = PayloadSizeMismatchException.class)
	public ResponseEntity<ResponseDTO> payloadSizeMismatchException(final PayloadSizeMismatchException e) {
		final String message = String.format(ExceptionHandlerMessage.PAYLOAD_SIZE_MISMATCH_EXCEPTION_MESSAGE, e.getLeftSize(), e.getRightSize());

		LOGGER.error(message);
		
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
							 .body(ResponseDTO.builder().withMessage(message)
									 				  	.build());
	}
	
	/**
	 * This method is related with the {@link PayloadContentMismatchException} handling, where it handles
	 * situations where when the GET Diff is requested for a specific ID, with both payloads having the same
	 * size but with different data/values
	 * 
	 * It receives a {@link Map} containing the diff built but the {@link JsonUtil} abstract class, and returns
	 * it to the requestor
	 * 
	 * @param e containing the {@link PayloadContentMismatchException} instance with the failure data to be returned to the requestor
	 * @return {@link ResponseEntity} containing the {@link ResponseDTO} with the details of what happened
	 */
	@ExceptionHandler(value = PayloadContentMismatchException.class)
	public ResponseEntity<ResponseDTO> payloadContentMismatchException(final PayloadContentMismatchException e) {
		final String message = ExceptionHandlerMessage.PAYLOAD_CONTENT_MISMATCH_EXCEPTION_MESSAGE;
		
		LOGGER.error(message);
		
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
							 .body(ResponseDTO.builder().withMessage(message)
									 				    .withDetail((Serializable) e.getDifference())
									 				    .build());
	}
	
	/**
	 * This method is related with the {@link MissingInMemoryPayloadException} handling, where it handles
	 * situations when the GET Diff is requested for an ID that has only one of the memories (left or right)
	 * with payload set with the other being null/empty
	 * 
	 * It returns which side does not contain data for the diff comparison as well its id
	 * 
	 * @param e containing the {@link MissingInMemoryPayloadException} instance with the failure data to be returned to the requestor
	 * @return {@link ResponseEntity} containing the {@link ResponseDTO} with the details of what happened
	 */
	@ExceptionHandler(value = MissingInMemoryPayloadException.class)
	public ResponseEntity<ResponseDTO> missingPayloadException(final MissingInMemoryPayloadException e) {
		final String message = String.format(ExceptionHandlerMessage.MISSING_PAYLOAD_EXCEPTION_MESSAGE, e.getMemoryName(), e.getId());
		
		LOGGER.error(message);
		
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
							 .body(ResponseDTO.builder().withMessage(message)
							 					  		.build());
	}
	
	/**
	 * This method is related with the {@link RequiredPayloadException} handling, where it handles
	 * situations when a POST os made on one of the endpoints (left or right) without any content
	 * in its body
	 * 
	 * @param e containing the {@link MissingInMemoryPayloadException} instance with the failure data to be returned to the requestor
	 * @return {@link ResponseEntity} containing the {@link ResponseDTO} with the details of what happened
	 */
	@ExceptionHandler(value = RequiredPayloadException.class)
	public ResponseEntity<ResponseDTO> requiredPayloadException(final RequiredPayloadException e) {
		final String message = ExceptionHandlerMessage.REQUIRED_PAYLOAD_EXCEPTION_MESSAGE;
		
		LOGGER.error(message);
		
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
							 .body(ResponseDTO.builder().withMessage(message)
								 				  		.build());
	}
}
