package org.openpcm.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openpcm.model.User;
import org.openpcm.model.UserJWTTokenState;
import org.openpcm.security.JWTRequest;
import org.openpcm.security.TokenHelper;
import org.openpcm.service.PCMUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/authenticate")
@Api(value = "/authenticate")
public class AuthenticationController extends BaseController {

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PCMUserDetailsService userDetailsService;

    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody UserJWTTokenState login(@RequestBody JWTRequest jwtRequest, HttpServletResponse response) throws AuthenticationException, IOException {

        // Perform the security
        final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

        // Inject into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // token creation
        User user = (User) authentication.getPrincipal();
        String jwsToken = tokenHelper.generateToken(user.getUsername());
        int expiresIn = tokenHelper.getExpiredIn();
        // Return the token
        return new UserJWTTokenState(jwsToken, expiresIn);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        String jwsToken = tokenHelper.getToken(request);

        if (jwsToken != null && principal != null) {
            String refreshedToken = tokenHelper.refreshToken(jwsToken);
            int expiresIn = tokenHelper.getExpiredIn();

            // we return okay with new token if we actually refreshed
            return ResponseEntity.ok(new UserJWTTokenState(refreshedToken, expiresIn));
        } else {
            UserJWTTokenState userTokenState = new UserJWTTokenState();

            // we return accepted with an empty JWT if nothing was provided.
            return ResponseEntity.accepted().body(userTokenState);
        }
    }
}
