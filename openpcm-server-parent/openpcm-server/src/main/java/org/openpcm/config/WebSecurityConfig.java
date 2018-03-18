package org.openpcm.config;

import org.openpcm.security.JWTFilter;
import org.openpcm.security.OpenPCMAuthenticationEntryPoint;
import org.openpcm.security.TokenHelper;
import org.openpcm.service.PCMUserDetailsService;
import org.openpcm.utils.OperationIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final OpenPCMAuthenticationEntryPoint authenticationEntryPoint;
	private final TokenHelper tokenHelper;
	private final PasswordEncoder passwordEncoder;
	private final boolean webDebug;

	// Cant autowire avoiding circular dependency loop
	@Autowired
	private PCMUserDetailsService userDetailsService;

	@Autowired
	public WebSecurityConfig(OpenPCMAuthenticationEntryPoint authenticationEntryPoint, TokenHelper tokenHelper,
			PasswordEncoder passwordEncoder, @Value("${openpcm.web.debug:false}") boolean webDebug) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.tokenHelper = tokenHelper;
		this.passwordEncoder = passwordEncoder;
		this.webDebug = webDebug;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and().authorizeRequests()
				.antMatchers("/api/v1/**", "/api/v1/*").permitAll().anyRequest().authenticated().and()
				.addFilterBefore(new JWTFilter(tokenHelper, userDetailsService), BasicAuthenticationFilter.class)
				.addFilterBefore(new OperationIdInterceptor(), JWTFilter.class);

		http.csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public Module datatypeHibernateModule() {
		return new Hibernate5Module();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.debug(webDebug);
		web.ignoring().antMatchers(HttpMethod.POST, "/authenticate/*").and().ignoring().antMatchers(AUTH_WHITELIST);
	}

	private static final String[] AUTH_WHITELIST = { "/v2/**", "/swagger-resources", "/swagger-resources/**",
			"/swagger-ui.html", "/webjars/**", "/configuration/ui", "/configuration/security", "/actuator/*", "/h2",
			"/h2/", "/h2/**" };
}
