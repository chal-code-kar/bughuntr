package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;


public interface BrandingDetailsService {

	public Map<String,String> getBrandingDetails(String userId, String prevLoginTime);
	
	List<String> getUserRoles(String empID);
	
	public boolean isUserGuest();

	public boolean isUserAdmin(int employeeid);
	
	/**
	 * Validates user credentials.
	 * @param userId the user ID
	 * @param password the password
	 * @return true if credentials are valid, false otherwise
	 */
	public boolean validateUser(String userId, String password);
}
