package com.waes.assessment.service;

import java.io.IOException;
import java.io.InputStream;

import com.waes.assessment.dto.ResponseDTO;

/**
 * This is the service which handles the processes that this application is
 * related with, like value settings and their diff process
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public interface AssessmentApplicationService {

	/**
	 * This method has the goal to set the received binary data into the left memory
	 * 
	 * @param id of the memory where the binary data needs to be stored
	 * @param inputStream containing the base64 encoded binary data to be stored
	 * @return {@link ResponseDTO} containing the result of this addition
	 * @throws IOException
	 */
	ResponseDTO setLeft(String id, InputStream inputStream) throws IOException;

	/**
	 * This method has the goal to set the received binary data into the right memory
	 * 
	 * @param id of the memory where the binary data needs to be stored
	 * @param inputStream containing the base64 encoded binary data to be stored
	 * @return {@link ResponseDTO} containing the result of this addition
	 * @throws IOException
	 */
	ResponseDTO setRight(String id, InputStream inputStream) throws IOException;

	/**
	 * This method has the goal of validating and processing both stored base64 binary data
	 * in order to return to the requestor if they are equal or has any issue or difference
	 * 
	 * @param id of the memory where the binary data needs to be retrieved in both memories
	 * @return {@link ResponseDTO} containing the result of the validation/process
	 * @throws IOException
	 */
	ResponseDTO processLeftAndRight(String id) throws IOException;

}
