package com.tcs.utx.digiframe.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

import jakarta.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api")

public class BuildVersionController {

	private static final Logger LOG = LoggerFactory.getLogger(BuildVersionController.class);
	@Autowired
	private ServletContext servletContext;

	/**
	 * This method is used to fetch SVN build version. It return map contains
	 * version details.
	 * 
	 * @return buildVersionMap
	 * @throws IOException
	 */
	@RequestMapping(value = "/getversion", method = RequestMethod.GET,  produces = "application/json; charset=utf-8")

	public Map<String, String> getBuild() throws IOException {
		LOG.info("BuildVersionController | getBuild | invoked");
		Map<String, String> buildVersionMap = new HashMap<>();
		InputStream manifestStream = null;
		
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
		return buildVersionMap;
	}
}
