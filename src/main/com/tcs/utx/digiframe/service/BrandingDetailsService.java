package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;


public interface BrandingDetailsService {

	public Map<String,String> getBrandingDetails(String userId, String prevLoginTime);
	
//	public List getUserRole(String userId);
	
	List<String> getUserRoles(String empID);
	
	public boolean isUserGuest();

	public boolean isUserAdmin(int employeeid);
}
