package com.waes.assessment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is inteded to contain the beans that needs to be controlled
 * by Spring to allow their access through the entire application
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

	/**
	 * This method instantiates a {@link ObjectMapper} object to allow Spring
	 * to control it through the entire application, in order to avoid useless
	 * multiple instances everytime that a {@link JsonNode} parsing is needed
	 * 
	 * @return {@link ObjectMapper} instance
	 */
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
}
