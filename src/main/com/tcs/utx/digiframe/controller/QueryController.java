package com.tcs.utx.digiframe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import com.tcs.utx.digiframe.model.Query;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
@Validated
public class QueryController {

	private static final Logger LOG = LoggerFactory.getLogger(HelpAPI.class);

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXIT = "QueryController | Query Exit";

	private String TEXT_VALIDATION = "Validation Failed";

	private String TEXT_BUGHUNTR = "BugHuntr";

	private String TEXT_ADMIN = "Admin";

	private String TEXT_NOTADMIN = "Non Admin";

	public static final String GUEST = "Guest";

	public static final String ACCESS_DENIED = "ACCESS_DENIED";
	
	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	@Autowired
	private QueryService QueryService;

	@Autowired
	private BrandingDetailsService brandingService;

	@Autowired
	private PermissionHelperService service;

	public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int vulnid,
			int programid) {
		return this.service.isOperationPermissible(module, submodule, Perm_Type, userid, vulnid, programid);
	}

	@RequestMapping(value = "PostQuery", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> PostQuery(@Valid @RequestBody Query query) {
		try {
			LOG.info("QueryController | PostQuery Begin");
			
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			if (!this.QueryService.validateQueryPost(query)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			query.setCreatedby(emp_id);
			this.QueryService.PostQuery(query);

			LOG.info("QueryController | PostQuery Exit");

		} catch (DataAccessException e) {
			LOG.error("QueryController | Exception in PostQuery - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in PostQuery ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Query Posted!", HttpStatus.OK);

	}

	@RequestMapping(value = "MyQuery/{emp_id}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> MYQuery(@Min(1) @PathVariable int emp_id) {
		List<Map<String, Object>> data = new ArrayList<>();
		try {
			LOG.info("QueryController | AllQuery Begin");
			int authenticated_emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			// Users can only view their own queries unless they are admin
			if (authenticated_emp_id != emp_id &&
				!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", authenticated_emp_id, 0, 0)) {
				LOG.info("QueryController | Access Denied in MYQuery - user attempted to view another user's queries");
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			data = this.QueryService.MYQuery(emp_id);
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			LOG.error("QueryController | Exception in MYQuery - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in MYQuery ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "AllQuery", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> AllQuery(){
		LOG.info("QueryController | AllQuery Begin");

		List<Map<String, Object>> data = new ArrayList<>();

		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			if (this.service.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("QueryController | Access Denied in AllQuery");
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			data = this.QueryService.AllQuery();
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			LOG.error("QueryController | Exception in AllQuery - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in AllQuery ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	

	@RequestMapping(value = "updatequery", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updatequery(@Valid @RequestBody Query updatequery) {

		LOG.info("PermissionHelperController | UpdateQuery Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();
			
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.QueryService.validateQuery(updatequery)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			updatequery.setAnswerby(emp_id);

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in UpdateQuery");
				return new ResponseEntity<>(TEXT_NOTADMIN, HttpStatus.FORBIDDEN);
			}
			this.QueryService.updatequery(updatequery);

			LOG.info("PermissionHelperController | UpdateQuery Exit");
		} catch (DataAccessException e) {
			LOG.error("QueryController | Exception in updatequery - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in updatequery ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Query Updated!", HttpStatus.OK);

	}


}
