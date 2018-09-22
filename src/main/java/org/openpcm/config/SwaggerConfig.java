package org.openpcm.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
				.securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.newArrayList(apiKey()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("OpenPCM SERVER REST API", "REST API for OpenPM Server", "0.0.1-SNAPSHOT",
				"Terms of service",
				new Contact("Raymond King", "https://github.com/OpenPCM/openpcm-server", "gsugambit@gmail.com"), "GNU",
				"https://raw.githubusercontent.com/OpenPCM/openpcm-server/master/LICENSE", Collections.emptyList());
	}

	private ApiKey apiKey() {
		return new ApiKey("AUTHORIZATION", "api_key", "header");
	}

	@Bean
	SecurityConfiguration security() {
		return new SecurityConfiguration(null, null, "test-openpcm-server", // realm Needed for authenticate button to
																			// work
				"openpcm-server", // appName Needed for authenticate button to work
				"BEARER ", // apiKeyValue
				ApiKeyVehicle.HEADER, "AUTHORIZATION", // apiKeyName
				null);
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/anyPath.*"))
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("AUTHORIZATION", authorizationScopes));
	}
}
