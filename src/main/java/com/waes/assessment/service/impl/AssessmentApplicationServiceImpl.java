package com.waes.assessment.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waes.assessment.common.Constant.LogMessage;
import com.waes.assessment.common.Constant.ResponseMessage;
import com.waes.assessment.common.Endpoint;
import com.waes.assessment.dto.ResponseDTO;
import com.waes.assessment.logger.WAESLoggerFactory;
import com.waes.assessment.service.AssessmentApplicationService;
import com.waes.assessment.service.MessageService;
import com.waes.assessment.validator.Validator;

@Service
public class AssessmentApplicationServiceImpl implements AssessmentApplicationService {

	private static final Logger LOGGER = WAESLoggerFactory.getLogger(AssessmentApplicationServiceImpl.class);

	private static final Map<String, String> IN_MEMORY_LEFT_MAP = new HashMap<>();
	private static final Map<String, String> IN_MEMORY_RIGHT_MAP = new HashMap<>();
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public ResponseDTO setLeft(final String id, final InputStream inputStream) throws IOException {
		final String parsedString = this.messageService.parseString(Endpoint.LEFT, inputStream);

		this.validator.validate(Endpoint.LEFT, id, parsedString);
		
		IN_MEMORY_LEFT_MAP.put(id, parsedString);

		LOGGER.info(LogMessage.MEMORY_SETTING_LOG_MESSAGE, Endpoint.LEFT.getValue(), id);

		return ResponseDTO.builder().withMessage(this.messageService.parse(Endpoint.LEFT, id))
								    .build();
	}

	@Override
	public ResponseDTO setRight(final String id, final InputStream inputStream) throws IOException {
		final String parsedString = this.messageService.parseString(Endpoint.RIGHT, inputStream);
		
		this.validator.validate(Endpoint.RIGHT, id, parsedString);
		
		IN_MEMORY_RIGHT_MAP.put(id, parsedString);

		LOGGER.info(LogMessage.MEMORY_SETTING_LOG_MESSAGE, Endpoint.RIGHT.getValue(), id);
		
		return ResponseDTO.builder().withMessage(this.messageService.parse(Endpoint.RIGHT, id))
								    .build();
	}

	@Override
	public ResponseDTO processLeftAndRight(final String id) throws IOException {
		LOGGER.info(LogMessage.LEFT_AND_RIGHT_PROCESS_LOG_MESSAGE, id);
		
		this.validator.validate(id, IN_MEMORY_LEFT_MAP.get(id), IN_MEMORY_RIGHT_MAP.get(id));

		return ResponseDTO.builder().withMessage(ResponseMessage.LEFT_AND_RIGHT_PROCESS_RESPONSE_MESSAGE)
								    .build();
	}

}
