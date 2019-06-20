package com.waes.assessment.validator.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.assessment.common.Constant;
import com.waes.assessment.common.Endpoint;
import com.waes.assessment.exception.MissingInMemoryPayloadException;
import com.waes.assessment.exception.PayloadContentMismatchException;
import com.waes.assessment.exception.PayloadSizeMismatchException;
import com.waes.assessment.exception.RequiredPayloadException;
import com.waes.assessment.service.MessageService;
import com.waes.assessment.service.impl.MessageServiceImpl;
import com.waes.assessment.validator.Validator;

public class ValidatorImplTest {
	
	final Validator validator = new ValidatorImpl();

	@Before
	public void setup() {
		final ObjectMapper objectMapper = new ObjectMapper();
		final MessageService messageService = new MessageServiceImpl();
		ReflectionTestUtils.setField(messageService, "objectMapper", objectMapper, ObjectMapper.class);
		ReflectionTestUtils.setField(this.validator, "messageService", messageService, MessageService.class);
	}
	
	@Test(expected = RequiredPayloadException.class)
	public void givenFakeId1ValueWithNullInputStream_whenValidateIsCalled_thenItShouldThrowARequiredPayloadException() throws IOException {
		// GIVEN fakeId1 value with null inputStream
		final String fakeId = "fakeId1";
		
		// WHEN validate is called
		// THEN it should throw a RequiredPayloadException
		this.validator.validate(Endpoint.LEFT, fakeId, null);
	}
	
	@Test(expected = RequiredPayloadException.class)
	public void givenFakeId2ValueWithMockedEmptyInputStream_whenValidateIsCalled_thenItShouldThrowARequiredPayloadException() throws IOException {
		// GIVEN fakeId2 value with mocked empty inputStream 
		final String fakeId = "fakeId2";
		final InputStream mockInputStream = Mockito.mock(ByteArrayInputStream.class);

		// WHEN validate is called
		// THEN it should throw a RequiredPayloadException
		Mockito.when(mockInputStream.available()).thenReturn(0);
		this.validator.validate(Endpoint.LEFT, fakeId, null);
	}	

	@Test
	public void givenFakeId3ValueWithTwoNullInputStream_whenValidateIsCalled_thenItShouldThrowAMissingInMemoryPayloadException() throws IOException {
		// GIVEN fakeId3 value with two null inputStream
		final String fakeId = "fakeId3";
		try {
			// WHEN validate is called
			this.validator.validate(fakeId, null, null);
		} catch (final MissingInMemoryPayloadException e) {
			// THEN it should throw a MissingInMemoryPayloadException
			Assert.assertEquals(fakeId, e.getId());
			Assert.assertEquals(Endpoint.LEFT.getValue(), e.getMemoryName());
		}
	}
	
	@Test
	public void givenFakeId4ValueWithAMockedAndANullInputStream_whenValidateIsCalled_thenItShouldThrowAMissingInMemoryPayloadException() throws IOException {
		// GIVEN fakeId4 value with a mocked and a null inputStream
		final String fakeId = "fakeId4";
		try {
			// WHEN validate is called
			this.validator.validate(fakeId, "fakeValue", null);
		} catch (final MissingInMemoryPayloadException e) {
			// THEN it should throw a MissingInMemoryPayloadException
			Assert.assertEquals(fakeId, e.getId());
			Assert.assertEquals(Endpoint.RIGHT.getValue(), e.getMemoryName());
		}
	}
	
	@Test
	public void givenFakeId5ValueWithTwoMockedInputStreamsWithDifferentSizes_whenValidateIsCalled_thenItShouldThrowAPayloadSizeMismatchException() throws IOException {
		// GIVEN fakeId5 value with two mocked inputStreams with different sizes
		final String fakeId = "fakeId6";
		final int fakeLeftSize = 0;
		final int fakeRightSize = 5;
		final String mockLeftNode = StringUtils.EMPTY;
		final String mockRightNode = "12345";
		
		try {
			// WHEN validate is called
			this.validator.validate(fakeId, mockLeftNode, mockRightNode);
		} catch (final PayloadSizeMismatchException e) {
			// THEN it should throw a PayloadSizeMismatchException
			Assert.assertEquals(fakeLeftSize, e.getLeftSize());
			Assert.assertEquals(fakeRightSize, e.getRightSize());
		}
	}
	
	@Test
	public void givenFakeValuesDiffPayloadsSameSize_whenCalledValidateTwiceUsesCache_thenCallingAgainWithSamePayloadsShouldNotThrowException() throws JsonProcessingException, IOException {
		// GIVEN fake values with different payloads with the same size
		final String fakeId = "fakeId";
		String fakeLeftValue = Base64.getEncoder().encodeToString("{\"a\":\"b\"}".getBytes(Charset.forName("UTF-8")));
		String fakeRightValue = Base64.getEncoder().encodeToString("{\"c\":\"d\"}".getBytes(Charset.forName("UTF-8")));
		
		final Map<String, Map<String, Object>> expectedResult = new HashMap<>();
		final Map<String, Object> expectedSubResult1 = new HashMap<>();
		expectedSubResult1.put("left", "b");
		expectedSubResult1.put("right", Constant.UNAVAILABLE_FIELD);
		
		final Map<String, Object> expectedSubResult2 = new HashMap<>();
		expectedSubResult2.put("left", Constant.UNAVAILABLE_FIELD);
		expectedSubResult2.put("right", "d");

		expectedResult.put("a", expectedSubResult1);
		expectedResult.put("c", expectedSubResult2);
		
		// WHEN it is called first time it should throw an exception
		try {
			this.validator.validate(fakeId, fakeLeftValue, fakeRightValue);
		} catch (final PayloadContentMismatchException e) {
			Assert.assertEquals(expectedResult, e.getDifference());
		}
		
		// WHEN it is called second time it should throw another exception catch from the cache
		try {
			this.validator.validate(fakeId, fakeLeftValue, fakeRightValue);
		} catch (final PayloadContentMismatchException e) {
			Assert.assertEquals(expectedResult, e.getDifference());
		}
		
		// WHEN another payload is set into the same id
		this.validator.validate(Endpoint.LEFT, fakeId, fakeRightValue);

		// THEN calling the validate with another payload equals than the prior one it should not throw an exception
		this.validator.validate(fakeId, fakeRightValue, fakeRightValue);
	}
	
	@Test
	public void givenAFakeId_whenValidateIsCalledTwice_thenFirstTimeShouldProcessAndTheOtherShouldCatchFromTheCache() throws JsonProcessingException, IOException {
		// GIVEN a fakeId
		final String fakeId = "fakeId";

		// WHEN validate is called twice
		// THEN first time should process and the other should catch from the cache
		final String fakeValue = Base64.getEncoder().encodeToString("{\"a\":\"b\"}".getBytes(Charset.forName("UTF-8")));
		
		this.validator.validate(fakeId, fakeValue, fakeValue);
		this.validator.validate(fakeId, fakeValue, fakeValue);
	}
}
