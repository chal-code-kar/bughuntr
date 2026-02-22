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

import com.tcs.utx.digiframe.model.Helper;
import com.tcs.utx.digiframe.model.help_wordcloud;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.HelpService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
public class HelpAPI {

	private static final Logger LOG = LoggerFactory.getLogger(HelpAPI.class);
	
	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	public static final String GUEST = "Guest";

	public static final String ERROR = "Something Went Wrong.";

	private static final String BUGHUNTR = "BugHuntr";

	private String ADMIN = "Admin";

	private String MESSAGE_BUGHUNTR_ADMIN = "You are not the bugHuntr admin";

	public static final String SAME_MENU_ERROR = "Something Went Wrong. Help with same Menu Name mey be already present.";
	public static final String ACCESS_DENIED = "ACCESS_DENIED";
	@Autowired
	private HelpService helpService;

	@Autowired
	private PermissionHelperService permissionService;

	@Autowired
	private BrandingDetailsService brandingService;

	@RequestMapping(value = "help", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> gethelpdetails() {
		List<Map<String, Object>> content = new ArrayList<>();
		Map<String, Object> retData = new HashMap<>();

		try {
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			int emp_id = BrandingDetailsController.getUser();
			if (!this.permissionService.isAuthorized(emp_id)) {
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			LOG.info("HelpController | gethelpdetails Begin");

			content = this.helpService.gethelpdetails();			
			retData.put("content", content);
			retData.put("isAdmin",
					this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0));
			LOG.info("HelpController | gethelpdetails Exit");

		} catch (DataAccessException e) {
			LOG.error("AdminController | Exception in getMenus ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("AdminController | Exception in  getMenus ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "help", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> addhelp(@RequestBody Helper help) {
		try {
			LOG.info("HelpController | addhelp Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("HelpController | Access Denied in addHelp");
				return new ResponseEntity<>(MESSAGE_BUGHUNTR_ADMIN, HttpStatus.FORBIDDEN);
			}			 

			String retData = this.helpService.validateHelp(help);
			if (retData != null) {
				return new ResponseEntity<>(retData, HttpStatus.BAD_REQUEST);
			}

			this.helpService.addhelp(help);

			LOG.info("HelpController | addHelp Exit");
		} catch (DataAccessException e) {
			LOG.error("HelpController | Exception in addHelp", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("HelpController |Exception in addHelp", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Help Menu Added", HttpStatus.OK);

	}

	@RequestMapping(value = "wordcloud", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> wordcloud() {
		List<Map<String, Object>> content = new ArrayList<>();
		Map<String, Object> retData = new HashMap<>();

		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			LOG.info("HelpController | Wordcloud Begin");
			if (this.permissionService.isOperationPermissible(BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("HelpController | Access Denied in wordCloud");
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}
			content = this.helpService.wordcloud();
			retData.put("content", content);

			LOG.info("HelpController | Wordcloud Exit");
		} catch (DataAccessException e) {
			LOG.error("HelpController | Exception in Wordcloud", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("HelpController | Exception in Wordcloud ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);
	}

	@RequestMapping(value = "deletehelp/{id}", method = RequestMethod.GET,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deletehelp(@PathVariable int id) {
		try {
			LOG.info("HelpController | DeleteHelp Begin");
			int emp_id = BrandingDetailsController.getUser();
			
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			
			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("HelpController | Access Denied in DeleteHelp");
				return new ResponseEntity<>(MESSAGE_BUGHUNTR_ADMIN, HttpStatus.FORBIDDEN);
			}
			
			this.helpService.deleteHelp(id);
			LOG.info("HelpController | DeleteHelp Exit");
		}catch(DataAccessException e) {
			LOG.error("HelpController | Exception in DeleteHelp - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			LOG.error("HelpController |  Exception in DeleteHelp ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Help Deleted !", HttpStatus.OK);
	}

	@RequestMapping(value = "updateHelp", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updatehelp(@RequestBody Helper help) {
		try {
		LOG.info("HelpController | UpdateHelp Begin");
		int emp_id = BrandingDetailsController.getUser();
		
		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
		}
		
		if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
			LOG.info("HelpController | Access Denied in UpdateHelp");
			return new ResponseEntity<>(MESSAGE_BUGHUNTR_ADMIN, HttpStatus.FORBIDDEN);
		}
		
		this.helpService.updateHelp(help);
		LOG.info("HelpController | UpdateHelp Exit");
		}catch(DataAccessException e) {
			LOG.error("HelpController | Exception in UpdateHelp - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			LOG.error("HelpController | Exception in UpdateHelp ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Help Updated!", HttpStatus.OK);

	}

	@RequestMapping(value = "search", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> help_wordcloud(@RequestBody help_wordcloud word) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
		int emp_id = BrandingDetailsController.getUser();
		
		LOG.info("HelpController | Help_wordcloud Begin");
		
		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			map.put("text", ACCESS_DENIED);
			return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
		}
		
		if (this.permissionService.isOperationPermissible(BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
			LOG.info("HelpController | Access Denied in Help_wordcloud");
			map.put("text", ACCESS_DENIED);
			return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);

		}
		this.helpService.help_wordcloud(word);
		LOG.info("HelpController | Help_wordcloud Exit");
		map.put("Success", "Done!");
		}catch(DataAccessException e) {
			LOG.error("HelpController | Exception in Help_wordcloud - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			LOG.error("HelpController | Exception in Help_wordcloud ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(map, HttpStatus.OK);

	}


	
}
