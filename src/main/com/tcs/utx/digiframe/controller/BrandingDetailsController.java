package com.tcs.utx.digiframe.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import com.tcs.utx.digiframe.model.LoginRequest;

import com.tcs.utx.digiframe.dto.UtxMenuItemServiceVo;
import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import com.tcs.utx.digiframe.service.MenusService;
import com.tcs.utx.digiframe.service.ResearcherService;
import com.tcs.utx.digiframe.util.SSAComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/BugHuntr/api/")
public class BrandingDetailsController {

	@Autowired
	private MenusService menuService;

	@Autowired
	private ResearcherService researcherService;

	private static final Logger LOG = LoggerFactory.getLogger(BrandingDetailsController.class);

	@Value("${spring.session.store-type}")
	private String sessionStoreType;

	@Value("${JAR_TYPE:SSO}")
	private String jarType;

	@Autowired
	private BrandingDetailsService brandingService;

	/**
	 * This method is being used to fetch branding details and menus for user.
	 * 
	 * @param request
	 * @return
	 */

	private static final String GET_USER_MENU_LIST_ERROR = "LoginController | getUserMenuList";

	
	
	@RequestMapping(value = "/dologin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> setAuthoization(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		LOG.info("BrandingDetailsController | setAuthoization | invoked");
		Map<String, Object> response = new HashMap<>();
		
		String userId = loginRequest.getUserId();
		String password = loginRequest.getPassword();
		
		// Validate input
		if (userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID and password are required");
		}
		
		// Note: In production, implement proper password hashing and verification
		// This is a demo application - replace with actual authentication mechanism
		if (brandingService.validateUser(userId, password)) {
			HttpSession session = request.getSession();
			session.setAttribute("USER_ID", userId);
			LOG.info("USER ID SET IN SESSION");
			
			List<String> userRoles = brandingService.getUserRoles(userId);
			List<GrantedAuthority> authorities = new ArrayList<>();
			if (userRoles != null && !userRoles.isEmpty()) {
				userRoles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
			}
			Authentication auth = new UsernamePasswordAuthenticationToken(
				userId, 
				null,   
				authorities 
			);
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			response.put("success", true);
			response.put("message", "Login successful");
			LOG.info("BrandingDetailsController | setAuthoization | leaving");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			SecurityContextHolder.clearContext();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}
	}
	/**
	 * This method returns lastLoginTime of logged in user. Last login time will be
	 * retrieved through request header. Then this method will convert into proper
	 * format.
	 * 
	 * @param request
	 * @return
	 */
	private String getLastloginTime(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String previousLoginTime = null;

		if (session != null) {
			Object storedLoginTime = session.getAttribute("PrevLoginTime");
			if (storedLoginTime instanceof Long) {
				long loginTimeLong = (Long) storedLoginTime;
				Date date = new Date(loginTimeLong);
				Calendar cal = new GregorianCalendar();
				cal.setTime(date);
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss aa z");
				dateFormat.setCalendar(cal);
				previousLoginTime = dateFormat.format(date);
			}

			// Store current time as the previous login time for next request
			session.setAttribute("PrevLoginTime", System.currentTimeMillis());
		}

		if (previousLoginTime == null) {
			Calendar cal = new GregorianCalendar();
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd hh:mm:ss z aa yyyy");
			dateFormat.setCalendar(cal);
			cal.setTime(date);
			previousLoginTime = dateFormat.format(date);
		}
		return previousLoginTime;
	}
	
