package com.tcs.utx.digiframe.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.owasp.esapi.ESAPI;
//import org.owasp.esapi.HTTPUtilities;
//import org.owasp.esapi.errors.AccessControlException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/BugHuntr/api")



public class LogoutController {

	private static final Logger LOG = LoggerFactory.getLogger(LogoutController.class);
	
	private static String expiresString = "Expires";
	private static String cacheControlString = "Cache-Control";
	private static String noCacheString = "no-cache";
	
	/**
	 * On logout this method will be called and it will redirect to logout URL
	 * specified in properties file.
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		LOG.info("LogoutController | logout action initiated ");
		HttpSession session=request.getSession(false);
		if(session!=null) {
			SecurityContextHolder.clearContext();
			session.invalidate();
		}
		LOG.info("LogoutController | logout action terminated ");
		response.setHeader(cacheControlString, "no-cache, no-store, must-revalidate, private, max-age=0");
		response.setHeader("Pragma", noCacheString);
		response.setDateHeader(expiresString, 0);
		}

	/**
	 * This method will be called on clicking Exit from current application.
	 * @param response
	 */
	@RequestMapping(value = "/logoutApp", method = RequestMethod.POST)
	public void logoutAppSession(HttpServletRequest request,HttpServletResponse response) {
		LOG.info("LogoutController | logoutAppSession action initiated ");
		HttpSession session=request.getSession(false);
		if(session!=null) {
			SecurityContextHolder.clearContext();
			session.invalidate();
		}
		response.setHeader(cacheControlString, "no-cache, no-store, must-revalidate, private, max-age=0");
		response.setHeader("Pragma", noCacheString);
		response.setDateHeader(expiresString, 0);
		LOG.info("LogoutController | logoutAppSession action terminated ");
	}
	
}
