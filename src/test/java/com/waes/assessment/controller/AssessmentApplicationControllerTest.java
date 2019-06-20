package com.waes.assessment.controller;

import java.io.ByteArrayInputStream;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.waes.assessment.dto.ResponseDTO;
import com.waes.assessment.service.AssessmentApplicationService;


@RunWith(MockitoJUnitRunner.class)
public class AssessmentApplicationControllerTest {

	@Mock
	private AssessmentApplicationService mockAssessmentApplicationService;
	
	@InjectMocks
	private AssessmentApplicationController assessmentApplicationController;
	
	@Test
	public void givenALeftCall_whenItIsMade_thenItShouldCallTheSetLeftInServiceAndReturnAFakePayload() throws IOException {
		final String fakeId = "fakeId";
		final InputStream fakeInputStream = Mockito.mock(ByteArrayInputStream.class);
		final ResponseDTO fakeResponseDTO = ResponseDTO.builder().build();

		Mockito.when(this.mockAssessmentApplicationService.setLeft(ArgumentMatchers.eq(fakeId), ArgumentMatchers.eq(fakeInputStream)))
														  .thenReturn(fakeResponseDTO);

		final ResponseEntity<ResponseDTO> result = this.assessmentApplicationController.left(fakeId, fakeInputStream);
		Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assert.assertTrue("The returned response should be the same instance of the expected one", fakeResponseDTO == result.getBody());
	}

	@Test
	public void givenARightCall_whenItIsMade_thenItShouldCallTheSetRightInServiceAndReturnAFakePayload() throws IOException {
		final String fakeId = "fakeId";
		final InputStream fakeInputStream = Mockito.mock(ByteArrayInputStream.class);
		final ResponseDTO fakeResponseDTO = ResponseDTO.builder().build();

		Mockito.when(this.mockAssessmentApplicationService.setRight(ArgumentMatchers.eq(fakeId), ArgumentMatchers.eq(fakeInputStream)))
														  .thenReturn(fakeResponseDTO);

		final ResponseEntity<ResponseDTO> result = this.assessmentApplicationController.right(fakeId, fakeInputStream);
		Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assert.assertTrue("The returned response should be the same instance of the expected one", fakeResponseDTO == result.getBody());
	}
	
	@Test
	public void test() throws IOException {
		final String fakeId = "fakeId";
		final ResponseDTO fakeResponseDTO = ResponseDTO.builder().build();
		
		Mockito.when(this.mockAssessmentApplicationService.processLeftAndRight(ArgumentMatchers.eq(fakeId))).thenReturn(fakeResponseDTO);
		
		final ResponseEntity<ResponseDTO> result = this.assessmentApplicationController.id(fakeId);
		Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assert.assertTrue("The returned response should be the same instance of the expected one", fakeResponseDTO == result.getBody());
	}
}
