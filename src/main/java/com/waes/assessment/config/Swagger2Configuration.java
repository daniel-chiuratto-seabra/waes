package com.waes.assessment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.waes.assessment.common.Constant.SwaggerDetail;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class sets the configuration for Swagger 2 to allow it
 * to work properly on the application
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

	/**
	 * The following method creates a {@link Docket} object to start the Swagger 2 up
	 * 
	 * @return {@link Docket} instance
	 */
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2).select()                                  
											          .apis(RequestHandlerSelectors.any())              
											          .paths(PathSelectors.any())                          
											          .build()
											          .apiInfo(this.metaData());                                           
    }
    
    /**
     * The following method creates a {@link ApiInfo} instance to fill the Swagger 2 API
     * Document page with data
     * 
     * @return {@link ApiInfo} instance
     */
    private ApiInfo metaData() {
        return new ApiInfoBuilder().title(SwaggerDetail.TITLE)
					               .description(SwaggerDetail.DESCRIPTION)
					               .version(SwaggerDetail.VERSION)
					               .license(SwaggerDetail.LICENSE)
					               .licenseUrl(SwaggerDetail.LICENSE_URL)
					               .build();
      }
}
