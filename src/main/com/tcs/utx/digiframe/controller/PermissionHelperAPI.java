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

import com.tcs.utx.digiframe.model.Role;
import com.tcs.utx.digiframe.model.UserRoleDTO;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.util.TSSVStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
public class PermissionHelperAPI {

	private static final Logger LOG = LoggerFactory.getLogger(PermissionHelperAPI.class);

	private String TEXT_BUGHUNTR = "BugHuntr";

	private String TEXT_ADMIN = "Admin";

	private String ACCESS_DENIED = "Access Denied";

	private String TEXT_ERROR = "error";

	public static final String ERROR = "Something Went Wrong.";

	public static final String VALID_EMPLOYEE_ID = "Provide valid Employee Id";

	public static final String ACCESS_DENIED_ADDBOUNTYROLE = "PermissionHelperController | Access Denied in AddBugBountyRole";

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXIT = "PermissionHelperController | AddBugBountyRole Exit";

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXCEPTION = "PermissionHelperController | Exception in AddBugBountyRole - ";

	private String TEXT_NOTADMIN = "You are not the bugHuntr admin";

	private String TEXT_DELETE = "Deleted Sucessfully !";

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	@Autowired
	private PermissionHelperService service;

	@Autowired
	private BrandingDetailsService brandingService;

