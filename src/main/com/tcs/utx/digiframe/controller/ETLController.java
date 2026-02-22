package com.tcs.utx.digiframe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.utx.digiframe.model.Etldata;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.ETLService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")

public class ETLController {

	private static final Logger LOG = LoggerFactory.getLogger(ETLController.class);
	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	@Autowired
	private ETLService ETLService;

	@Autowired
	private PermissionHelperService permissionService;

	@Autowired
	private BrandingDetailsService brandingService;


	@RequestMapping(value = "etldata", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> ETLData() {
		List<Map<String, Object>> data = new ArrayList<>();
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible("BugHuntr", "Admin", "View", emp_id, 0, 0)) {
				LOG.info("ETLController | Access Denied in ETLData");
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			data = this.ETLService.ETLData();

		} catch (DataAccessException e) {
			LOG.error("ETLController | Exception in  getetldata", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ETLController | Exception in getetldata", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "Postetldata", method = RequestMethod.POST)
	public ResponseEntity<String> Postetldata(@RequestBody Etldata postetldata) {
		try {
			LOG.info("ETLController | postetldata Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible("BugHuntr", "Admin", "View", emp_id, 0, 0)) {
				LOG.info("ETLController | Access Denied in postetldata");
				return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
			}

			if (this.permissionService.isUserPresent(Integer.toString(postetldata.getEmployee_number()))) {
				LOG.info("ETLController | Access Denied in postetldata");
				return new ResponseEntity<>("User already has been added in ETL Data", HttpStatus.BAD_REQUEST);
			}
			this.ETLService.Postetldata(postetldata);
			LOG.info("ETLController | postetldata Exit");
		} catch (DataAccessException e) {
			LOG.error("ETLController | Exception in postetldata ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ETLController | Exception in postetldata", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("data Updated!", HttpStatus.OK);

	}

}
