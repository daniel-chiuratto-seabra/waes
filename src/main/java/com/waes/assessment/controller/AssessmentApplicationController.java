package com.waes.assessment.controller;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waes.assessment.common.Constant.LogMessage;
import com.waes.assessment.dto.ResponseDTO;
import com.waes.assessment.logger.WAESLoggerFactory;
import com.waes.assessment.service.AssessmentApplicationService;

/**
 * This class is the controller which contains the endpoints available to use the application by
 * external applications
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
@RestController
@RequestMapping("/v1/diff")
public class AssessmentApplicationController {
	
	private static final Logger LOGGER = WAESLoggerFactory.getLogger(AssessmentApplicationController.class);

	@Autowired
	private AssessmentApplicationService assessmentApplicationService;
	
	/**
	 * This endpoint sets a payload into the left memory on the specific id
	 * 
	 * @param id of the payload to save it into the left memory
	 * @param inputStream containing the Base64 encoded binary data to be store into memory
	 * @return {@link ResponseDTO} containing the result of the setting
	 * @throws IOException
	 */
	@PostMapping(value = "/{ID}/left", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ResponseDTO> left(final @PathVariable("ID") String id, final InputStream inputStream) throws IOException {
		LOGGER.info(LogMessage.CONTROLLER_LEFT_OR_RIGHT_LOG_MESSAGE, id, "left");

		return ResponseEntity.ok(this.assessmentApplicationService.setLeft(id, inputStream));
	}

	/**
	 * This endpoint sets a payload into the right memory on the specific id
	 * 
	 * @param id of the payload to save it into the right memory
	 * @param inputStream containing the Base64 encoded binary data to be store into memory
	 * @return {@link ResponseDTO} containing the result of the setting
	 * @throws IOException
	 */
	@PostMapping(value = "/{ID}/right", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ResponseDTO> right(final @PathVariable("ID") String id, final InputStream inputStream) throws IOException {
		LOGGER.info(LogMessage.CONTROLLER_LEFT_OR_RIGHT_LOG_MESSAGE, id, "right");
		
		return ResponseEntity.ok(this.assessmentApplicationService.setRight(id, inputStream));
	}
	
	/**
	 * This endpoint retrieves both payloads from both memory sides and validates it returning the
	 * validation result according to the requirements
	 * 
	 * @param id of the payloads stored on both left and right memories
	 * @return {@link ResponseDTO} containing the validation result
	 * @throws IOException
	 */
	@GetMapping(value = "/{ID}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ResponseDTO> id(final @PathVariable("ID") String id) throws IOException {
		LOGGER.info(LogMessage.CONTROLLER_LEFT_AND_RIGHT_PROCESS_LOG_MESSAGE, id);
		
		return ResponseEntity.ok(this.assessmentApplicationService.processLeftAndRight(id));
	}

}