	public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int vulnid,
			int programid) {
		return this.service.isOperationPermissible(module, submodule, Perm_Type, userid, vulnid, programid);
	}

	@RequestMapping(value = "AccessList", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getTotalAccess() {
		Map<String, Object> retData = new HashMap<>();
		try {
			LOG.info("PermissionHelperController | getTotalAccess Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put(TEXT_ERROR, ACCESS_DENIED);
				LOG.info("PermissionHelperController | Access Denied in getTotalAccess");
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				retData.put(TEXT_ERROR, ACCESS_DENIED);
				LOG.info("PermissionHelperController | Access Denied in getTotalAccess");
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}
			retData = this.service.getTotalAccess();
			LOG.info("PermissionHelperController | getTotalAccess Exit");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in getTotalAccess ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  getTotalAccess ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "roles", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> AddRole(@RequestBody Role role) {
		try {

			LOG.info("PermissionHelperController | AddRole Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in AddRole");
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			this.service.AddRole(role);
			LOG.info("PermissionHelperController | AddRole Exit");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in AddRole - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  AddRole ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Role Added", HttpStatus.OK);

	}

	@RequestMapping(value = "rolesAndPermission", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getRoleAndPermission() {
		Map<String, Object> retData = new HashMap<>();
		try {
			LOG.info("PermissionHelperController | getRoleAndPermission Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put(TEXT_ERROR, ACCESS_DENIED);
				LOG.info("PermissionHelperController | Access Denied in getRoleAndPermission");
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				retData.put(TEXT_ERROR, ACCESS_DENIED);
				LOG.info("PermissionHelperController | Access Denied in getRoleAndPermission");
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}
			retData = this.service.getRoleAndPermission();
			LOG.info("PermissionHelperController | getRoleAndPermission Exit");

		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in getRoleAndPermission - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  getRoleAndPermission ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "rolesAndPermission", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> UpdatePermisions(@RequestBody List<Role> role) {
		try {
			LOG.info("PermissionHelperController | UpdatePermisions Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in UpdatePermisions");
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			this.service.updatePermisions(role);
			LOG.info("PermissionHelperController | UpdatePermisions Exit");

		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in UpdatePermisions - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  UpdatePermisions ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Permission Updated", HttpStatus.OK);

	}

	@RequestMapping(value = "bugBountyRole/{role_bounty_id}", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> AddBugBountyRole(@PathVariable int role_bounty_id) {
		try {
			LOG.info("PermissionHelperController | AddBugBountyRole Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
           
			if (emp_id == role_bounty_id) {
				LOG.info(
						"PermissionHelperController | Validation Failed (Can not provide access to yourself) in AddBugBountyRole");
				return new ResponseEntity<>("You cannot provide access to yourself", HttpStatus.BAD_REQUEST);
			}
			
			
			boolean isBountyAdmin = brandingService.isUserAdmin(emp_id);
			if (isBountyAdmin) {
				if ("TRUE".equals(this.service.bountyUserExists(role_bounty_id, 2))) {
					LOG.info(
							"PermissionHelperController | Validation Failed (User already has Bounty Admin Role) in AddBugBountyRole");
					return new ResponseEntity<>("User already has Bounty Admin Role", HttpStatus.BAD_REQUEST);
				}
			}
			
			
			if (role_bounty_id < 0) {
				LOG.info(
						"PermissionHelperController | Validation Failed (Negative role_bounty_id) in AddBugBountyRole");
				return new ResponseEntity<>(VALID_EMPLOYEE_ID, HttpStatus.BAD_REQUEST);
			}
			if (String.valueOf(role_bounty_id).length() < 4 || String.valueOf(role_bounty_id).length() > 10) {
				LOG.info(
						"PermissionHelperController | Validation Failed (role_bounty_id length validation failed) in AddBugBountyRole");
				return new ResponseEntity<>(VALID_EMPLOYEE_ID, HttpStatus.BAD_REQUEST);
			}
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info(ACCESS_DENIED_ADDBOUNTYROLE);
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			this.service.AddBugBountyRole(role_bounty_id, "Bounty Administrator");
			this.service.AddBugBountyRole(role_bounty_id, "Administrator");
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);

		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessException in AddBugBountyRole - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  AddBugBountyRole ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Bounty Admin Role Added", HttpStatus.OK);

	}

	@RequestMapping(value = "bugBountyAdminRole/{role_bounty_id}", method = RequestMethod.POST ,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> AddBugBountyAdminRole(@PathVariable int role_bounty_id) {
		
		List<Map<String, Object>> data = new ArrayList<>();
		
		try {
			LOG.info("PermissionHelperController | AddBugBountyRole Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			data = this.service.getEmployeeDetail(Integer.toString(role_bounty_id));
			if(data.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			

			if (emp_id == role_bounty_id) {
				LOG.info(
						"PermissionHelperController | Validation Failed (Can not provide access to yourself) in AddBugBountyRole");
				return new ResponseEntity<>("You cannot provide access to yourself", HttpStatus.BAD_REQUEST);
			}
			if (role_bounty_id < 0) {
				LOG.info(
						"PermissionHelperController | Validation Failed (Negative role_bounty_id) in AddBugBountyRole");
				return new ResponseEntity<>(VALID_EMPLOYEE_ID, HttpStatus.BAD_REQUEST);
			}
			if (String.valueOf(role_bounty_id).length() < 4 || String.valueOf(role_bounty_id).length() > 10) {
				LOG.info(
						"PermissionHelperController | Validation Failed (role_bounty_id length validation failed) in AddBugBountyRole");
				return new ResponseEntity<>(VALID_EMPLOYEE_ID, HttpStatus.BAD_REQUEST);
			}
			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info(ACCESS_DENIED_ADDBOUNTYROLE);
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			
			boolean isAdmin = brandingService.isUserAdmin(emp_id);
			if (isAdmin) {
				if (this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", role_bounty_id, 0, 0)) {
					LOG.info(ACCESS_DENIED_ADDBOUNTYROLE);
					return new ResponseEntity<>("User already has Bughuntr Admin Role", HttpStatus.BAD_REQUEST);
				}
			}
			
			
			this.service.AddBugBountyRole(role_bounty_id, "Administrator");
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);

		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | DataAccessException in AddBugBountyAdminRole - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController |  Exception in AddBugBountyAdminRole ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("BugHuntr Admin Role Added", HttpStatus.OK);

	}

	@RequestMapping(value = "AllUsersRole", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<UserRoleDTO>> AllUsersRole() {
		List<UserRoleDTO> data = new ArrayList<>();
		try {
			LOG.info("PermissionHelperController | AllUsersRole Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info(ACCESS_DENIED_ADDBOUNTYROLE);
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

			data = this.service.AllUsersRole();
			
			
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in AllUsersRole - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  AllUsersRole ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);

	}


	@RequestMapping(value = "employee/{employeeid}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> getEmployeeDetail(@PathVariable String employeeid) {
		LOG.info("PermissionHelperController | getEmployeeDetail Begin");
		int emp_id = BrandingDetailsController.getUser();

		List<Map<String, Object>> data = new ArrayList<>();
		try {

		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			LOG.info("PermissionHelperController | Access Denied in getEmployeeDetail");
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
			LOG.info("PermissionHelperController | Access Denied in getEmployeeDetail");
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		if (String.valueOf(employeeid).length() > 2) {
			data = this.service.getEmployeeDetail(employeeid);
			if(data.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
			return new ResponseEntity<>(data, HttpStatus.OK);
		} else {
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in getEmployeeDetail - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  getEmployeeDetail ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "employee", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> getEmployeeData() {
		List<Map<String, Object>> data =  new ArrayList<Map<String, Object>>();
		try {
		LOG.info("PermissionHelperController | GetEmployeeData Begin");
		int emp_id = BrandingDetailsController.getUser();

		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			LOG.info("PermissionHelperController | Access Denied in getEmployeeData");
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
			LOG.info("PermissionHelperController | Access Denied in getEmployeeData");
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		data = this.service.getEmployeeData();

		LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in getEmployeeData - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  getEmployeeData ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "deleterole/{id}", method = RequestMethod.DELETE,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleterole(@PathVariable int id) {
		LOG.info("PermissionHelperController | DeleteRole Begin");
		try {
		int emp_id = BrandingDetailsController.getUser();

		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
		}

		if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
			LOG.info("PermissionHelperController | Access Denied in DeleteRole");
			return new ResponseEntity<String>(TEXT_NOTADMIN, HttpStatus.FORBIDDEN);
		}

		// IDOR fix (3.14): Validate role ID is positive before deletion
		if (id <= 0) {
			return new ResponseEntity<>("Invalid role ID", HttpStatus.BAD_REQUEST);
		}

		this.service.deleterole(id);
		LOG.info("PermissionHelperController | DeleteRole Exit");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in deleterole - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  deleterole ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(TEXT_DELETE, HttpStatus.OK);
	}



	@RequestMapping(value = "analytics", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getData() {
		Map<String, Object> retData = new HashMap<>();
		try {
			LOG.info("PermissionHelperController | GetData Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put(TEXT_ERROR, ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				retData.put(TEXT_ERROR, ACCESS_DENIED);
				LOG.info("PermissionHelperController | Access Denied in getData");
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			retData = this.service.getData();
			LOG.info("PermissionHelperController | GetData End");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in getData - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  getData ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);
	}

 

	@RequestMapping(value = "getUsers", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getUsers() {
		Map<String, Object> retData = new HashMap<>();
		LOG.info("PermissionHelperController | GetUsers Begin");
		try {
		int emp_id = BrandingDetailsController.getUser();

		boolean isGuest = brandingService.isUserGuest();
		if (isGuest) {
			retData.put(TEXT_ERROR, ACCESS_DENIED);
			LOG.info("PermissionHelperController | Access Denied in GetUsers");
			return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
		}

		if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
			retData.put(TEXT_ERROR, ACCESS_DENIED);
			LOG.info("PermissionHelperController | Access Denied in GetUsers");
			return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
		}
		retData.put("Data", this.service.getUsers());
		LOG.info("PermissionHelperController | GetUsers End");
		} catch (DataAccessException e) {
			LOG.error("PermissionHelperController | Exception in getUsers - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("PermissionHelperController | Exception in  getUsers ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);
	}

}
