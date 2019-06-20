package com.waes.assessment.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.waes.assessment.common.Constant.ResponseMessage;
import com.waes.assessment.common.Endpoint;
import com.waes.assessment.common.test.common.Util;
import com.waes.assessment.dto.ResponseDTO;
import com.waes.assessment.service.MessageService;
import com.waes.assessment.validator.Validator;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentApplicationServiceImplTest {

	@Mock
	private Validator mockValidator;
	
	@Mock
	private MessageService mockMessageService;
	
	@InjectMocks
	private AssessmentApplicationServiceImpl assessmentApplicationServiceImpl;
	
	@Test
	public void givenFakeValues_whenSetLeftIsCalled_thenItShouldReturnThoseValues() throws IOException {
		// GIVEN fake values
		final String fakeId = "fakeId";
		final String fakeMessage = "fakeMessage";
		final String fakeValue = "fakeValue";
		final InputStream fakeValueInputStream = Util.buildInputStream(true, fakeValue);

		// WHEN setLeft is called
		Mockito.when(this.mockMessageService.parse(ArgumentMatchers.eq(Endpoint.LEFT), ArgumentMatchers.eq(fakeId))).thenReturn(fakeMessage);
		Mockito.when(this.mockMessageService.parseString(ArgumentMatchers.eq(Endpoint.LEFT), ArgumentMatchers.eq(fakeValueInputStream))).thenReturn(fakeValue);
		final ResponseDTO responseDTO = this.assessmentApplicationServiceImpl.setLeft(fakeId, fakeValueInputStream);
		
		// THEN it should return those values
		Assert.assertNotNull("An instance should be returned by the service", responseDTO);
		Assert.assertEquals(fakeMessage, responseDTO.getMessage());
		Assert.assertNull(responseDTO.getDetail());
		
		Mockito.verify(this.mockValidator, Mockito.times(1)).validate(ArgumentMatchers.eq(Endpoint.LEFT), ArgumentMatchers.eq(fakeId), ArgumentMatchers.eq(fakeValue));
		Mockito.verify(this.mockMessageService, Mockito.times(1)).parse(ArgumentMatchers.eq(Endpoint.LEFT), ArgumentMatchers.eq(fakeId));
	}
	
	@Test
	public void givenFakeValues_whenSetRightIsCalled_thenItShouldReturnThoseValues() throws IOException {
		// GIVEN fake values
		final String fakeId = "fakeId";
		final String fakeMessage = "fakeMessage";
		final String fakeValue = "fakeValue";
		final InputStream fakeValueInputStream = Util.buildInputStream(true, fakeValue);

		// WHEN setLeft is called
		Mockito.when(this.mockMessageService.parse(ArgumentMatchers.eq(Endpoint.RIGHT), ArgumentMatchers.eq(fakeId))).thenReturn(fakeMessage);
		Mockito.when(this.mockMessageService.parseString(ArgumentMatchers.eq(Endpoint.RIGHT), ArgumentMatchers.eq(fakeValueInputStream))).thenReturn(fakeValue);
		final ResponseDTO responseDTO = this.assessmentApplicationServiceImpl.setRight(fakeId, fakeValueInputStream);
		
		// THEN it should return those values
		Assert.assertNotNull("An instance should be returned by the service", responseDTO);
		Assert.assertEquals(fakeMessage, responseDTO.getMessage());
		Assert.assertNull(responseDTO.getDetail());
		
		Mockito.verify(this.mockValidator, Mockito.times(1)).validate(ArgumentMatchers.eq(Endpoint.RIGHT), ArgumentMatchers.eq(fakeId), ArgumentMatchers.eq(fakeValue));
		Mockito.verify(this.mockMessageService, Mockito.times(1)).parse(ArgumentMatchers.eq(Endpoint.RIGHT), ArgumentMatchers.eq(fakeId));
	}
	
	@Test
	public void givenFakeValues_whenProcessLeftAndRightIsCalled_thenItShouldReturnThatTheProcessesHasBeenSuccessfulyExecuted() throws IOException {
		// GIVEN fake values
		final String fakeId = "fakeId";
		
		// WHEN processLeftAndRight is called
		final ResponseDTO responseDTO = this.assessmentApplicationServiceImpl.processLeftAndRight(fakeId);

		
		// THEN it should return that the processes has been successfuly executed
		Assert.assertNotNull("An instance should be returned by the service", responseDTO);
		Assert.assertEquals(ResponseMessage.LEFT_AND_RIGHT_PROCESS_RESPONSE_MESSAGE, responseDTO.getMessage());
		Assert.assertNull(responseDTO.getDetail());
		
		Mockito.verify(this.mockValidator, Mockito.times(1)).validate(ArgumentMatchers.eq(fakeId), ArgumentMatchers.any(), ArgumentMatchers.any());
	}
}
