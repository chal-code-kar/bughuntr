package com.tcs.utx.digiframe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
public class SecurityConfig {

	private static final String JARTYPE = "SSO";
	private static final boolean ENABLEAUTHORIZATION = true;
	private static final boolean ENABLESESSION = true;
	private static String ADMINISTRATOR = "Administrator";

	/*
	 * This method is used to give URL patterns to which authenticated user will
	 * have access Here entry-point-ref has configured which is
	 * authenticationEntryPoint addFilter method adds filter to security filter
	 * chain. This method also has accessdeniedhandler configuration.
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationEntryPoint customEntryPoint)
			throws Exception {

		// Disable Spring's built-in CSRF — custom CSRFValidationFilter handles CSRF protection
		http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/Bughuntr/isAuthUser","/","/Bughuntr/**","/Bughuntr/login", "/index.html", "/**/index.html*", "/**/*.js*",
						"/**/*.css*","/BugHuntr/api/dologin/**", "/BugHuntr/api/menu", "/Bughuntr/login"
						)
				.permitAll()
				.anyRequest().authenticated())
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(customEntryPoint)
				.accessDeniedHandler(accessDeniedHandler()))
		    .headers(headers -> headers
	            .frameOptions(frame -> frame.deny())
	            .contentTypeOptions(contentType -> {})
	            .httpStrictTransportSecurity(hsts -> hsts.maxAgeInSeconds(31536000).includeSubDomains(true))
	            .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'")));
		return http.build();
	}

	/*
	 * This method is used to give URL patterns which needs to be ignored like
	 * js,css,images
	 *
	 */

	/**
	 * This will create bean of Http403ForbiddenEntryPoint
	 * 
	 * @return Http403ForbiddenEntryPoint
	 */
	@Bean
	public AuthenticationEntryPoint forbiddenEntryPoint() {
		return new Http403ForbiddenEntryPoint();
	}

	/**
	 * This will create bean of CustomAccessDeniedHandler
	 * 
	 * @return CustomAccessDeniedHandler
	 */
	
	@Bean
	public CustomAccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
}