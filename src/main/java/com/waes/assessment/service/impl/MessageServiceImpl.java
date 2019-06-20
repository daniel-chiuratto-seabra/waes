package com.waes.assessment.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.assessment.common.Constant.LogMessage;
import com.waes.assessment.common.Constant.ResponseMessage;
import com.waes.assessment.common.Endpoint;
import com.waes.assessment.exception.DeserializationException;
import com.waes.assessment.logger.WAESLoggerFactory;
import com.waes.assessment.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	
	private static final Logger LOGGER = WAESLoggerFactory.getLogger(MessageServiceImpl.class);

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public JsonNode parseJsonNode(final Endpoint endpoint, final String jsonString) throws IOException {
		try {
			final String decMessage = new String(Base64.getDecoder().decode(jsonString), Charset.forName("UTF-8"));
			return this.objectMapper.readTree(decMessage);
		} catch (final Exception e) {
			LOGGER.error(LogMessage.DESERIALIZATION_ERROR_MESSAGE, e);
			throw new DeserializationException(endpoint, e);
		}
	}
	
	@Override
	public String parse(final Endpoint endpoint, final String id) {
		if (null == endpoint)
			return null;
		String message = null;
		if (null != id && id.trim().length() > 0)
			message = String.format(ResponseMessage.LEFT_OR_RIGHT_RESPONSE_MESSAGE_WITH_ID, endpoint.getValue(), id);
		else message = String.format(ResponseMessage.LEFT_OR_RIGHT_RESPONSE_MESSAGE_WITHOUT_ID, endpoint.getValue());
		return message;
	}
	
	@Override
	public String parseString(final Endpoint endpoint, final InputStream inputStream) {
		try {
			return IOUtils.toString(inputStream, Charset.forName("UTF-8"));
		} catch (final IOException e) {
			LOGGER.error(LogMessage.DESERIALIZATION_ERROR_MESSAGE, e);
			throw new DeserializationException(endpoint, e);
		}
	}
}
