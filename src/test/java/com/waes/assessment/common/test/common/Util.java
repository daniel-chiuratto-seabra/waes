package com.waes.assessment.common.test.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This utilitary interface contains methods which helps
 * with the serialization of the data to be used on the integration tests
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public interface Util {

	/**
	 * This method parses the informed message in {@link String} format into {@link InputStream}
	 * 
	 * @param isToEncode sets if the corresponding message should be Base64 encoded or not
	 * @param message containing the {@link String} data
	 * @return parsed data in {@link InputStream}
	 * @throws JsonProcessingException
	 */
	public static InputStream buildInputStream(final boolean isToEncode, final String message) throws JsonProcessingException {
		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
		if (isToEncode)
			bytes = Base64.getEncoder().encode(bytes);
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * This method parses the informed {@link InputStream} into binary data (byte array)
	 * @param inputStream containing the data to be parsed
	 * @return {@code byte[]} of the corresponding {@link InputStream}
	 * @throws IOException
	 */
	public static byte[] toByteArray(final InputStream inputStream) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int len;
		
		while((len = inputStream.read(buffer)) != -1)
			baos.write(buffer, 0, len);

		return baos.toByteArray();
	}
	
	/**
	 * This method parses the informed {@link JsonNode} into a {@link String}
	 * @param jsonNode containing the data to be parsed
	 * @return {@link String}
	 * @throws JsonProcessingException
	 */
	public static String parse(final JsonNode jsonNode) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(jsonNode);
	}
	
}
