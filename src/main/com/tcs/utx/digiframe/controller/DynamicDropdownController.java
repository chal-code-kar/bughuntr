package com.tcs.utx.digiframe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.DynamicDropdown;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.ManageOptionsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.util.TSSVStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/BugHuntr/api/v1/")
@Validated
public class DynamicDropdownController {

	private static final Logger LOG = LoggerFactory.getLogger(HelpAPI.class);

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	private static final String TEXT_NOTADMIN = "You are not the bugHuntr admin";

	private String TEXT_ERROR = "Something went wrong!";

	private String TEXT_BUGHUNTR = "BugHuntr";

	private String TEXT_ADMIN = "Admin";

	public static final String ACCESS_DENIED = "Access Denied";

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXIT = "PermissionHelperController | AddBugBountyRole Exit";

	public static final String GUEST = "Guest";
	
	public static final String VALIDATION_ERROR = "Validation Failed";

	@Autowired
	private PermissionHelperService permissionService;

	@Autowired
	private ManageOptionsService optionService;

	@Autowired
	private BrandingDetailsService brandingService;

	@Autowired
	private PermissionHelperService service;

	public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int vulnid,
			int programid) {
		return this.service.isOperationPermissible(module, submodule, Perm_Type, userid, vulnid, programid);
	}

	@RequestMapping(value = "getAllOptions", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getAllOptions(@RequestBody @Size(max = 100) String type) {
		Map<String, Object> retData = new HashMap<>();
		int emp_id = BrandingDetailsController.getUser();

		LOG.info("getAllOptions | getAllOptions Begin");
		try {
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put("text", ACCESS_DENIED);				
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}
			
			if (this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("Dynamic Dropdown Controller | Access Denied in getAllOptions");
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			if ("Vulnerability".equals(type)) {
				retData.put("vulncat", this.optionService.getOptions("Vulnerability Category"));
				retData.put("ASVS", this.optionService.getOptions("ASVS"));
				retData.put("CIA", this.optionService.getOptions("CIA"));
			} else {
				if (type != null) {
					retData.put("content", this.optionService.getOptions(type));
				} else {
					retData.put("error", TEXT_ERROR);
					return new ResponseEntity<>(retData, HttpStatus.BAD_REQUEST);
				}
			}
		} catch (UserDefinedException e) {
			LOG.info("getAllOptions | getAllOptions | Exception ");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		LOG.info("getAllOptions | getAllOptions Exit");
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "GetAllDropdown", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> GetAllDropdown(@RequestParam(value="filter", required=false) String filter,
			HttpServletResponse response) {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
	try {
		LOG.info("DropdownController | GetAllDropdown Begin");
		int emp_id = BrandingDetailsController.getUser();
		
		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			map.put(ACCESS_DENIED, "{\"TEXT_ERROR\":\"Forbidden.\"}");
			data.add(map);				
			return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
		}
		
		if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
			LOG.info("DropdownController | Access Denied in Add Dropdown");
		
			map.put(TEXT_NOTADMIN, "{\"TEXT_NOTADMIN\":\"Forbidden.\"}");
			data.add(map);
			return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
		}

		data = this.optionService.GetAllDropdown();
				
	}catch(DataAccessException e) {
		LOG.info("DropdownController | Exception in GetAllDropdown");
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

	}catch(Exception e) {
		LOG.info("DropdownController |  Exception in GetAllDropdown");
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

	}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "GetDropdown", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> GetAllDropdownURL() {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
	try {
		LOG.info("DropdownController | GetAllDropdown Begin");
		int emp_id = BrandingDetailsController.getUser();
		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {		
			map.put("text", ACCESS_DENIED);
			data.add(map);
			return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
		}
		if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
			LOG.info("DropdownController | Access Denied in GetDropdown");
			return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
		}

		data = this.optionService.GetAllDropdown();
		LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);

		
	}catch(DataAccessException e) {
		LOG.info("DropdownController | Exception in GetAllDropdown");
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

	}catch(Exception e) {
		LOG.info("DropdownController |  Exception in GetAllDropdown");
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	return new ResponseEntity<>(data, HttpStatus.OK);
	}


	@RequestMapping(value = "AddDropdown", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> AddDropdown(@Valid @RequestBody DynamicDropdown Adddropdown) {
		LOG.debug("DynamicDropdownController | addOption Begin");
		

		try {

			int emp_id = BrandingDetailsController.getUser();
			
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {		
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("DropdownController | Access Denied in Add Dropdown");
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			ResponseEntity<String> validationResponse = validateDynamicDropdownDTO(Adddropdown);

			if (validationResponse != null) {
				LOG.error("DynamicDropdownController | addOption validation failed");
				return validationResponse;
			}
			Adddropdown.setLookupvalue(Adddropdown.getLookupvalue().trim());
			Adddropdown.setLookupdescription(Adddropdown.getLookupdescription().trim());

			Adddropdown.setCreatedby(Integer.toString(emp_id));
			Adddropdown.setActive(true);

			boolean alreadyExists = optionService.checkExistsInOptions(Adddropdown);

			if (alreadyExists) {
				
				return new ResponseEntity<>("Option Alredy Exist ", HttpStatus.BAD_REQUEST);
			} else {
				int retVal = 0;
				retVal = this.optionService.AddDropdown(Adddropdown, emp_id);

				if (retVal == 0) {
					return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception e) {
			LOG.error("DynamicDropdownController | addOption UserDefinedException - ");
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOG.debug("DynamicDropdownController |Add Dropdown Exit");
		return new ResponseEntity<>("Option Added Successfully", HttpStatus.OK);

	}

	@RequestMapping(value = "deleteDropdown/{id}", method = RequestMethod.DELETE,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleteDropdown(@Min(1) @PathVariable int id) {
		LOG.info("DropdownController | deleteDropdown Begin");
		try {
		int emp_id = BrandingDetailsController.getUser();
		
		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {		
			return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
		}
		
		if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
			LOG.info("DropdownController | deleteDropdown Begin");
			return new ResponseEntity<String>(TEXT_NOTADMIN, HttpStatus.BAD_REQUEST);
		}
		this.optionService.deleteDropdown(id);
		LOG.info("DropdownController | DeleteRole Exit");
		}catch(DataAccessException e) {
			LOG.error("DropdownController | Eception in DeleteRole ");
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);

		}catch(Exception e) {
			LOG.error("DropdownController | Exception in DeleteRole");
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<String>("Deleted Sucessfully !", HttpStatus.OK);
	}

	@RequestMapping(value = "updateDropdown/{id}", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateDropdown(@Min(1) @PathVariable int id, @Valid @RequestBody DynamicDropdown updateDropdown) {
		
		LOG.info("DropdownController | updateresources Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();
			
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {		
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("DropdownController | Access Denied in updateDropdown");
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			updateDropdown.setLookupvalue(updateDropdown.getLookupvalue().trim());
			updateDropdown.setLookupdescription(updateDropdown.getLookupdescription().trim());
			boolean alreadyExists = optionService.checkExistsInOptions(updateDropdown);

			if (alreadyExists) {
				
				return new ResponseEntity<>("Option Already Exist", HttpStatus.BAD_REQUEST);
			} else {
				int retVal = 0;
				retVal = this.optionService.updateDropdown(id, updateDropdown, emp_id);

				if (retVal == 0) {
					return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.BAD_REQUEST);
				}
			}
		} 
		catch (UserDefinedException e) {
			LOG.error("DynamicDropdownController | update Option UserDefinedException - ");
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			LOG.error("DynamicDropdownController | update Option Exception - ");
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		LOG.debug("DynamicDropdownController |Update Dropdown Exit");
		return new ResponseEntity<>("Option Updated Successuflly", HttpStatus.OK);

	}


	private ResponseEntity<String> validateDynamicDropdownDTO(DynamicDropdown dynamicDropdownDTO) {

		ResponseEntity<String> badRequest = new ResponseEntity<>(VALIDATION_ERROR, HttpStatus.BAD_REQUEST);

		if (StringUtils.isBlank(dynamicDropdownDTO.getLookupvalue())
				|| dynamicDropdownDTO.getLookupvalue().trim().length() > 150) {
			return badRequest;
		}

		if (dynamicDropdownDTO.getLookupdescription().trim().length() > 260
				|| dynamicDropdownDTO.getLookupdescription().trim().length() < 1 || !TSSVStringUtil
						.matchPattern(dynamicDropdownDTO.getLookupdescription(), TSSVStringUtil.PATTERN_FOR_NAMES)) {
			return badRequest;
		}

		if (dynamicDropdownDTO.getLookupgroup().trim().length() == 0 || !TSSVStringUtil
				.matchPattern(dynamicDropdownDTO.getLookupgroup().trim(), TSSVStringUtil.PATTERN_FOR_NAMES)) {
			return badRequest;
		}

		if (dynamicDropdownDTO.getLookupvalue().trim().length() == 0) {
			return badRequest;
		}

		return null;
	}

}
