package com.waes.assessment.validator;

import java.io.IOException;

import com.waes.assessment.common.Endpoint;


public interface Validator {

	void validate(Endpoint endpoint, String id, String value) throws IOException;

	void validate(String id, String left, String right) throws IOException;

}
