package org.openpcm.login;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Authentication API specification for Swagger documentation and Code Generation. Implemented by Spring Security.
 */
@Api("Authentication")
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public interface SpringLoginControllerDocumentation {
    /**
     * Implemented by Spring Security
     */
    @ApiOperation(value = "Login", notes = "Login with the given credentials.")
    @ApiResponses({ @ApiResponse(code = 200, message = "", response = Authentication.class) })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    default void login(@RequestParam("username") String username, @RequestParam("password") String password) {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }

    /**
     * Implemented by Spring Security
     */
    @ApiOperation(value = "Logout", notes = "Logout the current user.")
    @ApiResponses({ @ApiResponse(code = 200, message = "") })
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    default void logout() {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }
}