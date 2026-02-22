package com.tcs.utx.digiframe.controller;

import java.util.ArrayList;
import java.util.Base64;
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

import com.tcs.utx.digiframe.model.History;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.HistoryService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")

public class HistoryController {

	private static final Logger LOG = LoggerFactory.getLogger(HelpAPI.class);

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	public static final String ERROR = "Something Went Wrong.";

	private static final String TEXT_ADMIN = "Admin";

	private static final String TEXT_BUGHUNTR = "BugHuntr";

	private static final String TEXT_DELETE = "Delete";

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXIT = "AdminAPIController | history Exit";

	public static final String SAME_MENU_ERROR = "Something Went Wrong. Help with same Menu Name mey be already present.";

	public static final String ERROR_MSG = "You are not the bugHuntr admin";

	public static final String TEXT_VALIDATION = "TEXT VALIDATION";
	public static final String ACCESS_DENIED = "ACCESS_DENIED";
	public static final String GUEST = "Guest";

	@Autowired
	private HistoryService HistoryService;

	@Autowired
	private PermissionHelperService permissionService;

	@Autowired
	private BrandingDetailsService brandingService;

	@RequestMapping(value = "history", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> getHistory() {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			LOG.info("HistroyController | Gethistory Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				map.put(ACCESS_DENIED, "{\"ACCESS_DENIED\":\"Access Denied.\"}");
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			if (this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("HistoryController | Access Denied in Get History");

				map.put(ACCESS_DENIED, "{\"ACCESS_DENIED\":\"Access Denied.\"}");
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			data = this.HistoryService.getHistory();
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			LOG.error("HistroyController | Exception in Gethistory - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("HistroyController | Exception in Gethistory ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "Posthistory", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> Posthistory(@RequestBody History posthistory) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			LOG.info("HistoryController | FAQQuery Begin");
			int emp_id = BrandingDetailsController.getUser();
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				map.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isAuthorized(emp_id)) {
				map.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
			}
			if (!this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("HistoryController | Access Denied in History");
				map.put(ERROR_MSG, ERROR_MSG);
				return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
			}
			
			this.HistoryService.Posthistory(posthistory);
			map.put("Success", "Item Added");
			LOG.info("HistroyController |  Exit");
		} catch (DataAccessException e) {
			LOG.error("HistoryController | Exception in History - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("HistoryController | Exception in History ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(map, HttpStatus.OK);

	}

	@RequestMapping(value = "updateHistory/{srno}", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateHistory(@PathVariable int srno, @RequestBody History edithistory) {

		LOG.info("AdminController | updateHistory Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			if (!this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("HistoryController | Access Denied in History");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}
			
			this.HistoryService.updateHistory(srno, edithistory);
			LOG.info("HistoryController | updateHistory Exit");
		} catch (DataAccessException e) {
			LOG.error("HistoryController | Exception in updateHistory - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("HistoryController | Exception in updateHistory ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("data Updated!", HttpStatus.OK);

	}

	@RequestMapping(value = "deleteHistory/{srno}", method = RequestMethod.GET,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleteHistory(@PathVariable int srno) {
		LOG.info("HistoryController | deleteHistory Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("HistoryController | Access Denied in deleteHistory");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}

			this.HistoryService.deleteHistory(srno);
			LOG.info("HistoryService | deleteHistory Exit");
		} catch (DataAccessException e) {
			LOG.error("HistoryController | Exception in deleteHistory - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("HistoryController | Exception in deleteHistory ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(TEXT_DELETE, HttpStatus.OK);
	}

}
