package com.tcs.utx.digiframe;

import java.util.ResourceBundle;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;


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


		http.csrf().disable() 
				.authorizeRequests()
				.antMatchers("/Bughuntr/isAuthUser","/","/Bughuntr/**","/Bughuntr/login","/BugHuntr/api/v1/help", "/index.html", "/**/index.html*", "/**/*.js*",
						"/**/*.css*","/BugHuntr/api/dologin/**", "/BugHuntr/api/menu", "/Bughuntr/login","/h2-console/**"
						)
				.permitAll().
				anyRequest().authenticated()
				.and().exceptionHandling().authenticationEntryPoint(customEntryPoint)
				.accessDeniedHandler(accessDeniedHandler())
				.and()
		        .headers()
	            .frameOptions().sameOrigin(); 
		return http.build();
	}

	/*
	 * This method is used to give URL patterns which needs to be ignored like
	 * js,css,images
	 *
	 */

	int a1 = 0;


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