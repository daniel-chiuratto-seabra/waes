package com.waes.assessment.validator.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.waes.assessment.common.Constant;
import com.waes.assessment.common.Constant.LogMessage;
import com.waes.assessment.common.Endpoint;
import com.waes.assessment.common.JsonUtil;
import com.waes.assessment.exception.MissingInMemoryPayloadException;
import com.waes.assessment.exception.PayloadContentMismatchException;
import com.waes.assessment.exception.PayloadSizeMismatchException;
import com.waes.assessment.exception.RequiredPayloadException;
import com.waes.assessment.logger.WAESLoggerFactory;
import com.waes.assessment.service.MessageService;
import com.waes.assessment.validator.Validator;

@Component
public class ValidatorImpl implements Validator {
	
	private static final Logger LOGGER = WAESLoggerFactory.getLogger(ValidatorImpl.class);

	private static final Map<String, Object> CACHE_MAP = new HashMap<>();
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public void validate(final Endpoint endpoint, final String id, final String value) throws IOException {
		if (CACHE_MAP.containsKey(id))
			CACHE_MAP.remove(id);
		
		if (StringUtils.isBlank(value))
			throw new RequiredPayloadException(endpoint, id);
	}

	@Override
	public void validate(final String id, String left, String right) throws IOException {
		
		LOGGER.info(LogMessage.STARTING_VALIDATION);
		
		if (CACHE_MAP.containsKey(id)) {
			LOGGER.info(LogMessage.RETRIEVING_CACHED_DATA);

			final Object object = CACHE_MAP.get(id);
			if (RuntimeException.class.isAssignableFrom(object.getClass())) {
				final RuntimeException exception = (RuntimeException) object;
				
				LOGGER.error(LogMessage.THROWING_CACHED_EXCEPTION, exception);

				throw exception;
			} else if (Constant.OK_STATUS.equals(object)) {
				LOGGER.info(LogMessage.NO_ERRORS_CACHED_DATA);
				
				return;
			}
		}
		
		if (null == left) {
			final MissingInMemoryPayloadException exception = new MissingInMemoryPayloadException(Endpoint.LEFT, id);
			CACHE_MAP.put(id, exception);
			throw exception;
		}
		
		if (null == right) {
			final MissingInMemoryPayloadException exception = new MissingInMemoryPayloadException(Endpoint.RIGHT, id);
			CACHE_MAP.put(id, exception);
			throw exception;
		}
		
		final int leftSize = left.length();
		final int rightSize = right.length();
		if (leftSize != rightSize) {
			final PayloadSizeMismatchException exception = new PayloadSizeMismatchException(leftSize, rightSize);
			CACHE_MAP.put(id, exception);
			throw exception;
		}
		
		final JsonNode leftNode = this.messageService.parseJsonNode(Endpoint.LEFT, left);
		final JsonNode rightNode = this.messageService.parseJsonNode(Endpoint.RIGHT, right);

		if (!leftNode.equals(rightNode)) {
			final Map<String, Map<String, Object>> result = JsonUtil.buildDiffMapResult(leftNode, rightNode);
			final PayloadContentMismatchException exception = new PayloadContentMismatchException(result);
			CACHE_MAP.put(id, exception);
			throw exception;
		}

		CACHE_MAP.put(id, Constant.OK_STATUS);
		
		LOGGER.info(LogMessage.FINISHING_VALIDATION_NO_ISSUES);
	}

}
