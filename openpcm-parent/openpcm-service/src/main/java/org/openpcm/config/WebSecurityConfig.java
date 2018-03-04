package org.openpcm.config;

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig/* extends WebSecurityConfigurerAdapter */ {

	private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };
	
//	@Autowired
//	private AuthSuccessHandler authHandler;
//
//	@Autowired
//	private AuthFailureHandler authFailureHandler;
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests().antMatchers("/v2/api-docs").permitAll().antMatchers("/").permitAll().
//
//		// allow swagger to work without login
//				antMatchers("/webjars/springfox-swagger-ui/*").permitAll()
//				.antMatchers("/webjars/springfox-swagger-ui/*/*").permitAll().antMatchers("/swagger-resources/*/*")
//				.permitAll().antMatchers("/swagger-resources/*").permitAll().antMatchers("/swagger-resources")
//				.permitAll().antMatchers("/swagger-ui.html").permitAll().antMatchers("/logfile").permitAll()
//				.antMatchers("/logfile/*").permitAll().antMatchers("/favicon.ico").permitAll()
//				.antMatchers("/ui-websocket").permitAll().antMatchers("/ui-websocket/*").permitAll();
//
//				antMatchers(HttpMethod.POST, "/login").permitAll().anyRequest().authenticated().and().formLogin()
//				.loginPage("/login").successHandler(authHandler).failureHandler(authFailureHandler)
//				.usernameParameter("username").passwordParameter("password").permitAll();
//
//	}
//
//	@Component
//	public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//		private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//		@Override
//		protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//				throws IOException {
//			redirectStrategy.sendRedirect(request, response, "/home");
//		}
//	}
//
//	@Component
//	public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//
//		private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//		@Override
//		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//				AuthenticationException exception) throws IOException, ServletException {
//			System.out.println("AuthFailureHandler.onAuthenticationFailure()");
//			redirectStrategy.sendRedirect(request, response, "/login?msg=Bad Credentials");
//		}
//	}
}
