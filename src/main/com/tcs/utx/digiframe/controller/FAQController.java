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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import com.tcs.utx.digiframe.model.Faq;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.FAQService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
@Validated
public class FAQController {

	private static final Logger LOG = LoggerFactory.getLogger(HelpAPI.class);

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	@Autowired
	private BrandingDetailsService brandingService;

	@Autowired
	private FAQService FAQService;

	@Autowired
	private PermissionHelperService service;

	public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int vulnid,
			int programid) {
		return this.service.isOperationPermissible(module, submodule, Perm_Type, userid, vulnid, programid);
	}

	private String TEXT_NOTADMIN = "You are not the bugHuntr admin";

	private String TEXT_BUGHUNTR = "BugHuntr";

	private String TEXT_ADMIN = "Admin";

	private String ACCESS_DENIED = "Access Denied";

	private String TEXT_VALIDATION = "Validation Failed";

	public static final String ERROR = "Something Went Wrong.";

	private String TEXT_DELETE = "Deleted Sucessfully !";

	public static final String GUEST = "Guest";


	public static final String SAME_MENU_ERROR = "Something Went Wrong. Help with same Menu Name mey be already present.";

	public static final String ACCESS_DENIED_ADDBOUNTYROLE = "FAQController | Access Denied in FAQ";

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXIT = "FAQController | FAQ Exit";

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXCEPTION = "FAQController | Exception in FAQ - ";

	@RequestMapping(value = "FaqPostQuery", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> FaqPostQuery(@Valid @RequestBody Faq faqquery) {
		try {
			LOG.info("FAQController | FAQQuery Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.FAQService.validateFAQPost(faqquery)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("FAQController | Access Denied in FAQQuery");
				return new ResponseEntity<String>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			this.FAQService.FaqPostQuery(faqquery);
			LOG.info("FAQController | FAQQuery Exit");
		} catch (DataAccessException e) {
			LOG.error("FAQController | Exception in FAQQuery", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("FAQController | Exception in FAQQuery ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("FAQQuery Item added", HttpStatus.OK);

	}

	@RequestMapping(value = "FaqAllQuery", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> FaqAllQuery() {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			int emp_id = BrandingDetailsController.getUser();
			LOG.info("FAQController | FaqAllQuery Begin");
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				map.put(ACCESS_DENIED, "{\"ACCESS_DENIED\":\"Access Denied.\"}");
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			if (this.service.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("FAQController | Access Denied in FaqAllQuery");

				map.put(ACCESS_DENIED, "{\"TEXT_ACCESS_DENIED\":\"Access Denied.\"}");
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			data = this.FAQService.FaqAllQuery();
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			LOG.error("FAQController |Exception in FaqAllQuery ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("FAQController | Exception in FaqAllQuery ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "deleteFAQ/{id}", method = RequestMethod.DELETE,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleteFAQ(@Min(1) @PathVariable int id) {
		try {
			LOG.info("FAQController | deleteFAQ Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("FAQController | Access Denied in deleteFAQ");
				return new ResponseEntity<>(TEXT_NOTADMIN, HttpStatus.FORBIDDEN);
			}

			this.FAQService.deleteFAQ(id);
			LOG.info("PermissionHelperController | deleteFAQ Exit");
		} catch (DataAccessException e) {
			LOG.error("FAQController |Exception in deleteFAQ ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("FAQController | Exception in deleteFAQ ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(TEXT_DELETE, HttpStatus.OK);
	}

	@RequestMapping(value = "updateFAQ/{id}", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateFAQ(@Min(1) @PathVariable int id, @Valid @RequestBody Faq editFaq) {
		try {
			LOG.info("FAQController | updateFAQ Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.FAQService.validateFAQUpdate(editFaq)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("FAQController | Access Denied in updateFAQ");
				return new ResponseEntity<>(TEXT_NOTADMIN, HttpStatus.FORBIDDEN);
			}
			this.FAQService.updateFAQ(id, editFaq);
			LOG.info("PermissionHelperController | updateFAQ Exit");
		} catch (DataAccessException e) {
			LOG.error("FAQController |Exception in updateFAQ ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("FAQController | Exception in updateFAQ ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("FAQ Updated!", HttpStatus.OK);

	}


}
