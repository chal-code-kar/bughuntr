package com.tcs.utx.digiframe.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.utx.digiframe.model.Employee;
import com.tcs.utx.digiframe.model.joinedEmploye;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.service.ProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
public class ProgramAPI {

	private String TEXT_ACCESS_DENIED = "Access Denied";
	private String TEXT_BUGHUNTR = "BugHuntr";
	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	private static final Logger LOG = LoggerFactory.getLogger(ProgramAPI.class);

	private String TEXT_BB_PROGRAM = "BB Program";

	private String TEXT_NEW_PROGRAM = "New Program";

	private String TEXT_PRIVATE = "Private";

	private String TEXT_PTYPE = "ptype";

	private String TEXT_VISITOR = "Visitor";

	private String TEXT_PROJECT_OWNER = "bproject_ownemp_id";

	private String TEXT_VALID_FAILED = " Date Validation Failed";

	private String DUPLICATE_PROGRAM = "Programe with same name already exists";

	public static final String ERROR = "Something Went Wrong.";

	private String TEXT_ERROR = "error";

	private String JOINED = "Joined";

	private String PROTECTED = "Protected";

	private String BUGHUNTR = "BugHuntr";

	private String ADMIN = "Admin";

	private String SUSPEND = "Suspend";

	private String EXPEL = "Expel";
	
	private String APM_ID = "apm_id";
	
	private String BUGPROJECT_OWNEMP_NAME = "bugproject_ownemp_name";

	public static final String GUEST = "Guest";

	public static final String ACCESS_DENIED = "ACCESS_DENIED";

	private String ACCESSDENIED_JOINPROJECT = "ProgramAPI | Access Denied in joinProject";

	@Autowired
	private ProgramService programService;

	@Autowired
	private PermissionHelperService permissionService;

	@Autowired
	private BrandingDetailsService brandingService;

