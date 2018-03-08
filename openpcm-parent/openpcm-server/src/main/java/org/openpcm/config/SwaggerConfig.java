package org.openpcm.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("org.openpcm.controller"))
                        .paths(PathSelectors.ant("/api/v1/**")).build().apiInfo(apiInfo()).securityContexts(Lists.newArrayList(actuatorSecurityContext()))
                        .securitySchemes(Lists.newArrayList(actuatorBasicAuth()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("OpenPCM REST API", "REST API for Open Patient Care Management ", "0.0.1-SNAPSHOT", "Terms of service",
                        new Contact("Raymond King", "https://github.com/gsugambit/openpcm", "gsugambit@gmail.com"), "GNU",
                        "https://raw.githubusercontent.com/gsugambit/openpcm/master/LICENSE", Collections.EMPTY_LIST);
    }

    private BasicAuth actuatorBasicAuth() {
        return new BasicAuth("basic");
    }

    private SecurityContext actuatorSecurityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("api/v1/*")).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("AUTHORIZATION", authorizationScopes));
    }
}