	private void setCustomAttributes(String principal, HttpServletRequest request) {
		LOG.debug("LoginController | setCustomAttributes action initiated");
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			
			 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			        String empNo = auth.getPrincipal().toString(); // principal is userId

			HttpSession session = request.getSession();

			session.setAttribute("LoggedInUser", empNo);

			List<String> userRoles = brandingService.getUserRoles(principal);
			List<GrantedAuthority> authorities = new ArrayList<>();
			try {
				if (userRoles != null && !userRoles.isEmpty()) {
					for (String role : userRoles) {
						authorities.add(new SimpleGrantedAuthority(role));
					}
				}
			} catch (NumberFormatException e) {
				LOG.error(GET_USER_MENU_LIST_ERROR, e);
			}

			Authentication newAuth = new UsernamePasswordAuthenticationToken(
			        auth.getPrincipal(), 
			        auth.getCredentials(), 
			        authorities 
			);
			SecurityContextHolder.getContext().setAuthentication(newAuth);
					}
		LOG.info("LoginController | setCustomAttributes action terminated");
	}
	
	

	private List<UtxMenuItemServiceVo> getUserMenuList(String empNo) {
		List<UtxMenuItemServiceVo> menuList = new ArrayList<UtxMenuItemServiceVo>();
		try {

			menuList.addAll(this.menuService.getUserMenus(empNo));
			Collections.sort(menuList, new SSAComparator());

		} catch (UserDefinedException e) {
			LOG.error(GET_USER_MENU_LIST_ERROR, e);
		}
		return menuList;
	}
			
	
	@RequestMapping(value = "/menu", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ResponseEntity<Map<String, Object>> getUserDetails(HttpServletRequest request) {
		LOG.info("BrandingDetailsController | getUserDetails | invoked");
		Map<String, Object> userSpecificDetails = new HashMap<>();
		String prevLoginTime = getLastloginTime(request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getPrincipal().toString();
        if(userId.equalsIgnoreCase("anonymousUser")) {
        	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

		boolean smEnv = "SSO".equals(jarType);
		if (smEnv == false && "redis".equals(sessionStoreType)) {
			smEnv = true;
		}

		userSpecificDetails.put("SM_ENV", smEnv);
		
		 // This will fetch branding details of logged in user. If application team
		//  doesn't want to use branding details then below two lines can be removed.
		 
		LOG.debug("BrandingDetailsController | fetching branding details--");
		Map<String, String> brandingDetails = brandingService.getBrandingDetails(userId, prevLoginTime);
		userSpecificDetails.put("brandingDetails", brandingDetails);
		LOG.debug("BrandingDetailsController | fetching menus--");
		


		setCustomAttributes(userId, request);
		
		userSpecificDetails.put("roles", auth.getAuthorities());
		userSpecificDetails.put("menuList", getUserMenuList(userId));
		String profile_pic = researcherService.getResearcherProfile(getUser()).getProfile_pic();
		int srno = researcherService.getResearcherProfile(getUser()).getSrno();
		
		if(profile_pic == null || profile_pic.length()==0) {
			profile_pic = "N1";
		}
		
		String profile = profile_pic + "-" + srno;
		userSpecificDetails.put("profile", profile);
		LOG.info("BrandingDetailsController | getUserDetails | leaving");
		return new ResponseEntity<>(userSpecificDetails, HttpStatus.OK);
	}
	

public static int getUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() != null) {
        String empNo = auth.getPrincipal().toString(); // principal is userId
        return Integer.parseInt(empNo);
    }
    throw new IllegalStateException("No authenticated user found");
}
	
	
	@RequestMapping(value = "/getUserId", method = RequestMethod.GET)
	public static int getUser2() {
		   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth != null && auth.getPrincipal() != null) {
		        String empNo = auth.getPrincipal().toString(); // principal is userId
		        return Integer.parseInt(empNo);
		    }
		    throw new IllegalStateException("No authenticated user found");
	}	
	

	public JSONArray getUserRoles(String principal) {
		List<String> userRoles = brandingService.getUserRoles(principal);
		JSONArray roles = new JSONArray();
		try {
			if (userRoles != null && !userRoles.isEmpty()) {
				for (String role : userRoles) {
					JSONObject obj = new JSONObject();
					obj.put("authority", role);
					roles.put(obj);
				}
			}
		} catch (NumberFormatException e) {
			LOG.error(GET_USER_MENU_LIST_ERROR, e);
		} catch (JSONException e) {
			LOG.error(GET_USER_MENU_LIST_ERROR, e);
		}		
		return roles;
	}
	
	@RequestMapping(value = "/isAuthUser", method = RequestMethod.GET)
	public static ResponseEntity<String> isAuthUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.getPrincipal() != null) {
	        return new ResponseEntity<String>("Authenticated",HttpStatus.OK);
	    }
	    return new ResponseEntity<String>("Not Authencticated",HttpStatus.UNAUTHORIZED);
	 }

}
