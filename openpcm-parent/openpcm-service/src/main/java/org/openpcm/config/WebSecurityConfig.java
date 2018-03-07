package org.openpcm.config;

import org.openpcm.service.PCMUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PCMUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(PCMUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    protected void configure(HttpSecurity http) throws Exception {
        // will disabled this when deployment strategy is finalized
        // could be two separate docker containers so not needed, could serve up both together or could have separate
        // for now we need this enabled
        // http.;
        // http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();

        http.csrf();
        http.httpBasic().disable();
        http.authorizeRequests().antMatchers("/api/v1/**").authenticated().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().permitAll().and().formLogin()
                        .permitAll();
    }

    // @Override
    // public void init(WebSecurity web) throws Exception {
    // super.init(web);
    // web.ignoring().antMatchers(AUTH_WHITELIST);
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * White list to include all points that should be accessible without authentication
     */
    private static final String[] AUTH_WHITELIST = { "/v2/**", "/swagger-resources", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
                    "/configuration/ui", "/configuration/security", };
}
