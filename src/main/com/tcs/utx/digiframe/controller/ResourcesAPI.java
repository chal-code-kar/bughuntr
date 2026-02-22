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

import com.tcs.utx.digiframe.model.Resources;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.service.ResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
@Validated
public class ResourcesAPI {

	private static final Logger LOG = LoggerFactory.getLogger(HelpAPI.class);

	public static final String ACCESS_DENIED_RESOURCES = "ResourcesController | Resources Exit";

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	private String TEXT_NOTADMIN = "You are not the bugHuntr admin";

	private String TEXT_DELETE = "Deleted Sucessfully !";

	private String TEXT_VALIDATION = "Validation Failed";

	private String TEXT_BUGHUNTR = "BugHuntr";

	private String TEXT_ADMIN = "Admin";

	private String TEXT_ACCESS_DENIED = "Access Denied";

	
	private static final String ACCESS_DENIED_JSON =  "{\"ACCESS_DENIED\":\"Access Denied.\"}"; 


	public static final String GUEST = "Guest";

	@Autowired
	private ResourcesService resourcesservice;

	@Autowired
	private PermissionHelperService service;

	@Autowired
	private BrandingDetailsService brandingService;

	public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int vulnid,
			int programid) {
		return this.service.isOperationPermissible(module, submodule, Perm_Type, userid, vulnid, programid);
	}

	@RequestMapping(value = "resources", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> getResources() {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		LOG.info("PermissionHelperController | GetResources Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			if (this.service.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("ResourcesAPIController | Access Denied in getResources");
				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			data = this.resourcesservice.getResources();
			LOG.info(ACCESS_DENIED_RESOURCES);
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in getResources - ", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in getResources", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "getchild/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> getChild(@Min(1) @PathVariable int id) {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		LOG.info("PermissionHelperController | GetChild Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			if (this.service.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("ResourcesAPIController | Access Denied in getChild");

				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);

			}

			// IDOR fix: Verify user has admin access or is resource owner before returning child data
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				List<Map<String, Object>> parentResource = this.resourcesservice.getresourcesbyid(id);
				if (parentResource == null || parentResource.isEmpty()) {
					return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
				}
			}

			data = this.resourcesservice.getChild(id);
			LOG.info(ACCESS_DENIED_RESOURCES);
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in getChild - ", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in getChild", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "getresources", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> getTableResources() {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();

		LOG.info("PermissionHelperController | GetTableResources Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			if (this.service.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("ResourcesAPIController | Access Denied in getTableResources");

				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);

			}
			data = this.resourcesservice.getTableResources();
			LOG.info(ACCESS_DENIED_RESOURCES);
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in getTableResources - ", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in getTableResources ", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "getresources/{id}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> getresourcesbyid(@Min(1) @PathVariable int id) {
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		LOG.info("PermissionHelperController | GetResourcesById Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			if (this.service.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("ResourcesAPIController | Access Denied in getresourcesbyid");
				map.put(TEXT_ACCESS_DENIED, ACCESS_DENIED_JSON);
				data.add(map);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			// IDOR fix: Verify resource exists before returning; non-admins get filtered view
			data = this.resourcesservice.getresourcesbyid(id);
			if (data == null || data.isEmpty()) {
				return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
			}
			LOG.info((ACCESS_DENIED_RESOURCES));
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in getresourcesbyid - ", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in getresourcesbyid ", e);
			return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "deleteResources/{id}", method = RequestMethod.DELETE, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleteResources(@Min(1) @PathVariable int id) {
		LOG.info("PermissionHelperController | DeleteResources Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(TEXT_ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in deleteResources");
				return new ResponseEntity<>(TEXT_NOTADMIN, HttpStatus.FORBIDDEN);
			}

			this.resourcesservice.deleteResources(id);
			LOG.info("PermissionHelperController | DeleteResources Exit");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in deleteResources - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in deleteResources ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(TEXT_DELETE, HttpStatus.OK);
	}

	@RequestMapping(value = "addcategory", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> addcategory(@Valid @RequestBody Resources resource) {
		try {
			LOG.info("PermissionHelperController | AddCategory Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(TEXT_ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			if (!this.resourcesservice.validatecategory(resource)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in AddCategory");
				return new ResponseEntity<String>(TEXT_ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			if (!this.resourcesservice.validateGuidelines(resource)) {
				LOG.info("PermissionHelperController | Validation Failed in addcategory");
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			this.resourcesservice.addcategory(resource);
			LOG.info("PermissionHelperController | AddCategory Exit");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in addcategory - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in addcategory ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Category added", HttpStatus.OK);

	}

	@RequestMapping(value = "addcategoryitem", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> addcategoryitem(@Valid @RequestBody Resources resources) {

		LOG.info("PermissionHelperController | AddCategoryItem Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(TEXT_ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.resourcesservice.validatecategoryitem(resources)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in AddCategoryItem");
				return new ResponseEntity<String>(TEXT_ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			if (!this.resourcesservice.validateResources(resources)) {
				LOG.info("PermissionHelperController | Validation Failed in addcategoryitem");
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			this.resourcesservice.addcategoryitem(resources);
			LOG.info("PermissionHelperController | AddCategoryItem Exit");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in addcategoryitem - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in addcategoryitem ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Category Item added", HttpStatus.OK);

	}

	@RequestMapping(value = "updateresources/{id}", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateresources(@Min(1) @PathVariable int id, @Valid @RequestBody Resources editresources) {

		LOG.info("PermissionHelperController | updateresources Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(TEXT_ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in updateresources");
				return new ResponseEntity<>(TEXT_NOTADMIN, HttpStatus.FORBIDDEN);
			}

			// IDOR fix: Verify path variable ID matches request body ID to prevent ID tampering
			if (editresources.getSrno() != 0 && editresources.getSrno() != id) {
				LOG.info("ResourcesAPIController | ID mismatch in updateresources");
				return new ResponseEntity<>("ID mismatch", HttpStatus.BAD_REQUEST);
			}

			if (!this.resourcesservice.validateResources(editresources)) {
				LOG.info("PermissionHelperController | Validation Failed in addcategoryitem");
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			if (!this.resourcesservice.updateresourcesbyid(editresources)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			this.resourcesservice.updateresources(id, editresources);
			LOG.info("PermissionHelperController | updateresources Exit");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessexception in updateresources - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in updateresources ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Resource Updated!", HttpStatus.OK);

	}


}
