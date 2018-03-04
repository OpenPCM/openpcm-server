package org.openpcm.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("org.openpcm.controller"))              
          .paths(PathSelectors.ant("/api/v1/**"))                          
          .build().apiInfo(apiInfo());                                           
    }
	
	private ApiInfo apiInfo() {
	     return new ApiInfo(
	       "OpenPCM REST API", 
	       "REST API for Open Patient Care Management ", 
	       "0.0.1-SNAPSHOT", 
	       "Terms of service", 
	       new Contact("Raymond King", "www.example.com", "gsugambit@gmail.com"), 
	       "GNU", "https://raw.githubusercontent.com/gsugambit/openpcm/master/LICENSE", Collections.EMPTY_LIST);
	}
}
