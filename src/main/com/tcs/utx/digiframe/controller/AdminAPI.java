package com.tcs.utx.digiframe.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import com.tcs.utx.digiframe.model.Menu;
import com.tcs.utx.digiframe.service.AdminService;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
@Validated
public class AdminAPI {

	private static final Logger LOG = LoggerFactory.getLogger(ResearcherAPI.class);

	public static final String BUGHUNTR = "BugHuntr";

	public static final String ADMIN = "Admin";

	public static final String ACCESS_DENIED = "Access Denied";

	public static final String ERROR_MSG = "You are not the bugHuntr admin";

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	@Autowired
	private PermissionHelperService permissionService;

	@Autowired
	private AdminService service;

	@Autowired
	private BrandingDetailsService brandingService;

	
	@RequestMapping(value = "menus", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getMenus() {

		Map<String, Object> retData = new HashMap<>();
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();

			if (isGuest) {
				retData.put("text", "ACCESS_DENIED");
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("AdminController | Access Denied in getMenus");
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			List<Map<String, Object>> content = new ArrayList<>();

			content = this.service.getMenus();
			retData.put("content", content);
			LOG.info("AdminController | getMenus Exit");

		} catch (DataAccessException e) {
			LOG.error("AdminController | Exception in getMenus ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "addMenu", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> addMenu(@Valid @RequestBody Menu data) {
		LOG.info("AdminController | addMenu Begin");

		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();

			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("AdminController | Access Denied in addMenu");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}

			if (!this.service.validateMenu(data)) {
				LOG.info("AdminController | Validation Failed in addMenu");
				return new ResponseEntity<>("Validation Failed", HttpStatus.BAD_REQUEST);
			}

			this.service.addMenu(data);
			
			LOG.info("AdminController | addMenu Exit");

		} catch (DataAccessException e) {
			LOG.error("AdminController | Exception in addMenu ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@RequestMapping(value = "deleteMenu/{srno}", method = RequestMethod.DELETE, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleteMenu(@Min(1) @PathVariable int srno) {
		try {
			LOG.info("BlogController | DeleteBlog Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("AdminController | Access Denied in DeleteMenu");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}
			this.service.deleteMenu(srno);

			LOG.info("AdminController | DeleteMenu Exit");
		} catch (DataAccessException e) {
			LOG.error("AdminController | Exception in deleteMenu ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Menu Deleted!", HttpStatus.OK);
	}

	@RequestMapping(value = "updateMenu", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateMenu(@Valid @RequestBody Menu data) {
		try {
			LOG.info("AdminController | UpdateMenu Begin");

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			
			int emp_id = BrandingDetailsController.getUser();
			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("AdminController | Access Denied in UpdateMenu");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}
			if (!this.service.validateMenu(data)) {
				LOG.info("AdminController | Validation Failed in addMenu");
				return new ResponseEntity<>("Validation Failed", HttpStatus.BAD_REQUEST);
			}
			this.service.updateMenu(data);
			LOG.info("AdminController | UpdateMenu Exit");
		} catch (DataAccessException e) {
			LOG.error("AdminController | Exception in UpdateMenu ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Menu Updated!", HttpStatus.OK);

	}

}
