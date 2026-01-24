package com.tcs.utx.digiframe;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication(scanBasePackages = { "com.tcs.utx.digiframe" })
@EnableScheduling
public class BughuntrApplication {
	
	 public static void main(String[] args) {
	        SpringApplication.run(BughuntrApplication.class, args);
	 }
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(5242880); 
		return resolver;
	}


	
	@Bean
	public ServletContextInitializer servletContextInitializer() {
	    return new ServletContextInitializer() {

	        @Override
	        public void onStartup(ServletContext servletContext) throws ServletException {
	            servletContext.getSessionCookieConfig().setHttpOnly(true);
	        }
	    };
	}
	
	
	
	@Bean
    public FilterRegistrationBean<EnableCSRFFilter> csrfFilter() {
        FilterRegistrationBean<EnableCSRFFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EnableCSRFFilter());
        registrationBean.addUrlPatterns("/BugHuntr/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
 
	

}
