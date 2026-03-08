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

import com.tcs.utx.digiframe.model.Blog;
import com.tcs.utx.digiframe.service.BlogService;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.PermissionHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/v1/")
@Validated
public class BlogAPI {

	private static final Logger LOG = LoggerFactory.getLogger(BlogAPI.class);

	private static final String GLB_ERROR_MSG = "An error has occured please try again or contact Administrator";

	public static final String ERROR = "Something Went Wrong.";

	public static final String BUGHUNTR = "BugHuntr";

	public static final String ADMIN = "Admin";

	public static final String GUEST = "Guest";

	public static final String ERROR_MSG = "You are not the bugHuntr admin";

	public static final String ACCESS_DENIED = "ACCESS_DENIED";

	@Autowired
	private BrandingDetailsService brandingService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private PermissionHelperService permissionService;

	@RequestMapping(value = "blogs", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getBlogs() {
		Map<String, Object> retData = new HashMap<>();
 
		try {
			int emp_id = BrandingDetailsController.getUser();
			List<Map<String, Object>> content = new ArrayList<>();

			boolean isGuest = brandingService.isUserGuest();

			if (isGuest) {
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}	
			
			LOG.info("BlogController | getBlogs Begin");
			if (this.permissionService.isOperationPermissible(BUGHUNTR, GUEST, "View", emp_id, 0, 0)) {
				LOG.info("BlogController | Access Denied in Blog");
				retData.put("text", ACCESS_DENIED);
				return new ResponseEntity<>(retData, HttpStatus.FORBIDDEN);
			}
  
			content = this.blogService.getBlogs();
			retData.put("content", content);

			LOG.info("BlogController | getBlogs Exit");
		} catch (DataAccessException e) {
			LOG.error("BlogController | Exception in Blog", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("BlogController | Exception  in Blog", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(retData, HttpStatus.OK);
 
	}

	@RequestMapping(value = "addBlog", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> addBlog(@Valid @RequestBody Blog blog) {
		try {
			LOG.info("BlogController | addBlog Begin");
			int emp_id = BrandingDetailsController.getUser();
			
			boolean isGuest = brandingService.isUserGuest();
			
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("BlogController | Access Denied in addBlog");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}

			if (!this.blogService.validateBlog(blog)) {
				LOG.info("BlogController | Validation Failed in addBlog");
				return new ResponseEntity<>("Validation Failed", HttpStatus.BAD_REQUEST);
			}
			this.blogService.addBlog(blog, emp_id);
			LOG.info("BlogController | addBlog Exit");

		} catch (DataAccessException e) {
			LOG.error("BlogController | Exception in addBlog ", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("BlogController | Exception in  addBlog", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Blog Added", HttpStatus.OK);

	}

	@RequestMapping(value = "deleteBlog/{srno}", method = RequestMethod.DELETE, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> deleteBlog(@Min(1) @PathVariable int srno) {
		try {
			LOG.info("BlogController | DeleteBlog Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("BlogController | Access Denied in DeleteBlog");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}
			this.blogService.deleteBlog(srno);
			LOG.info("BlogController | DeleteBlog Exit");
		} catch (DataAccessException e) {
			LOG.error("BlogController | Exception in deleteBlog", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("BlogController | Exception in  deleteBlog", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Blog Deleted !", HttpStatus.OK);
	}

	@RequestMapping(value = "updateBlog", method = RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public ResponseEntity<String> updateBlog(@Valid @RequestBody Blog blog) {
		try {
			LOG.info("BlogController | UpdateBlog Begin");
			int emp_id = BrandingDetailsController.getUser();

			boolean isGuest = brandingService.isUserGuest();
			if (isGuest) {
				return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
			}

			if (!this.permissionService.isOperationPermissible(BUGHUNTR, ADMIN, "View", emp_id, 0, 0)) {
				LOG.info("BlogController | Access Denied in UpdateBlog");
				return new ResponseEntity<>(ERROR_MSG, HttpStatus.FORBIDDEN);
			}
			this.blogService.updateBlog(blog);
			LOG.info("BlogController | UpdateBlog Exit");
		} catch (DataAccessException e) {
			LOG.error("BlogController | Exception in updateBlog", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOG.error("BlogController | Exception in  updateBlog", e);
			return new ResponseEntity<>(GLB_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Blog Updated!", HttpStatus.OK);

	}


}
