package com.tcs.utx.digiframe;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import org.springframework.core.annotation.Order;


import com.tcs.utx.digiframe.util.CSRFValidationFilter;

@WebFilter("/api/*")
@Order(1)
public class EnableCSRFFilter extends CSRFValidationFilter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		super.doFilter(request, response, chain);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	
}
