package com.tcs.utx.digiframe;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;



@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

   

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		System.out.print(request.getRequestURI());
		System.out.print(request.getRequestURL());

		
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
		    response.setContentType("application/json");
		    response.getWriter().write("ACCESS DENIED");
	}
}
