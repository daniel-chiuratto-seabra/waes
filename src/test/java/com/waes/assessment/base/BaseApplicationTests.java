package com.waes.assessment.base;

import static com.waes.assessment.common.test.common.Util.buildInputStream;
import static com.waes.assessment.common.test.common.Util.toByteArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.assessment.common.test.common.TestEndpoint;
import com.waes.assessment.logger.WAESLoggerFactory;


/**
 * This class is a base class for all the integration tests that needs to be
 * implemented, because it contains the needed annotations to run the test itself
 * and also contains some implementations to help with the request buildings
 * according to each scenario
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public abstract class BaseApplicationTests {

	private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	protected static final Logger LOGGER = WAESLoggerFactory.getLogger(BaseApplicationTests.class);
	
	/**
	 * This enumerator is used to define the http method
	 * to be used on the corresponding request
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	protected enum HttpMethod {
		POST,
		GET;
	}
	
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * This method returns the Request Builder to
	 * help to create the request
	 * 
	 * @return {@link RequestBuilder} instance
	 */
	protected RequestBuilder getRequestBuilder() {
		return new RequestBuilder(this.mockMvc);
	}
	
	/**
	 * This method returns the Payload Builder to
	 * help to create a payload to be used on the
	 * requests
	 * 
	 * @return {@link PayloadBuilder} instance
	 */
	protected PayloadBuilder getPayloadBuilder() {
		return new PayloadBuilder();
	}
	
	/**
	 * This method logs the {@link JsonNode} passed through the
	 * parameter in a beautified manner
	 * 
	 * @param jsonNode containg the content to be logged
	 * @throws JsonProcessingException
	 */
	protected void logPayload(final JsonNode jsonNode) throws JsonProcessingException {
		LOGGER.info(OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
	}
	
	/**
	 * This class is used to build the payload to be used on the integration
	 * tests, where the payload created is in {@link JsonNode}
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	protected static class PayloadBuilder {
		
		private Map<String, Object> map;
		
		private PayloadBuilder() {
			this.map = new HashMap<>();
		}
		
		/**
		 * This method creates an element on the payload that
		 * is being created
		 * 
		 * @param fieldName of the element being added in {@link String}
		 * @param value to be added on the element
		 * @return {@link PayloadBuilder} instance since that it is a Builder pattern
		 */
		public PayloadBuilder addElement(final String fieldName, final Object value) {
			this.map.put(fieldName, value);
			return this;
		}
		
		/**
		 * Final method which actually creates a Json as {@link String}
		 * instance containing all the added elements
		 * @return {@link String}
		 * @throws JsonProcessingException
		 * @throws IllegalArgumentException
		 */
		public String create() throws JsonProcessingException, IllegalArgumentException {
			return OBJECT_MAPPER.writeValueAsString(OBJECT_MAPPER.valueToTree(this.map));
		}

		/**
		 * Final method which actually creates a Json as {@link JsonNode}
		 * instance containing all the added elements
		 * @return {@link JsonNode}
		 * @throws JsonProcessingException
		 * @throws IllegalArgumentException
		 */
		public JsonNode createAsJsonNode() throws JsonProcessingException, IllegalArgumentException {
			return OBJECT_MAPPER.valueToTree(this.map);
		}
	}
	
	/**
	 * This class is related with the actual request creation asking for the basics
	 * needed to execute the integration tests to attend this application for now 
	 * 
	 * @author Daniel Chiuratto Seabra
	 *
	 */
	public static class RequestBuilder {
		
		private MockMvc mockMvc;
		private HttpMethod httpMethod = HttpMethod.POST;
		private String body;
		private String endpoint;
		private boolean isToEncode = true;

		private RequestBuilder(final MockMvc mockMvc) {
			this.mockMvc = mockMvc;
		}
		
		/**
		 * This method sets which kind of http method the request will be doing
		 * 
		 * @param httpMethod containing the http method that is needed for the test
		 * @return {@link RequestBuilder} instance
		 */
		public RequestBuilder withHttpMethod(final HttpMethod httpMethod) {
			this.httpMethod = httpMethod;
			return this;
		}

		/**
		 * Defines which endpoint will receive the request (left or right)
		 * 
		 * @param endpoint containing the endpoint that will receive the request
		 * @param id of the payload that will be processed
		 * @return {@link RequestBuilder} instance
		 */
		public RequestBuilder withEndpoint(final TestEndpoint endpoint, final int id) {
			this.endpoint = endpoint.getUrl(id);
			return this;
		}

		/**
		 * Method which sets if the payload will be encoded or not for the request
		 * to test scenarios with invalid payloads (default is true)
		 * 
		 * @param isToEncode containing the boolean that teach the application if the
		 * payload needs to be encoded or not
		 * @return {@link RequestBuilder} instance
		 */
		public RequestBuilder isToEncode(final boolean isToEncode) {
			this.isToEncode = isToEncode;
			return this;
		}
		
		/**
		 * This method sets a body to be sent on the request on the scenario
		 * that needs to be tested
		 * 
		 * @param body containing the body for the request in {@link JsonNode} format
		 * @return {@link RequestBuilder} instance
		 */
		public RequestBuilder withBody(final String body) {
			this.body = body;
			return this;
		}
		
		/**
		 * This is the final method of the process that gets all the data set on the
		 * other methods to actually execute the request on the application
		 * 
		 * @return {@linkp ResultContainer} containing the result of the request
		 * @throws Exception
		 */
		public ResultContainer execute() throws Exception {
			MockHttpServletRequestBuilder requestBuilders = null;
			switch (this.httpMethod) {
				case POST:
					requestBuilders = MockMvcRequestBuilders.post(this.endpoint);
					if (null != this.body)
						requestBuilders.content(toByteArray(buildInputStream(isToEncode, this.body)));
					break;
				case GET:
					requestBuilders = MockMvcRequestBuilders.get(this.endpoint);
					break;
			}
			
			final MockHttpServletResponse response = this.mockMvc.perform(requestBuilders)
															     .andReturn()
															     .getResponse();
			return new ResultContainer(HttpStatus.valueOf(response.getStatus()),
									   					  response.getContentAsString());
		}

		/**
		 * This class is a container for the result of the request made on the test
		 * containing basic data needed for the comparisos such as response http status
		 * and the content itself
		 * 
		 * @author Daniel Chiuratto Seabra
		 *
		 */
		public class ResultContainer {
			
			private HttpStatus httpStatus;
			private String result;
			
			private ResultContainer(final HttpStatus httpStatus, final String result) {
				this.httpStatus = httpStatus;
				this.result = result;
			}
			
			/**
			 * Returns the {@link HttpStatus} of the response
			 * 
			 * @return {@link HttpStatus} of the response
			 */
			public HttpStatus getHttpStatus() {
				return this.httpStatus;
			}
			
			/**
			 * Gets the content of the response in {@link String} format
			 * @return {@link String} containing the data of the request response
			 */
			public String getResult() {
				return this.result;
			}
			
			/**
			 * Gets the content of the response in {@link JsonNode} format
			 * @return {@link JsonNode} containing the data of the request response
			 * @throws IOException
			 */
			public JsonNode getResultAsJsonNode() throws IOException {
				return OBJECT_MAPPER.readTree(this.result);
			}
			
			@Override
			public String toString() {
				return new StringBuilder("ResultContainer:(").append("httpStatus: ").append(this.httpStatus).append(", ")
															 .append("result: ").append(this.result)
															 .append(")")
															 .toString();
			}
		}
	}
}
