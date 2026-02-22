package com.tcs.utx.digiframe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.service.WorkListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
@Validated
public class WorklistAPI {

    private static final Logger LOG = LoggerFactory.getLogger(WorklistAPI.class);

    public static final String ERROR = "Something Went Wrong.";
    
    public static final String  ACCESS_DENIED = "Access Denied";
    
    @Autowired
    private WorkListService service;

    @Autowired
    private PermissionHelperService permissionService;

    @Autowired
	private BrandingDetailsService brandingService;

    @RequestMapping(value = "worklists", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    public ResponseEntity<Map<String, Object>> viewTodo(HttpServletRequest request, @Min(1) @RequestParam(required=false) Integer user_id) {
        Map<String, Object> workList = new HashMap<>();
        try {
            LOG.info("WorkListController | getWorkList | invoked");

    		boolean isGuest = brandingService.isUserGuest();
    		if (isGuest) {
    			workList.put("text", ACCESS_DENIED);
    			return new ResponseEntity<>(workList, HttpStatus.FORBIDDEN);
    		}

            int authenticated_user = BrandingDetailsController.getUser();

    		// Only allow viewing another user's worklist if the requester is an admin
    		if (user_id != null && user_id != authenticated_user) {
    			if (!this.permissionService.isOperationPermissible("BugHuntr", "Admin", "View", authenticated_user, 0, 0)) {
    				LOG.info("WorkListController | Access Denied - user attempted to view another user's worklist");
    				workList.put("text", ACCESS_DENIED);
    				return new ResponseEntity<>(workList, HttpStatus.FORBIDDEN);
    			}
    		}

    		if(user_id==null){
				user_id = authenticated_user;
			}

            int emp_id = Integer.valueOf(user_id);
            if (this.permissionService.isOperationPermissible("BB Program", "New Program", "Add", emp_id, 0, 0)) {
            	workList = this.service.getWorklistForProgramAdmin(emp_id);
            } else {
            	workList = this.service.getWorklistForResearcher(emp_id);
            }
            
            workList.put("emp_id", emp_id);

            LOG.info("WorkListController | getWorkList | leaving");
        } catch (DataAccessException e) {
			LOG.error("WorkListController | DataAccessexception in getWorkList - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("WorkListController | Exception in getWorkList", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
            return new ResponseEntity<Map<String, Object>>(workList, HttpStatus.OK);
        
    }

    @RequestMapping(value = "reports", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    public ResponseEntity<List<Map<String, Object>>> reports() {
        List<Map<String, Object>> reportList = new ArrayList<>();
            LOG.info("WorkListController | reports | invoked");
            try {
            int emp_id = BrandingDetailsController.getUser();

    		boolean isGuest = brandingService.isUserGuest();
    		if (isGuest) {
    			LOG.info("WorkListController | Access Denied in reports");
    			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    		}

            if (this.permissionService.isOperationPermissible("BugHuntr", "Admin", "View", emp_id, 0, 0)) {
            	reportList = this.service.reportsForResearcher(emp_id, "Admin");
            } else if (this.permissionService.isOperationPermissible("BB Program", "New Program", "Add", emp_id, 0,
                    0)) {
            	reportList = this.service.reportsForResearcher(emp_id, "Bounty Admin");
            } else {
            	reportList = this.service.reportsForResearcher(emp_id, "Researcher");
            }
            LOG.info("WorkListController | reports | leaving");
            } catch (DataAccessException e) {
    			LOG.error("PermissionHelperController | DataAccessexception in getChild - ", e);
    			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    		} catch (Exception e) {
    			LOG.error("PermissionHelperController | Exception in getChild", e);
    			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    		}
            return new ResponseEntity<>(reportList, HttpStatus.OK);
    }

}
