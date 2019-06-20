package com.waes.assessment.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.assessment.common.Constant.ResponseMessage;
import com.waes.assessment.common.Endpoint;
import com.waes.assessment.exception.DeserializationException;
import com.waes.assessment.service.MessageService;


public class MessageServiceImplTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	final MessageService messageService = new MessageServiceImpl();
	
	@Before
	public void setup() {
		ReflectionTestUtils.setField(this.messageService, "objectMapper", OBJECT_MAPPER, ObjectMapper.class);
	}
	
	@Test
	public void givenFakeValuesWithBase64EncodedPayload_whenItCallsTheParseMethod_thenItShouldReturnTheExpectedPayload() throws IOException {
		// GIVEN fake values with base64 encoded payload
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		final String fakeRequest = "{\"field\":\"value\"}";
		
		// WHEN it calls the parse method
		final JsonNode response = this.messageService.parseJsonNode(fakeEndpoint, Base64.getEncoder().encodeToString(fakeRequest.getBytes(Charset.forName("UTF-8"))));
		
		// THEN it should return the expected payload
		Assert.assertEquals(new ObjectMapper().readTree(fakeRequest), response);
	}

	@Test(expected = DeserializationException.class)
	public void givenFakeValuesWithNonBase64EncodedPayload_whenItCallsTheParseMethod_thenItShouldThrowAnException() throws IOException {
		// GIVEN fake values with non-base64 encoded payload
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		final String fakeRequest = "{\"field\":\"value\"}";
		
		// WHEN it calls the parse method
		// THEN it should throw an exception
		this.messageService.parseJsonNode(fakeEndpoint, fakeRequest);
	}
	
	@Test
	public void givenFakeValues_whenTheParseMethodIsCalled_thenTheResponseMessageShouldBeParsedAccordingly() {
		// GIVEN fake values
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		final String fakeId = "fakeId";
		
		// WHEN the parse method is called
		final String response = this.messageService.parse(fakeEndpoint, fakeId);
		
		// THEN the response message should be parsed accordingly
		Assert.assertEquals(String.format(ResponseMessage.LEFT_OR_RIGHT_RESPONSE_MESSAGE_WITH_ID, fakeEndpoint.getValue(), fakeId), response);
	}

	@Test
	public void givenFakeValuesWithEmptyId_whenTheParseMethodIsCalled_thenTheResponseMessageShouldBeParsedAccordingly() {
		// GIVEN fake values with empty id
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		
		// WHEN the parse method is called
		final String response = this.messageService.parse(fakeEndpoint, StringUtils.EMPTY);
		
		// THEN the response message should be parsed accordingly
		Assert.assertEquals(String.format(ResponseMessage.LEFT_OR_RIGHT_RESPONSE_MESSAGE_WITHOUT_ID, fakeEndpoint.getValue()), response);
	}
	
	@Test
	public void givenFakeValuesWithNullId_whenTheParseMethodIsCalled_thenTheResponseMessageShouldBeParsedAccordingly() {
		// GIVEN fake values with null id
		final Endpoint fakeEndpoint = Endpoint.LEFT;
		
		// WHEN the parse method is called
		final String response = this.messageService.parse(fakeEndpoint, (String) null);
		
		// THEN the response message should be parsed accordingly
		Assert.assertEquals(String.format(ResponseMessage.LEFT_OR_RIGHT_RESPONSE_MESSAGE_WITHOUT_ID, fakeEndpoint.getValue()), response);		
	}
	
	@Test
	public void givenNullValues_whenTheParseMethodIsCalled_thenNullShouldBeReturnedInstead() {
		// GIVEN null values
		// WHEN the parse method is called
		// THEN null should be returned instead
		Assert.assertNull(this.messageService.parse(null, (String) null));
	}

}
