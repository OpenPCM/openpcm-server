package org.openpcm.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openpcm.model.AuthSuccess;
import org.openpcm.model.User;
import org.openpcm.model.UserJWTTokenState;
import org.openpcm.security.JWTRequest;
import org.openpcm.security.TokenHelper;
import org.openpcm.service.PCMUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/authenticate")
@Api(value = "/authenticate")
public class AuthenticationController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PCMUserDetailsService userDetailsService;

    @ApiOperation(value = "login", response = UserJWTTokenState.class)
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/login")
    @ApiResponses({ @ApiResponse(code = 200, response = AuthSuccess.class, message = "logged in user and token") })
    public @ResponseBody AuthSuccess login(@ApiParam(value = "login credentials", name = "jwtRequest", required = true) @RequestBody JWTRequest jwtRequest,
                    HttpServletResponse response) throws AuthenticationException, IOException {

        // Perform the security
        final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

        // Inject into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // token creation
        final User user = (User) authentication.getPrincipal();
        final String jwsToken = tokenHelper.generateToken(user.getUsername());
        final int expiresIn = tokenHelper.getExpiredIn();

        // Return the token
        final UserJWTTokenState token = new UserJWTTokenState(jwsToken, expiresIn);

        return AuthSuccess.builder().token(token).user(user).build();
    }

    @ApiOperation(value = "refreshToken", response = UserJWTTokenState.class)
    @PostMapping(value = "/refresh")
    @ApiResponses({ @ApiResponse(code = 200, response = UserJWTTokenState.class, message = "refreshed token") })
    public ResponseEntity<UserJWTTokenState> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        final String jwsToken = tokenHelper.getToken(request);

        LOGGER.trace("jwtToken == null --> {}, principal == null --> {}", jwsToken == null, principal == null);

        if (jwsToken != null) {
            final String refreshedToken = tokenHelper.refreshToken(jwsToken);
            final int expiresIn = tokenHelper.getExpiredIn();

            // we return okay with new token if we actually refreshed
            return ResponseEntity.ok(new UserJWTTokenState(refreshedToken, expiresIn));
        } else {
            final UserJWTTokenState userTokenState = new UserJWTTokenState();

            // we return accepted with an empty JWT if nothing was provided.
            return ResponseEntity.accepted().body(userTokenState);
        }
    }
}