	@RequestMapping(value = "programs", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> createProject(@RequestBody Employee detail) {
		try {
			LOG.info("ProgramAPI | createProject Begin");
			int emp_id = BrandingDetailsController.getUser();
			detail.setOwner_empid(emp_id);

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_NEW_PROGRAM, "Add", emp_id, 0,
					0)) {
				LOG.info("ProgramAPI | Access Denied in createProject");
				return new ResponseEntity<>("You are not Authorized to create Project", HttpStatus.FORBIDDEN);
			}

			String retData = this.programService.validateCreateProgramme(detail);
			if (retData != null) {
				return new ResponseEntity<>(retData, HttpStatus.BAD_REQUEST);
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String start_date = dateFormat.format(detail.getStartdate());
			String end_date = dateFormat.format(detail.getEnddate());
			String today = dateFormat.format(new Date());

			if (detail.getStartdate() == null) {
				if (!today.equals(start_date)) {
					LOG.info("ProgramAPI | Start Date Validation Failed in createProgram");
					return new ResponseEntity<>(TEXT_VALID_FAILED, HttpStatus.BAD_REQUEST);
				}
			}

			if (detail.getEnddate() == null) {
				if (!today.equals(end_date)) {
					LOG.info("ProgramAPI | End Date Validation Failed in createProgram");
					return new ResponseEntity<>(TEXT_VALID_FAILED, HttpStatus.BAD_REQUEST);
				}
			}

			String returnMsg = this.programService.createProject(detail);
			if (returnMsg != null && returnMsg.contains("already exists")) {
				LOG.info("ProgramAPI | Program name already exists in updateProgram");
				return new ResponseEntity<>(DUPLICATE_PROGRAM, HttpStatus.BAD_REQUEST);
			}
			LOG.info("ProgramAPI | createProject Exit");
		} catch (DataAccessException e) {
			e.printStackTrace();
			LOG.error("ProgramAPI | Exception in createProject - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in createProject ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("Project Created Successfully", HttpStatus.OK);

	}

	@RequestMapping(value = "joinProgram", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> joinProgram(@RequestBody joinedEmploye data) {
		try {
			LOG.info("ProgramAPI | joinProject Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			data.setReseacher_id(emp_id);
			Map<String, Object> tempData = this.programService.getProjectById(data.getProject_id(), emp_id).get(0);
			String old_status = this.programService.getJoinProgramStatus(data.getProject_id(), emp_id);

			if ("UnJoin".equals(data.getStatus()) && JOINED.equals(old_status)) {
				this.programService.updateJoinStatus(data, "UnJoin");
				return new ResponseEntity<>("You have UnJoined Program Successfully ", HttpStatus.OK);
			}

			if (old_status != null) {
				LOG.info("ProgramAPI | You cannot join this Program");
				return new ResponseEntity<>("You cannot join this Program", HttpStatus.BAD_REQUEST);
			}

			if (PROTECTED.equals((String) tempData.get(TEXT_PTYPE))) {
				if (this.programService.ifprogramjoinRequested(emp_id, data.getProject_id())) {
					LOG.info("ProgramAPI | You are not invited for this Program");
					return new ResponseEntity<>("You are not invited for this Program", HttpStatus.BAD_REQUEST);
				}
			}

			if (this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info(ACCESSDENIED_JOINPROJECT);
				return new ResponseEntity<>("BugHuntr Admin can not join this program", HttpStatus.FORBIDDEN);
			}
			if (this.permissionService.isOperationPermissible(TEXT_VISITOR, TEXT_VISITOR, "View", emp_id, 0, 0)) {
				LOG.info(ACCESSDENIED_JOINPROJECT);
				return new ResponseEntity<>("Visitor can not join program", HttpStatus.FORBIDDEN);
			}
			if ((int) tempData.get(TEXT_PROJECT_OWNER) == emp_id) {
				LOG.info("ProgramAPI | You are owner of this Project in joinProject");
				return new ResponseEntity<>("Since you are the owner of this Project. You can not join the program",
						HttpStatus.BAD_REQUEST);
			}
			this.programService.joinProject(data, (String) tempData.get(TEXT_PTYPE));
			LOG.info("ProgramAPI | joinProject Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in joinProgram - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in joinProgram ", e);
			e.printStackTrace();
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("You have joined Program Successfully", HttpStatus.OK);

	}

	@RequestMapping(value = "updatejoinProgram", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateJoinProgram(@RequestBody joinedEmploye data) {
		try {

			LOG.info("ProgramAPI | updateJoinProgram Begin");
			int projectid = data.getProject_id();
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			String old_status;
			Map<String, Object> tempData = this.programService.getProjectById(projectid, emp_id).get(0);
			if ((int) tempData.get(TEXT_PROJECT_OWNER) != emp_id) {
				return new ResponseEntity<>("You are not authorised to perform this action", HttpStatus.FORBIDDEN);
			}

			if (this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", data.getReseacher_id(), 0, 0)) {
				LOG.info(ACCESSDENIED_JOINPROJECT);
				return new ResponseEntity<>("BugHuntr Admin can not join this program", HttpStatus.FORBIDDEN);
			}
			if (this.permissionService.isOperationPermissible(TEXT_VISITOR, TEXT_VISITOR, "View",
					data.getReseacher_id(), 0, 0)) {
				LOG.info(ACCESSDENIED_JOINPROJECT);
				return new ResponseEntity<>("Visitor can not join program", HttpStatus.FORBIDDEN);
			}
			if ((int) tempData.get(TEXT_PROJECT_OWNER) == data.getReseacher_id()) {
				LOG.info("ProgramAPI | You are owner of this Project in joinProject");
				return new ResponseEntity<>("Since you are the owner of this Project. You can not join the program",
						HttpStatus.BAD_REQUEST);
			}
			old_status = this.programService.getJoinProgramStatus(projectid, data.getReseacher_id());

			if (old_status == null) {
				LOG.info("ProgramAPI | Old Status unvailable");
				return new ResponseEntity<>("Researcher don't have any active statuson this Program",
						HttpStatus.BAD_REQUEST);
			}

			if (PROTECTED.equals((String) tempData.get(TEXT_PTYPE))) {
				if ("Requested".equals(old_status)) {
					if ("Accept".equals(data.getStatus())) {
						this.programService.updateJoinStatus(data, JOINED);
						return new ResponseEntity<>("Researcher join request accepted successfully", HttpStatus.OK);
					} else if ("Decline".equals(data.getStatus())) {
						this.programService.updateJoinStatus(data, "Declined");
						return new ResponseEntity<>("Researcher join request declined successfully", HttpStatus.OK);
					} else {
						return new ResponseEntity<>("Else", HttpStatus.OK);
					}
				}
			}
			if (JOINED.equals(old_status)) {
				Date date = new Date();
				if (SUSPEND.equals(data.getStatus())) {
					if (data.getSuspendTill() == null || data.getSuspendTill().before(date)) {
						return new ResponseEntity<>("Please enter valid Date of Suspension", HttpStatus.BAD_REQUEST);
					}
					this.programService.updateJoinStatus(data, SUSPEND);
					return new ResponseEntity<>("Researcher suspended from this Project", HttpStatus.OK);
				}
				if (EXPEL.equals(data.getStatus())) {
					this.programService.updateJoinStatus(data, EXPEL);
					return new ResponseEntity<>("Researcher expeled form this Project", HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Join status does not match", HttpStatus.BAD_REQUEST);
				}
			}
			if (SUSPEND.equals(old_status)) {
				if ("Revoke".equals(data.getStatus())) {
					this.programService.updateJoinStatus(data, JOINED);
					return new ResponseEntity<>("Suspension Revoked form this Project", HttpStatus.OK);
				}
			}

			LOG.info("ProgramAPI | joinProject Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in updateJoinProgram - ", e);
			e.printStackTrace();
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in updateJoinProgram ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(ERROR, HttpStatus.BAD_REQUEST);

	}

	@RequestMapping(value = "programs", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getAllProject(@RequestParam(value="prog", required=false) String prog,
			@RequestParam(value="filename", required=false) String filename) {
		List<Map<String, Object>> TotalretData = new ArrayList<Map<String, Object>>();
		Map<String, Object> retMap = new HashMap<>();
			

		
		try {
			LOG.info("ProgramAPI | getAllProject Begin");
			int emp_id = BrandingDetailsController.getUser();
		
			
			boolean canCreate = true;
			List<Map<String, Object>> programs = this.programService.getAllProject(emp_id);
			List<Map<String, Object>> myprograms = this.programService.getMyProject(emp_id);
			List<Map<String, Object>> joinedprograms = this.programService.getJoinedProjects(emp_id);
			if (!this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_NEW_PROGRAM, "Add", emp_id, 0,
					0)) {
				canCreate = false;
			}
			
			if(prog!=null) {
				return ResponseEntity.status(HttpStatus.FOUND)
						.header("Location", prog)
						.build();				
			}

			for (Map<String, Object> temp : programs) {
				if (TEXT_PRIVATE.equals((String) temp.get(TEXT_PTYPE))) {
					if (this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_PRIVATE, "View", emp_id, 0,
							(int) temp.get("srno"))) {
						TotalretData.add(temp);
					}
				} else {
					TotalretData.add(temp);
				}
			}
			

		
			retMap.put("bbAdmin", this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0));
			retMap.put("canCreate", canCreate);
			retMap.put("programs", TotalretData);
			
			if(filename!=null) {
				List<String> cont = new ArrayList<>();
				File f = new File(System.getProperty("user.dir")+filename);
				cont = Files.readAllLines(f.toPath());
				retMap.put("filename",cont);
				return new ResponseEntity<>(retMap, HttpStatus.OK);
			}

			retMap.put("myprograms", myprograms);
			retMap.put("joinedprograms", joinedprograms);
			LOG.info("ProgramAPI | getAllProject Exit");
			

		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in getAllProject - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in getAllProject ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retMap, HttpStatus.OK);

	}

	@RequestMapping(value = "WeeklyPrograms", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getAllProject2(@RequestParam(required=false) String id) {
		List<Map<String, Object>> TotalretData = new ArrayList<Map<String, Object>>();
		Map<String, Object> retMap = new HashMap<>();
		try {
			LOG.info("ProgramAPI | getAllProject2 Begin");
			int emp_id = BrandingDetailsController.getUser();
			boolean canCreate = true;
			
			if(id!=null) {
				List<String> content = new ArrayList<>();
				File f = new File(System.getProperty("user.dir")+id);
				content = Files.readAllLines(f.toPath());
				retMap.put("data",content);
				return new ResponseEntity<>(retMap, HttpStatus.OK);
			}


			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retMap.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retMap, HttpStatus.FORBIDDEN);
			}

			List<Map<String, Object>> data = this.programService.getAllProject2();
			if (this.permissionService.isOperationPermissible(TEXT_BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("Dynamic Dropdown Controller | Access Denied in getAllOptions");
				retMap.put("text", TEXT_ACCESS_DENIED);
				return new ResponseEntity<>(retMap, HttpStatus.FORBIDDEN);
			}
			if (!this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_NEW_PROGRAM, "Add", emp_id, 0,
					0)) {
				canCreate = false;
			}

			for (Map<String, Object> temp : data) {
				if (TEXT_PRIVATE.equals((String) temp.get(TEXT_PTYPE))) {
					if (this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_PRIVATE, "View", emp_id, 0,
							(int) temp.get("srno"))) {
						TotalretData.add(temp);
					}
				} else {
					TotalretData.add(temp);
				}
			}
			retMap.put("canCreate", canCreate);
			retMap.put("programs", TotalretData);
			LOG.info("ProgramAPI | getAllProject2 Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in WeeklyPrograms - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in WeeklyPrograms ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retMap, HttpStatus.OK);

	}

	@RequestMapping(value = "/programs/{id}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getProjectById(@PathVariable int id) {
		Map<String, Object> error = new HashMap<>();
		Map<String, Object> tempData = new HashMap<>();
		try {
			LOG.info("ProgramAPI | getProjectById Begin");

			int emp_id = BrandingDetailsController.getUser();
			tempData = this.programService.getProjectById(id, emp_id).get(0);

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				tempData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(tempData, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, "Announcements", "View", emp_id, 0,
					id)) {
				if ((int) tempData.get(TEXT_PROJECT_OWNER) != emp_id) {
					tempData.replace("Announcement", null);
					tempData.replace("jobpricing", null);
					tempData.replace("access", null);
					tempData.replace("resources", null);
				}
			}

			if (TEXT_PRIVATE.equals((String) tempData.get(TEXT_PTYPE))) {
				if (!this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_PRIVATE, "View", emp_id, 0,
						id)) {
					LOG.info("ProgramAPI | Access Denied in getProjectById");
					error.put(TEXT_ERROR, TEXT_ACCESS_DENIED);
					return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
				}
			}

			boolean iseditable = true;
			boolean bbAdmin = false;
			boolean isVisitor = false;
			boolean isExpel = false;
			boolean isSuspend = false;
			if ((int) tempData.get(TEXT_PROJECT_OWNER) != emp_id) {
				iseditable = false;
			}
			if (this.permissionService.isOperationPermissible(TEXT_VISITOR, TEXT_VISITOR, "View", emp_id, 0, 0)) {
				isVisitor = true;
			}
			if (this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				bbAdmin = true;
			}

			String old_status = this.programService.getJoinProgramStatus(id, emp_id);
			if (EXPEL.equals(old_status)) {
				isExpel = true;
			} else if (SUSPEND.equals(old_status)) {
				isSuspend = true;
			} else {
				;
			}
			
			Object apm_id = tempData.get(APM_ID);
			Object apm_bugproject_ownemp_nameid = tempData.get(BUGPROJECT_OWNEMP_NAME);
			Object bproject_ownemp_id = tempData.get(TEXT_PROJECT_OWNER);
			Object unit = tempData.get("unit");
			
			tempData.replace(APM_ID, null);
			tempData.replace(BUGPROJECT_OWNEMP_NAME, null);
			tempData.replace(TEXT_PROJECT_OWNER, null);
			tempData.replace("unit", null);
			
			if (bbAdmin || iseditable) {
				tempData.replace(APM_ID, apm_id);
				tempData.replace(BUGPROJECT_OWNEMP_NAME, apm_bugproject_ownemp_nameid);
				tempData.replace(TEXT_PROJECT_OWNER, bproject_ownemp_id);
				tempData.replace("unit", unit);
			}
			
			
			
			tempData.put("iseditable", iseditable);
			tempData.put("bbAdmin", bbAdmin);
			tempData.put("isVisitor", isVisitor);
			tempData.put("isExpel", isExpel);
			tempData.put("isSuspend", isSuspend);

			List<Map<String, Object>> usersRequestedJoin = new ArrayList<Map<String, Object>>();
			if (iseditable == true) {
				tempData.put("users", this.programService.usersRequestedJoin(id));
			} else {
				tempData.put("users", usersRequestedJoin);
			}

			LOG.info("ProgramAPI | getProjectById Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in getProjectById - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in getProjectById ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(tempData, HttpStatus.OK);
	}

	@RequestMapping(value = "updateprograms/{id}", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateProgram(@PathVariable int id, @RequestBody Employee detail) {
		try {
			detail.setSrno(id);
			LOG.info("ProgramAPI | updateProgram Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			detail.setOwner_empid(emp_id);
			Map<String, Object> tempData = this.programService.getProjectById(detail.getSrno(), emp_id).get(0);
			String retData = this.programService.validateCreateProgramme(detail);
			if (retData != null) {
				return new ResponseEntity<>(retData, HttpStatus.BAD_REQUEST);
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String start_date = dateFormat.format(detail.getStartdate());
			String end_date = dateFormat.format(detail.getEnddate());
			String today = dateFormat.format(new Date());

			if (!start_date.equals(((Date) tempData.get("bprojectstart_date")).toString())) {
				if (detail.getStartdate() == null) {
					if (!today.equals(start_date)) {
						LOG.info("ProgramAPI | Start Date Validation Failed in updateProgram");
						return new ResponseEntity<>(TEXT_VALID_FAILED, HttpStatus.BAD_REQUEST);
					}
				}
			}

			if (!end_date.equals(((Date) tempData.get("bprojectend_date")).toString())) {
				if (detail.getEnddate() == null) {
					if (!today.equals(end_date)) {
						LOG.info("ProgramAPI | End Date Validation Failed in updateProgram");
						return new ResponseEntity<>(TEXT_VALID_FAILED, HttpStatus.BAD_REQUEST);
					}
				}
			}

			if ((int) tempData.get(TEXT_PROJECT_OWNER) != emp_id) {
				LOG.info("ProgramAPI | Access Denied in updateProgram");
				return new ResponseEntity<>("You are not authorized to update this project", HttpStatus.FORBIDDEN);
			}
			
			int returncCnt = this.programService.updateProgram(detail, emp_id);
			
			LOG.info("ProgramAPI | updateProgram Exit");
			 if(returncCnt > 0) {
				 return new ResponseEntity<>("Program Updated", HttpStatus.OK);
			 }else {
				 return new ResponseEntity<>("Unable to update expired program", HttpStatus.BAD_REQUEST);
			 }
	
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in updateProgram - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in updateProgram ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@RequestMapping(value = "ProgramsStatistics/{projectid}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> ResearchersJoined(@PathVariable int projectid, @RequestParam(value="url", required=false) String url) {
		Map<String, Object> retData = new HashMap<>();
		
		if(url!=null) {
			return ResponseEntity.status(HttpStatus.FOUND)
					.header("Location", url)
					.build();
		}

		try {
			
			
		LOG.info("ProgramAPI | ResearchersJoined Begin");
		int emp_id = BrandingDetailsController.getUser();
		LOG.info("ProgramAPI | ResearchersJoined Begin");
		
		
		retData = this.programService.ResearchersJoined(projectid, emp_id);
		
		LOG.info("ProgramAPI | ResearchersJoined Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in ResearchersJoined - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in ResearchersJoined ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "dashboards", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getdashborardcounts() {
		Map<String, Object> retData = new HashMap<>();
		try {
			int emp_id = BrandingDetailsController.getUser();
			
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			LOG.info("ProgramAPI | getdashborardcounts Begin");
			if (this.permissionService.isOperationPermissible(BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("ProgramAPIController | Access Denied in dashboards");
				retData.put(ACCESS_DENIED, TEXT_ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);

			}
			retData = this.programService.getdashborardcounts();
			retData.put("info", new Throwable("Stack trace marker"));

			LOG.info("ProgramAPI | getdashborardcounts Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in getdashborardcounts - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in getdashborardcounts ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "ProgramsStatistics", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> ProgramDetailStatistics() {
		Map<String, Object> retData = new HashMap<>();
		LOG.info("ProgramAPI | ProgramDetailStatistics Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}

			retData = this.programService.ProgrammeDeatilstatics(emp_id);
			retData.put("info", new Throwable("Stack trace marker"));

			LOG.info("ProgramAPI | ProgramDetailStatistics Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in ProgramDetailStatistics - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in ProgramDetailStatistics ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);

	}

	@RequestMapping(value = "refreshJobs", method = RequestMethod.GET,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> DeleteJobs() {
		LOG.info("ProgramAPI | DeleteJobs Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_NEW_PROGRAM, "Add", emp_id, 0,
					0)) {
				LOG.info("ProgramAPI | Access Denied in DeleteJobs");
				return new ResponseEntity<>("You are not Program Admin!", HttpStatus.FORBIDDEN);
			}
			this.programService.deleteJobs();
			
			LOG.info("ProgramAPI | DeleteJobs Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in DeleteJobs - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in DeleteJobs ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Deleted!", HttpStatus.OK);

	}
	
	@RequestMapping(value = "dAnnoucement/{id}", method = RequestMethod.GET,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleteAnnouncements(@PathVariable int id) {
		LOG.info("ProgramAPI | deleteAnnoucements Begin");
		try {
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(TEXT_BB_PROGRAM, TEXT_NEW_PROGRAM, "Add", emp_id, 0,
					0)) {
				LOG.info("ProgramAPI | Access Denied in deleteAnnoucements");
				return new ResponseEntity<>("You are not Program Admin!", HttpStatus.FORBIDDEN);
			}
			Map<String, Object> tempData = new HashMap<>();
			tempData = this.programService.getData(id).get(0);

			if ((int) tempData.get(TEXT_PROJECT_OWNER) != emp_id) {
				return new ResponseEntity<>("You are not authorised to perform this action", HttpStatus.FORBIDDEN);
			}
			this.programService.deleteAnnouncements(id);
			LOG.info("ProgramAPI | deleteAnnoucements Exit");
		} catch (DataAccessException e) {
			LOG.error("ProgramAPI | Exception in deleteAnnouncements - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("ProgramAPI | Exception in deleteAnnouncements ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Announcement Deleted!", HttpStatus.OK);
	}


	@Scheduled(cron = "0 0 */8 * * *")
	public void create() {
		LOG.info("ProgramAPI | checkSuspendedResearchers Begin");
		this.programService.checkSuspendedResearchers();
		LOG.info("ProgramAPI | checkSuspendedResearchers Leaving");
	}
}
