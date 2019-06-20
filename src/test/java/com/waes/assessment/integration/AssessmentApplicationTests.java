package com.waes.assessment.integration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.waes.assessment.base.BaseApplicationTests;
import com.waes.assessment.base.BaseApplicationTests.RequestBuilder.ResultContainer;
import com.waes.assessment.common.Constant;
import com.waes.assessment.common.test.common.TestEndpoint;
import com.waes.assessment.common.test.common.Util;

/**
 * This class contains all the functional/integration tests of the application
 * using the Spring Boot Test tool
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public class AssessmentApplicationTests extends BaseApplicationTests {
	
	@Test
	public void givenAPostCallForBothLeftAndRightEndpoints_whenTheyAreCalled_thenBothShouldReturnAMessageThatThisIsNotAllowed() throws Exception {

		// GIVEN a post call for both left and right endpoints
		// WHEN they are called
		final String expectedResult = this.getPayloadBuilder().addElement("message", "a payload is required to be present on the request to have it set on memory")
														  	  .create();
		
		final ResultContainer leftResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_LEFT, 1)
													  			   .withHttpMethod(HttpMethod.POST)
													  			   .execute();
		
		final ResultContainer rightResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_RIGHT, 1)
																	.withHttpMethod(HttpMethod.POST)
																	.execute();

		// THEN both should return a message that this is not allowed
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, leftResult.getHttpStatus());
		Assert.assertEquals(expectedResult, leftResult.getResult());

		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, rightResult.getHttpStatus());
		Assert.assertEquals(expectedResult, rightResult.getResult());
	}
	
	@Test
	public void givenAPairOfEqualPayloads_whenBothLeftAndRightPayloadsArePosted_thenWhenTheGetIsCalledAnEqualPayloadsMessageShouldBeReturned() throws Exception {
		
		// GIVEN a pair of equal payloads
		final String leftPayload = this.getPayloadBuilder().addElement("field1", "value1")
														   .addElement("field2", "value2")
														   .create();

		final String rightPayload = this.getPayloadBuilder().addElement("field1", "value1")
															.addElement("field2", "value2")
															.create();
		
		// WHEN both left and right payloads are posted
		final ResultContainer leftResult = this.getRequestBuilder().withBody(leftPayload)
																   .withEndpoint(TestEndpoint.V1_LEFT, 2)
																   .withHttpMethod(HttpMethod.POST)
																   .execute();
		
		final ResultContainer rightResult = this.getRequestBuilder().withBody(rightPayload)
																    .withEndpoint(TestEndpoint.V1_RIGHT, 2)
																    .withHttpMethod(HttpMethod.POST)
																    .execute();
		
		final String expectedLeftResult = this.getPayloadBuilder().addElement("message", "the left value has been set on id 2")
																	.create();
		
		final String expectedRightResult = this.getPayloadBuilder().addElement("message", "the right value has been set on id 2")
																	 .create();
		
		Assert.assertEquals(HttpStatus.OK, leftResult.getHttpStatus());
		Assert.assertEquals(expectedLeftResult, leftResult.getResult());
		Assert.assertEquals(HttpStatus.OK, rightResult.getHttpStatus());
		Assert.assertEquals(expectedRightResult, rightResult.getResult());

		// THEN when the get is called an Eequal payloads message should be returned
		final ResultContainer finalResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_DIFF, 2)
																	.withHttpMethod(HttpMethod.GET)
																	.execute();

		final String expectedFinalResult = this.getPayloadBuilder().addElement("message", "both payloads are equal")
				 												   .create();
		
		Assert.assertEquals(HttpStatus.OK, finalResult.getHttpStatus());
		Assert.assertEquals(expectedFinalResult, finalResult.getResult());
	}
	
	@Test
	public void givenAPostInLeftWithoutPostOnRight_whenGetIsCalled_thenItShouldReturnThatTheRightSettingIsMissing() throws Exception {
		
		// GIVEN a post in left without post on right
		final String leftPost = this.getPayloadBuilder().addElement("fieldNam1", "value1")
														.create();
		
		final ResultContainer leftResult = this.getRequestBuilder().withBody(leftPost)
																   .withHttpMethod(HttpMethod.POST)
																   .withEndpoint(TestEndpoint.V1_LEFT, 3)
																   .execute();
		
		// WHEN get is called
		final ResultContainer finalResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_DIFF, 3)
																	.withHttpMethod(HttpMethod.GET)
																	.execute();

		// THEN it should return that the right setting is missing
		final String expectedLeftResult = this.getPayloadBuilder().addElement("message", "the left value has been set on id 3")
																	.create();

		Assert.assertEquals(HttpStatus.OK, leftResult.getHttpStatus());
		Assert.assertEquals(expectedLeftResult, leftResult.getResult());

		final String expectedFinalResult = this.getPayloadBuilder().addElement("message", "the right memory does not contain a payload set for the id 3")
																   .create();

		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, finalResult.getHttpStatus());
		Assert.assertEquals(expectedFinalResult, finalResult.getResult());
	}

	@Test
	public void givenAPostInRightWithoutPostOnLeft_whenGetIsCalled_thenItShouldReturnThatTheLeftSettingIsMissing() throws Exception {
		
		// GIVEN a post in right without post on left
		final String rightPost = this.getPayloadBuilder().addElement("fieldName1", "value1")
														 .create();
		
		final ResultContainer rightResult = this.getRequestBuilder().withBody(rightPost)
																    .withHttpMethod(HttpMethod.POST)
																    .withEndpoint(TestEndpoint.V1_RIGHT, 4)
																    .execute();
		
		// WHEN get is called
		final ResultContainer finalResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_DIFF, 4)
																	.withHttpMethod(HttpMethod.GET)
																	.execute();

		// THEN it should return that the left setting is missing
		final String expectedRightResult = this.getPayloadBuilder().addElement("message", "the right value has been set on id 4")
																   .create();

		Assert.assertEquals(HttpStatus.OK, rightResult.getHttpStatus());
		Assert.assertEquals(expectedRightResult, rightResult.getResult());

		final String expectedFinalResult = this.getPayloadBuilder().addElement("message", "the left memory does not contain a payload set for the id 4")
																   .create();

		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, finalResult.getHttpStatus());
		Assert.assertEquals(expectedFinalResult, finalResult.getResult());
	}
	
	@Test
	public void givenTwoDifferentPayloadsWithDifferentSizes_whenTheyArePostedOnceTheGetIsRequested_thenItShouldReturnThatBothPayloadsHasDifferentSizes() throws Exception {
		// GIVEN two different payloads with different sizes
		final String leftPost = this.getPayloadBuilder().addElement("fieldName1", "value1")
														.create();
		
		final String rightPost = this.getPayloadBuilder().addElement("fieldName1", "fakeValue1")
														 .create();
		
		final ResultContainer leftResult = this.getRequestBuilder().withBody(leftPost)
																   .withHttpMethod(HttpMethod.POST)
																   .withEndpoint(TestEndpoint.V1_LEFT, 5)
																   .execute();
		
		final ResultContainer rightResult = this.getRequestBuilder().withBody(rightPost)
																	.withHttpMethod(HttpMethod.POST)
																	.withEndpoint(TestEndpoint.V1_RIGHT, 5)
																	.execute();
		
		final String expectedLeftResult = this.getPayloadBuilder().addElement("message", "the left value has been set on id 5")
																	.create();

		final String expectedRightResult = this.getPayloadBuilder().addElement("message", "the right value has been set on id 5")
						 										   .create();
		
		Assert.assertEquals(HttpStatus.OK, leftResult.getHttpStatus());
		Assert.assertEquals(expectedLeftResult, leftResult.getResult());
		Assert.assertEquals(HttpStatus.OK, rightResult.getHttpStatus());
		Assert.assertEquals(expectedRightResult, rightResult.getResult());
		
		// WHEN they are posted once the get is requested
		final ResultContainer finalResult = this.getRequestBuilder().withHttpMethod(HttpMethod.GET)
															 		.withEndpoint(TestEndpoint.V1_DIFF, 5)
															 		.execute();

		// THEN it should return that both payloads has different sizes
		final String expectedFinalResult = this.getPayloadBuilder().addElement("message", "left and right payloads has different size: left 32 bytes and right 36 bytes")
																   .create();

		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, finalResult.getHttpStatus());
		Assert.assertEquals(expectedFinalResult, finalResult.getResult());
	}
	
	@Test
	public void givenLeftAndRightPayloadsWithoutBeingBase64Encoded_whenTheyArePosted_thenItShouldReturnThatTheLeftPayloadIsInvalidForNotBeingBase64Encoded() throws Exception {
		// GIVEN left and right payloads without being Base64 encoded
		final String payload = this.getPayloadBuilder().addElement("fieldName", "value")
													   .create();
		
		final ResultContainer leftResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_LEFT, 6)
																   .withBody(payload)
																   .isToEncode(false)
																   .withHttpMethod(HttpMethod.POST)
																   .execute();
		final ResultContainer rightResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_RIGHT, 6)
																    .withBody(payload)
																    .isToEncode(false)
																    .withHttpMethod(HttpMethod.POST)
																    .execute();
		
		final String expectedLeftResult = this.getPayloadBuilder().addElement("message", "the left value has been set on id 6")
																  .create();

		final String expectedRightResult = this.getPayloadBuilder().addElement("message", "the right value has been set on id 6")
					 											   .create();
		
		Assert.assertEquals(HttpStatus.OK, leftResult.getHttpStatus());
		Assert.assertEquals(expectedLeftResult, leftResult.getResult());
		Assert.assertEquals(HttpStatus.OK, rightResult.getHttpStatus());
		Assert.assertEquals(expectedRightResult, rightResult.getResult());
		
		// WHEN they are posted
		final ResultContainer finalResult = this.getRequestBuilder().withHttpMethod(HttpMethod.GET)
																	.withEndpoint(TestEndpoint.V1_DIFF, 6)
																	.execute();

		// THEN it should return that the left payload is invalid for not being Base64 encoded
		final JsonNode expectedFinalResult = this.getPayloadBuilder().addElement("message", "the left payload is invalid and cannot be deserialized")
																     .addElement("detail", "java.lang.IllegalArgumentException: Illegal base64 character 7b")
																     .createAsJsonNode();
		
		Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, finalResult.getHttpStatus());
		Assert.assertEquals(expectedFinalResult, finalResult.getResultAsJsonNode());
	}
	
	@Test
	public void givenAnEncodedLeftAndNonEncodedRightPayloadsWithoutBeingBase64Encoded_whenTheyArePosted_thenItShouldReturnThatTheRightPayloadIsInvalidForNotBeingBase64Encoded() throws Exception {
		// GIVEN an encoded left and non encoded right payloads without being Base64 encoded
		final String leftPost = this.getPayloadBuilder().addElement("fieldName", "value")
														.create();
		
		final String rightPost = this.getPayloadBuilder().addElement("fieldName", "value1234567")
			  											 .create();
		
		final ResultContainer leftResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_LEFT, 7)
																   .withBody(leftPost)
																   .isToEncode(true)
																   .withHttpMethod(HttpMethod.POST)
																   .execute();
		final ResultContainer rightResult = this.getRequestBuilder().withEndpoint(TestEndpoint.V1_RIGHT, 7)
																    .withBody(rightPost)
																    .isToEncode(false)
																    .withHttpMethod(HttpMethod.POST)
																    .execute();
		
		final String expectedLeftResult = this.getPayloadBuilder().addElement("message", "the left value has been set on id 7")
																  .create();

		final String expectedRightResult = this.getPayloadBuilder().addElement("message", "the right value has been set on id 7")
					 											   .create();
		
		Assert.assertEquals(HttpStatus.OK, leftResult.getHttpStatus());
		Assert.assertEquals(expectedLeftResult, leftResult.getResult());
		Assert.assertEquals(HttpStatus.OK, rightResult.getHttpStatus());
		Assert.assertEquals(expectedRightResult, rightResult.getResult());
		
		// WHEN they are posted
		final ResultContainer finalResult = this.getRequestBuilder().withHttpMethod(HttpMethod.GET)
																	.withEndpoint(TestEndpoint.V1_DIFF, 7)
																	.execute();

		// THEN it should return that the right payload is invalid for not being Base64 encoded
		final JsonNode expectedFinalResult = this.getPayloadBuilder().addElement("message", "the right payload is invalid and cannot be deserialized")
																     .addElement("detail", "java.lang.IllegalArgumentException: Illegal base64 character 7b")
																     .createAsJsonNode();
		
		Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, finalResult.getHttpStatus());
		Assert.assertEquals(expectedFinalResult, finalResult.getResultAsJsonNode());
	}
	
	@Test
	public void givenTwoPayloadsWithSameSizeButDifferentValues_whenBothArePosted_thenWhenGetIsMadeADiffShouldBeReturned() throws Exception {
		// GIVEN two payloads with same size but different values
		final JsonNode leftPost = this.getPayloadBuilder().addElement("name", "Daniel")
														  .addElement("characteristics", this.getPayloadBuilder().addElement("isTaller", true)
														  													     .addElement("isCool", 21)
														  													     .createAsJsonNode())
												   		  .addElement("tastes", this.getPayloadBuilder().addElement("item1", "value1")
												   		  											    .addElement("item2", "value2")
												   		  											    .createAsJsonNode())
												   		  .addElement("fakeValue", "string12345678909")
												   		  .createAsJsonNode();
		
		
		final JsonNode rightPost = this.getPayloadBuilder().addElement("c", "d")
														   .addElement("name", "Eaniel")
														   .addElement("characteristics", this.getPayloadBuilder().addElement("isTaller", false)
																   												  .addElement("isCool", 0)
																   												  .createAsJsonNode())
														   .addElement("tastes", this.getPayloadBuilder().addElement("item1", "value3")
																   									     .addElement("item2", "value4")
																   									     .createAsJsonNode())
														   .addElement("fakeValue", this.getPayloadBuilder().addElement("a", "bcd")
																   										    .createAsJsonNode())
														   .createAsJsonNode();
		
		// WHEN both are posted
		final ResultContainer leftResponse = this.getRequestBuilder().withHttpMethod(HttpMethod.POST)
																     .withBody(Util.parse(leftPost))
																     .withEndpoint(TestEndpoint.V1_LEFT, 8)
																     .execute();
		
		final ResultContainer rightResponse = this.getRequestBuilder().withHttpMethod(HttpMethod.POST)
																      .withBody(Util.parse(rightPost))
																      .withEndpoint(TestEndpoint.V1_RIGHT, 8)
																      .execute();

		// THEN when get is made a diff should be returned
		final ResultContainer finalResult = this.getRequestBuilder().withHttpMethod(HttpMethod.GET)
																    .withEndpoint(TestEndpoint.V1_DIFF, 8)
																    .execute();

		final String expectedLeftResult = this.getPayloadBuilder().addElement("message", "the left value has been set on id 8")
																  .create();

		final String expectedRightResult = this.getPayloadBuilder().addElement("message", "the right value has been set on id 8")
																   .create();

		final JsonNode expectedFinalResult = this.getPayloadBuilder().addElement("message", "both payloads contains different content/values")
																     .addElement("detail", this.getPayloadBuilder().addElement("characteristics.isCool", this.getPayloadBuilder().addElement("left", "21")
																	  		 																								     .addElement("right", "0")
																			 																								     .createAsJsonNode())
																			 									   .addElement("c", this.getPayloadBuilder().addElement("left", Constant.UNAVAILABLE_FIELD)
																			 											   								    .addElement("right", "d")
																			 											   								    .createAsJsonNode())
																			 									   .addElement("characteristics.isTaller", this.getPayloadBuilder().addElement("left", "true")
																			 											   														   .addElement("right", "false")
																		 											   															   .createAsJsonNode())
																			 									   .addElement("name", this.getPayloadBuilder().addElement("left", "Daniel")
																		 											   										   .addElement("right", "Eaniel")
																		 											   										   .createAsJsonNode())
																			 									   .addElement("fakeValue", this.getPayloadBuilder().addElement("left", "string12345678909")
																			 											   									        .addElement("right", this.getPayloadBuilder().addElement("a", "bcd")
																			 											   																					     .createAsJsonNode())
																			 											   									        .createAsJsonNode())
																			 									   .addElement("tastes.item1", this.getPayloadBuilder().addElement("left", "value1")
																			 											   											   .addElement("right", "value3")
																			 											   											   .createAsJsonNode())
																			 									   .addElement("tastes.item2", this.getPayloadBuilder().addElement("left", "value2")
																														 											   .addElement("right", "value4")
																														 											   .createAsJsonNode())
																		 										   .createAsJsonNode())
				 												     .createAsJsonNode();
		
		Assert.assertEquals(HttpStatus.OK, leftResponse.getHttpStatus());
		Assert.assertEquals(expectedLeftResult, leftResponse.getResult());
		
		Assert.assertEquals(HttpStatus.OK, rightResponse.getHttpStatus());
		Assert.assertEquals(expectedRightResult, rightResponse.getResult());

		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, finalResult.getHttpStatus());
		Assert.assertEquals(expectedFinalResult, finalResult.getResultAsJsonNode());
	}

}
