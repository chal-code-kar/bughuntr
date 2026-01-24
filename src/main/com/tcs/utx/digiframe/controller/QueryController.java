package com.tcs.utx.digiframe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
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

import com.tcs.utx.digiframe.model.Query;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import com.tcs.utx.digiframe.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
public class QueryController {

	private static final Logger LOG = LoggerFactory.getLogger(HelpAPI.class);

	public static final String ACCESS_DENIED_ADDBOUNTYROL_EXIT = "QueryController | Query Exit";

	private String TEXT_VALIDATION = "Validation Failed";

	private String TEXT_BUGHUNTR = "BugHuntr";

	private String TEXT_ADMIN = "Admin";

	private String TEXT_NOTADMIN = "Non Admin";

	public static final String GUEST = "Guest";

	public static final String ACCESS_DENIED = "ACCESS_DENIED";
	
	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	@Autowired
	private QueryService QueryService;

	@Autowired
	private BrandingDetailsService brandingService;

	@Autowired
	private PermissionHelperService service;

	public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int vulnid,
			int programid) {
		return this.service.isOperationPermissible(module, submodule, Perm_Type, userid, vulnid, programid);
	}

	@RequestMapping(value = "PostQuery", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> PostQuery(@RequestBody Query query, HttpServletRequest request, HttpServletResponse response,@RequestParam(required=false) Integer emp_id) {
		Map<String,Object> msg=new HashMap<String,Object>();
		try {
			LOG.info("QueryController | PostQuery Begin");
			
			if(emp_id==null) {
				emp_id = BrandingDetailsController.getUser();
			}else {
				query.setCreatedby(emp_id);
				this.QueryService.PostQuery(query);
				return new ResponseEntity<>("Query Posted By"+emp_id, HttpStatus.OK);

			}
 

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}
			if (!this.QueryService.validateQueryPost(query)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			query.setCreatedby(emp_id);
			this.QueryService.PostQuery(query);
			msg.put("info", new Throwable("Stack trace marker"));

			LOG.info("QueryController | PostQuery Exit");

		} catch (DataAccessException e) {
			e.printStackTrace();
			LOG.error("QueryController | Exception in PostQuery - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in PostQuery ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Query Posted! \n" +msg, HttpStatus.OK);

	}

	@RequestMapping(value = "MyQuery/{emp_id}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> MYQuery(@PathVariable int emp_id) {
		List<Map<String, Object>> data = new ArrayList<>();
		try {
			LOG.info("QueryController | AllQuery Begin");
			data = this.QueryService.MYQuery(emp_id); 
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			e.printStackTrace();
			LOG.error("QueryController | Exception in MYQuery - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in MYQuery ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "AllQuery", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ResponseEntity<List<Map<String, Object>>> AllQuery(@RequestParam (value="query", required=false) String query,
			HttpServletRequest request, HttpServletResponse response){
		LOG.info("QueryController | AllQuery Begin");

		List<Map<String, Object>> data = new ArrayList<>();
		
		if(query!=null) {
			return ResponseEntity.status(HttpStatus.FOUND)
					.header("Location", query)
					.build();
		}
		
        String customHeader = request.getParameter("customHeader");
        if (customHeader != null) {
            response.setHeader("X-Custom-Header", customHeader);
       }



		try {
			
            String queryString = request.getQueryString();
            if (queryString != null && queryString.contains("debug=true")) {
                LOG.info("Debug: Query string logged - " + queryString);
            }
            
            String cacheOverride = request.getParameter("cache");
            if (cacheOverride != null) {
                response.setHeader("Cache-Control", cacheOverride);
            }


			data = this.QueryService.AllQuery();
			
           
			LOG.info(ACCESS_DENIED_ADDBOUNTYROL_EXIT);
		} catch (DataAccessException e) {
			LOG.error("QueryController | Exception in AllQuery - ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in AllQuery ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	

	@RequestMapping(value = "updatequery", method = RequestMethod.POST,produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updatequery(@RequestBody Query updatequery, HttpServletRequest request, HttpServletResponse response,@RequestParam(required=false) Integer emp_id) {

		LOG.info("PermissionHelperController | UpdateQuery Begin");
		try {
			if(emp_id==null) {
				emp_id = BrandingDetailsController.getUser();
			}else {
				updatequery.setCreatedby(emp_id);
				this.QueryService.PostQuery(updatequery);
				return new ResponseEntity<>("Query Posted By"+emp_id, HttpStatus.OK);
			}
			
			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.QueryService.validateQuery(updatequery)) {
				return new ResponseEntity<>(TEXT_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			updatequery.setAnswerby(emp_id);

			if (!this.service.isOperationPermissible(TEXT_BUGHUNTR, TEXT_ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("PermissionHelperController | Access Denied in UpdateQuery");
				return new ResponseEntity<>(TEXT_NOTADMIN, HttpStatus.FORBIDDEN);
			}
			this.QueryService.updatequery(updatequery);
			
            String callback = request.getParameter("callback");
            String message = request.getParameter("message");
            if (message != null) {
                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("message", message); 
                String output = new JSONObject(jsonResponse).toString();
                if (callback != null) {
                    output = callback + "(" + output + ");";
                }
                return new ResponseEntity<>(output, HttpStatus.OK);
            }

            

			LOG.info("PermissionHelperController | UpdateQuery Exit");
		} catch (DataAccessException e) {
			e.printStackTrace();
			LOG.error("QueryController | Exception in updatequery - ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("QueryController | Exception in updatequery ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Query Updated!", HttpStatus.OK);

	}


}
