package com.tcs.utx.digiframe;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tcs.utx.digiframe.util.CSRFValidationFilter;

@Configuration
public class EnableCSRFFilter {

	@Bean
	public FilterRegistrationBean<CSRFValidationFilter> csrfValidationFilter() {
		FilterRegistrationBean<CSRFValidationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CSRFValidationFilter());
		registrationBean.addUrlPatterns("/BugHuntr/api/*");
		registrationBean.setOrder(1);
		return registrationBean;
	}
}
