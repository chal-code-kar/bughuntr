package com.tcs.utx.digiframe.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tcs.utx.digiframe.model.Researcher;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.service.ResearcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
public class ResearcherAPI {

	private static final Logger LOG = LoggerFactory.getLogger(ResearcherAPI.class);

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	private String TEXT_EMP_ID = "emp_id";

	private String TEXT_BUGHUNTR = "BugHuntr";

	private String TEXT_ADMIN = "Admin";

	private String ERROR_MSG = "Something went wrong while adding researcher";

	public static final String ERROR = "Something Went Wrong.";

	private String TEXT_ERROR = "error";

	public static final String GUEST = "Guest";

	private String ACCESS_DENIED = "Access Denied";

	@Autowired
	private ResearcherService researcherService;

	@Autowired
	private PermissionHelperService permissionService;

	@Autowired
	private BrandingDetailsService brandingService;

	@RequestMapping(value = "researchers", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> addResearchers(MultipartHttpServletRequest request) {


		try {
			List<Map<String, Object>> data = this.researcherService.getAllReaserchers();
			int emp_id = Integer.valueOf(BrandingDetailsController.getUser());
			Researcher user = new Researcher();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			user = new com.fasterxml.jackson.databind.ObjectMapper().readValue(request.getParameter("data"),
					Researcher.class);
			user.setId(emp_id);
			for (int i = 0; i < data.size(); i++) {
				if ((int) data.get(i).get(TEXT_EMP_ID) == emp_id) {
					return new ResponseEntity<>("User already registered", HttpStatus.BAD_REQUEST);
				}
			}

			String retData = this.researcherService.validateResearchProgramme(user);
			if (retData != null) {
				return new ResponseEntity<>(retData, HttpStatus.BAD_REQUEST);
			}

			if (!this.researcherService.isActionAllowed(emp_id)) {
				LOG.info("ResearcherController | User not Allowed");
				return new ResponseEntity<>("You are not authorized to do this action", HttpStatus.FORBIDDEN);
			}

			if (this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("ResearcherController | This user has role BugHuntr Admin");
				return new ResponseEntity<>("This User has BugHuntr Admin role", HttpStatus.FORBIDDEN);
			}
			this.researcherService.addResearchers(user);

		} catch (JsonMappingException e) {
			LOG.info("ResearcherController | JsonMappingException in addResearchers - ", e);
			return new ResponseEntity<>(ERROR_MSG, HttpStatus.BAD_REQUEST);
		} catch (JsonProcessingException e) {
			LOG.info("ResearcherController | JsonProcessingException in addResearchers - ", e);
			return new ResponseEntity<>(ERROR_MSG, HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			LOG.info("ResearcherController | IOException in addResearchers - ", e);
			return new ResponseEntity<>(ERROR_MSG, HttpStatus.BAD_REQUEST);
		}
		LOG.info("ResearcherController | addResearchers (with image) Exit");
		return new ResponseEntity<>("Researcher Added Successfully", HttpStatus.OK);

	}

	@RequestMapping(value = "researchers", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getAllReaserchers() {
		Map<String, Object> data = new HashMap<String, Object>();

		LOG.info("ResearcherController | getAllReaserchers Begin");
		ArrayList<Researcher> retLst = new ArrayList<Researcher>();
		List<Map<String, Object>> a = new ArrayList<Map<String, Object>>();

		try {

			a = this.researcherService.getAllReaserchers();

			for (Map<String, Object> row : a) {
				Researcher temp = new Researcher();

				temp.setSrno((int) row.get("srno"));
				temp.setAvatar((String) row.get("avatar"));
				temp.setSkills((String) row.get("skills"));
				temp.setBio((String) row.get("emp_bio"));
				temp.setProfile_pic((String) row.get("profile_pic"));
				temp.setTeams((String) row.get("teams"));
				temp.setPoints((int)row.get("points"));
				if (row.get("total_bounty_earned") != null) {
					temp.setTotal_bounty_earned((int) row.get("total_bounty_earned"));
				} else {
					temp.setTotal_bounty_earned((0));
				}
				retLst.add(temp);

			}
			boolean isResearcher = true;
			int emp_id = BrandingDetailsController.getUser();

			if (this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				isResearcher = false;
			} else {
				for (Map<String, Object> temp : a) {
					if ((int) temp.get(TEXT_EMP_ID) == emp_id) {
						isResearcher = false;
					}
				}
			}
			data.put("isResearcher", isResearcher);
			if (this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				data.put("data", a);
			} else {
				data.put("data", retLst);
			}
		} catch (DataAccessException e) {
			LOG.error("DataAccessexception | getAllReaserchers API", e);
			return new ResponseEntity<Map<String, Object>>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("Exception | getAllReaserchers API", e);
			return new ResponseEntity<Map<String, Object>>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOG.info("ResearcherController | getAllReaserchers Exit");
		return new ResponseEntity<>(data, HttpStatus.OK);

	}

	@RequestMapping(value = "researchers/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getResearcherById(@PathVariable int id) {
		Map<String, Object> data = new HashMap<>();
		LOG.info("ResearcherController | getResearcherById Begin");
		try {

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				data.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
			}

			int emp_id = BrandingDetailsController.getUser();
			boolean isEditable = false;
			
	        
	        
			data = this.researcherService.getResearcherById(id).get(0);

			if (emp_id == (int) data.get(TEXT_EMP_ID)) {
				isEditable = true;
			}

			data.put("isEditable", isEditable);

		} catch (DataAccessException e) {
			LOG.error("DataAccessexception | Exception in getResearcherById API - ", e);
			return new ResponseEntity<Map<String, Object>>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("Exception | Exception in getResearcherById API", e);
			return new ResponseEntity<Map<String, Object>>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);

	}

	@RequestMapping(value = "updateresearchers/{id}", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateResearcher(@PathVariable int id, @RequestBody Researcher user) {
		try {
			int emp_id = BrandingDetailsController.getUser();
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			String retData = this.researcherService.validateResearchProgramme(user);
			if (retData != null) {
				return new ResponseEntity<>(retData, HttpStatus.BAD_REQUEST);
			}
			if (emp_id != user.getId()) {
				return new ResponseEntity<>("You can not update any other Researcher Profile", HttpStatus.FORBIDDEN);
			}
			this.researcherService.updateResearchers(user);
		} catch (DataAccessException e) {
			LOG.error("DataAccessexception | Exception in updateResearcher API - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("Exception | Exception in updateResearcher API", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Researcher Details Updated", HttpStatus.OK);

	}

	@RequestMapping(value = "huntHistory", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public List<Map<String, Object>> getAllhuntHistory() {
		List<Map<String, Object>> retData = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				LOG.info("ResearcherAPIController | Access Denied in huntHistory");
				map.put(ACCESS_DENIED, "{\"ACCESS_DENIED\":\"Access Denied.\"}");
				retData.add(map);
				return retData;
			}

			LOG.info("ResearcherController | getAllhuntHistory Begin");
			if (this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("ResearcherAPIController | Access Denied in huntHistory");

				map.put(ACCESS_DENIED, "{\"ACCESS_DENIED\":\"Access Denied.\"}");
				retData.add(map);
				return retData;
			}

			retData = this.researcherService.getAllhuntHistory();
			LOG.info("ResearcherController | getAllhuntHistory Exit");
		} catch (DataAccessException e) {
			LOG.error("DataAccessexception | Exception in updateResearcher API - ", e);
			return retData;
		} catch (Exception e) {
			LOG.error("Exception | Exception in updateResearcher API", e);
			return retData;
		}
		return retData;
	}

	@RequestMapping(value = "avatar/{avatarName}", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	public String IsAvatarAvailable(@PathVariable String avatarName) {
		LOG.info("ResearcherController | IsAvatarAvailable Begin");
		boolean result = false;
		try {

			result = this.researcherService.isAvatarAvailable(avatarName);

			LOG.info("ResearcherController | IsAvatarAvailable Exit");
		} catch (DataAccessException e) {
			LOG.error("DataAccessexception | Exception in IsAvatarAvailable API - ", e);
			return result+"";
		} catch (Exception e) {
			LOG.error("Exception | Exception in IsAvatarAvailable API", e);
			return result+"";
		}
		return "true";
	}

	@RequestMapping(value = "addDetail/{id}", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> addDetails(@PathVariable int id) {
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			this.researcherService.addDetails(id);
		} catch (DataAccessException e) {
			LOG.error("ResearcherController | DataAccessException in addDetails API - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ResearcherController | Exception in addDetails API", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("User Added!", HttpStatus.OK);

	}

	@RequestMapping(value = "isallowed/{id}", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	public String isActionallowed(@PathVariable int id) {
		boolean result = false;
		try {
			LOG.info("ResearcherController | isActionallowed Begin");
			result = this.researcherService.isActionAllowed(id);
			LOG.info("ResearcherController | isActionallowed Exit");
		} catch (DataAccessException e) {
			LOG.error("ResearcherController | DataAccessException in isallowed API - ", e);
			return result+"";
		} catch (Exception e) {
			LOG.error("ResearcherController | Exception in addDetails API", e);
			return result+"";
		}
		return result+"";
	}


	@RequestMapping(value = "isResearcher", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	public String isResearcher() {
		LOG.info("ResearcherController | isResearcher Begin");
		boolean result = false;
		try {
			int emp_id = BrandingDetailsController.getUser();
			result = this.researcherService.isResearcher(emp_id);
			LOG.info("ResearcherController | isResearcher Exit");
		} catch (DataAccessException e) {
			LOG.error("DataAccessexception | Exception in addDetails API - ", e);
			return result+"";
		} catch (Exception e) {
			LOG.error("Exception | Exception in addDetails API", e);
			return result+"";
		}
		return result+"";
	}

}