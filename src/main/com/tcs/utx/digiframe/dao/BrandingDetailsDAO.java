package com.tcs.utx.digiframe.dao;

import java.util.List;

import com.tcs.utx.digiframe.dto.BrandingDTO;
import com.tcs.utx.digiframe.exception.UserDefinedException;


public interface BrandingDetailsDAO {

	public BrandingDTO getBrandingDetails(String userId);

//	public List getUserRole(String userId);

	List<String> getUserRoles(String empNo) throws UserDefinedException;
	
}
