package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.DynamicDropdown;

public interface ManageOptionsService {

	public List<Map<String, Object>> getOptions(String category) throws UserDefinedException;

	public List<String> getOptionList(String category) throws UserDefinedException;

	public int AddDropdown(DynamicDropdown adddropdown, int emp_id) throws UserDefinedException;

	public void deleteDropdown(int id);

	public int updateDropdown(int id, DynamicDropdown updateDropdown, int emp_id) throws UserDefinedException;

	public List<Map<String, Object>> GetAllDropdown();

	public List<Map<String, Object>> GetDropdown();
	
	public boolean checkExistsInOptions(DynamicDropdown adddropdown) throws UserDefinedException;
	
}
