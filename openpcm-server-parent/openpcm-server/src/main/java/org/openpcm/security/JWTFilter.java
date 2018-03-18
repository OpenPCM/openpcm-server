package org.openpcm.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {

	private TokenHelper tokenHelper;

	private UserDetailsService userDetailsService;

	public JWTFilter(TokenHelper tokenHelper, UserDetailsService userDetailsService) {
		this.tokenHelper = tokenHelper;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String username;
		String authToken = tokenHelper.getToken(request);

		if (authToken != null) {
			// get username from token
			username = tokenHelper.getUsernameFromToken(authToken);
			if (username != null) {
				// get user
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (tokenHelper.validateToken(authToken, userDetails)) {
					// create authentication
					OpenPCMAuthenticationToken authentication = new OpenPCMAuthenticationToken(userDetails);
					authentication.setCredentials(authToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}

}