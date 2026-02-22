package com.tcs.utx.digiframe.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.utx.digiframe.service.BrandingDetailsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api")

public class BuildVersionController {

	private static final Logger LOG = LoggerFactory.getLogger(BuildVersionController.class);
	@Autowired
	private BrandingDetailsService brandingService;

	/**
	 * This method is used to fetch SVN build version. It return map contains
	 * version details.
	 * 
	 * @return buildVersionMap
	 * @throws IOException
	 */
	@RequestMapping(value = "/getversion", method = RequestMethod.GET,  produces = "application/json; charset=utf-8")

	public ResponseEntity<Map<String, String>> getBuild() throws IOException {
		LOG.info("BuildVersionController | getBuild | invoked");
		Map<String, String> buildVersionMap = new HashMap<>();
		InputStream manifestStream = null;

		int emp_id = BrandingDetailsController.getUser();
		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			return new ResponseEntity<>(buildVersionMap, HttpStatus.FORBIDDEN);
		}

		try {
			String version ="1.5";

			buildVersionMap.put("version", version);
		} finally {
			if (manifestStream != null) {
				try {
					manifestStream.close();	
				}catch(IOException e) {
					LOG.error("BuildVersionController | IOException in getBuild", e);
				}
			}
		}

		LOG.info("BuildVersionController | getBuild | leaving");
		return new ResponseEntity<>(buildVersionMap, HttpStatus.OK);
	}
}
