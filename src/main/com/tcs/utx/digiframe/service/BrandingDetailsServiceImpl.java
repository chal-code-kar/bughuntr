package com.tcs.utx.digiframe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.utx.digiframe.controller.BrandingDetailsController;
import com.tcs.utx.digiframe.dao.BrandingDetailsDAO;
import com.tcs.utx.digiframe.dto.BrandingDTO;
import com.tcs.utx.digiframe.exception.UserDefinedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BrandingDetailsServiceImpl implements BrandingDetailsService {

	@Value("${app.demo-password:Pass@123}")
	private String demoPassword;

	private static final Logger LOG = LoggerFactory.getLogger(BrandingDetailsController.class);

	private static final String GUEST_USER = "UserSpecificDetailsImpl | isUserGuest" ;
	
	@Autowired
	private BrandingDetailsDAO brandingDAO;

	@Override
	public Map<String, String> getBrandingDetails(String userId, String prevLoginTime) {
		Map<String, String> brandingDetailsMap = new HashMap<>();
		BrandingDTO brandingDTO = brandingDAO.getBrandingDetails(userId);
		brandingDetailsMap.put("ID", brandingDTO.getUserId());
		brandingDetailsMap.put("firstName", brandingDTO.getFirstName());
		brandingDetailsMap.put("lastName", brandingDTO.getLastName());
		brandingDetailsMap.put("icalmsRole", brandingDTO.getIcalmsRole());
		brandingDetailsMap.put("projectName", brandingDTO.getProjectName());
		brandingDetailsMap.put("supervisorName", brandingDTO.getSupervisorName());
		brandingDetailsMap.put("lastLogin", prevLoginTime.trim());
		return brandingDetailsMap;
	}






	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public List<String> getUserRoles(String empID) {
		List<String> userRole = new ArrayList<String>();

		try {
			userRole = this.brandingDAO.getUserRoles(empID);
		} catch (UserDefinedException e) {
			LOG.error(GUEST_USER, e);
		}
		return userRole;
	}

	public boolean isUserGuest() {
		try {

			String principal = "" + BrandingDetailsController.getUser();
			List<String> userRoles = getUserRoles(principal);
			for (int i = 0; i < userRoles.size(); i++) {
				if ("ROLE_Guest".equals(userRoles.get(i))) {
					return true;
				}
				
			}
		} catch (DataAccessException e) {
			LOG.error(GUEST_USER, e);
		}
		return false;
	}
	public boolean isUserAdmin(int employeeid) {
		try {
        
			
			String principal = "" + BrandingDetailsController.getUser();
			
			List<String> userRoles = getUserRoles(String.valueOf(principal));
			for (int i = 0; i < userRoles.size(); i++) {
				if ("ROLE_Administrator".equals(userRoles.get(i))) {
					return true;
				}
				
			}
			
		} catch (DataAccessException e) {
			LOG.error(GUEST_USER, e);
		}
		
		return false;
	}
	
	public boolean isBountyAdmin(int employeeid) {
		try {
			String principal = "" + BrandingDetailsController.getUser();
			
			List<String> userRoles = getUserRoles(String.valueOf(principal));
			for (int i = 0; i < userRoles.size(); i++) {
				if ("ROLE_Bounty Administrator".equals(userRoles.get(i))) {
					return true;
				}
			}
		} catch (DataAccessException e) {
			LOG.error(GUEST_USER, e);
		}
		return false;
	}
	
	@Override
	public boolean validateUser(String userId, String password) {
		// Note: This is a demo application. In production, implement proper password hashing
		// using BCrypt or Argon2, and verify against stored hashed passwords in the database.
		try {
			List<String> userRoles = this.brandingDAO.getUserRoles(userId);
			if (userRoles != null && !userRoles.isEmpty()) {
				return demoPassword.equals(password);
			}
		} catch (Exception e) {
			LOG.error("Error validating user", e);
		}
		return false;
	}

}
