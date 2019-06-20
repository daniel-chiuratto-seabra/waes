package com.waes.assessment.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.waes.assessment.common.Constant.ExceptionHandlerMessage;
import com.waes.assessment.common.Endpoint;
import com.waes.assessment.dto.ResponseDTO;
import com.waes.assessment.exception.DeserializationException;
import com.waes.assessment.exception.MissingInMemoryPayloadException;
import com.waes.assessment.exception.PayloadContentMismatchException;
import com.waes.assessment.exception.PayloadSizeMismatchException;
import com.waes.assessment.exception.RequiredPayloadException;

public class WAESExceptionHandlerTest {

	private final WAESExceptionHandler waesExceptionHandler = new WAESExceptionHandler(); 
	
	@Test
	public void givenFakeValues_whenExceptionIsCalled_thenItShouldGenerateAResponseWithTheValues() {
		// GIVEN fake values
		final Exception fakeException = new Exception(new Exception("fakeMessage"));

		// WHEN exception is called
		final ResponseEntity<ResponseDTO> responseEntity = this.waesExceptionHandler.exception(fakeException);

		// THEN it should generate a response with the values
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		Assert.assertTrue("The responseEntity should contain a body", responseEntity.hasBody());
		Assert.assertEquals(ExceptionHandlerMessage.EXCEPTION_MESSAGE, responseEntity.getBody().getMessage());
		Assert.assertTrue(((String) responseEntity.getBody().getDetail()).startsWith("java.lang.Exception: java.lang.Exception: fakeMessage"));
	}
	
	@Test
	public void givenFakeValues_whenDeserializationExceptionIsCalled_thenItShouldGenerateAResponseWithTheValues() {
		// GIVEN fake values
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		final Throwable fakeThrowable = new Exception("fakeMessage");
		final DeserializationException fakeDeserializationException = new DeserializationException(fakeEndpoint, fakeThrowable);

		// WHEN deserializationException is called
		final ResponseEntity<ResponseDTO> responseEntity = this.waesExceptionHandler.deserializationException(fakeDeserializationException);
		
		// THEN it should generate a response with the values
		Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		Assert.assertTrue("The responseEntity should contain a body", responseEntity.hasBody());
		Assert.assertEquals(String.format(ExceptionHandlerMessage.DESERIALIZATION_EXCEPTION_MESSAGE, fakeEndpoint.getValue()), responseEntity.getBody().getMessage());
		Assert.assertEquals("java.lang.Exception: fakeMessage", responseEntity.getBody().getDetail());
	}
	
	@Test
	public void givenFakeValues_whenPayloadSizeMismatchExceptionIsCalled_thenItShouldGenerateAResponseWithTheValues() {
		// GIVEN fake values
		final int fakeLeftSize = 2;
		final int fakeRightSize = 4;
		final PayloadSizeMismatchException fakePayloadSizeMismatchException = new PayloadSizeMismatchException(fakeLeftSize, fakeRightSize);
		
		// WHEN payloadSizeMismatchException is called
		final ResponseEntity<ResponseDTO> responseEntity = this.waesExceptionHandler.payloadSizeMismatchException(fakePayloadSizeMismatchException);

		// THEN it should generate a response with the values
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
		Assert.assertTrue("The responseEntity should contain a body", responseEntity.hasBody());
		Assert.assertEquals(String.format(ExceptionHandlerMessage.PAYLOAD_SIZE_MISMATCH_EXCEPTION_MESSAGE, fakeLeftSize, fakeRightSize), responseEntity.getBody().getMessage());
		Assert.assertNull(responseEntity.getBody().getDetail());
	}

	@Test
	public void givenFakeValues_whenPayloadContentMismatchExceptionIsCalled_thenItShouldGenerateAResponseWithTheValues() {
		// GIVEN fake values
		final Map<String, Map<String, Object>> fakeResult = new HashMap<>();
		final Map<String, Object> fakeSubResult1 = new HashMap<>();
		fakeSubResult1.put("fakeSubField1", "fakeSubValue1");
		fakeResult.put("fakeField1", fakeSubResult1);
		
		final Map<String, Object> fakeSubResult2 = new HashMap<>();
		fakeSubResult1.put("fakeSubField2", "fakeSubValue2");
		fakeResult.put("fakeField2", fakeSubResult2);
		
		final PayloadContentMismatchException fakePayloadContentMismatchException = new PayloadContentMismatchException(fakeResult);
		
		// WHEN payloadContentMismatchException is called
		final ResponseEntity<ResponseDTO> responseEntity = this.waesExceptionHandler.payloadContentMismatchException(fakePayloadContentMismatchException);
		
		// THEN it should generate a response with the values
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
		Assert.assertTrue("The responseEntity should contain a body", responseEntity.hasBody());
		Assert.assertEquals(ExceptionHandlerMessage.PAYLOAD_CONTENT_MISMATCH_EXCEPTION_MESSAGE, responseEntity.getBody().getMessage());
		Assert.assertEquals(fakeResult, responseEntity.getBody().getDetail());
	}
	
	@Test
	public void givenFakeValues_whenMissingPayloadExceptionIsCalled_thenItShouldGenerateAResponseWithTheValues() {
		// GIVEN fake values
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		final String fakeId = "fakeId";
		final MissingInMemoryPayloadException fakeMissingInMemoryPayloadException = new MissingInMemoryPayloadException(fakeEndpoint, fakeId);
		
		// WHEN missingPayloadException is called		
		final ResponseEntity<ResponseDTO> responseEntity = this.waesExceptionHandler.missingPayloadException(fakeMissingInMemoryPayloadException);

		// THEN it should generate a response with the values
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
		Assert.assertTrue("The responseEntity should contain a body", responseEntity.hasBody());
		Assert.assertEquals(String.format(ExceptionHandlerMessage.MISSING_PAYLOAD_EXCEPTION_MESSAGE, fakeEndpoint.getValue(), fakeId), responseEntity.getBody().getMessage());
		Assert.assertNull(responseEntity.getBody().getDetail());
	}
	
	@Test
	public void givenFakeValues_whenRequiredPayloadExceptionIsCalled_thenItShouldGenerateAResponseWithTheValues() {
		// GIVEN fake values
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		final String fakeId = "fakeId";
		final RequiredPayloadException fakeRequiredPayloadException = new RequiredPayloadException(fakeEndpoint, fakeId);

		// WHEN requiredPayloadException is called
		final ResponseEntity<ResponseDTO> responseEntity = this.waesExceptionHandler.requiredPayloadException(fakeRequiredPayloadException);

		// THEN it should generate a response with the values
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
		Assert.assertTrue("The responseEntity should contain a body", responseEntity.hasBody());
		Assert.assertEquals(ExceptionHandlerMessage.REQUIRED_PAYLOAD_EXCEPTION_MESSAGE, responseEntity.getBody().getMessage());
		Assert.assertNull(responseEntity.getBody().getDetail());
	}
	
}
