package com.waes.assessment.service;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.waes.assessment.common.Endpoint;

/**
 * This interface is focused in message parsing process where it containg
 * methods which parses data coming from outside as well methods that
 * parses data that needs to be returned to the requestor
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public interface MessageService {

	/**
	 * This method parses the received Base64 encoded {@code String} into a
	 * {@link JsonNode} for further processes
	 * 
	 * @param endpoint containing which endpoint that called the method
	 * @param jsonString containing the Base64 encoded {@link String}
	 * @return parsed {@link JsonNode} instance
	 * @throws IOException
	 */
	JsonNode parseJsonNode(Endpoint endpoint, String jsonString) throws IOException;

	/**
	 * This method parses the recevied Base64 encoded binary data into a
	 * {@link String}
	 * 
	 * @param endpoint containing which endpoint that called the method
	 * @param inputStream containing the base64 encoded binary data
	 * @return paesed {@link String}
	 */
	String parseString(Endpoint endpoint, InputStream inputStream);

	/**
	 * This method parses the received {@link Endpoint} and {@code id} values into
	 * a default response message to the requestor, saying that the process has been
	 * successfuly executed
	 * 
	 * @param endpoint containing which endpoint that called the method
	 * @param id containing the memory id where the process has been executed
	 * @return the parsed message to be returned to the requestor
	 */
	String parse(Endpoint endpoint, String id);

}
