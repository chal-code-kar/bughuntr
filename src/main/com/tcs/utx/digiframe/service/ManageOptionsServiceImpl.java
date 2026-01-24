package com.tcs.utx.digiframe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.ManageOptionsDAO;
import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.DynamicDropdown;

@Service
public class ManageOptionsServiceImpl implements ManageOptionsService {

	@Autowired
	ManageOptionsDAO manageDAO;
	
	@Override
	public List<Map<String, Object>> getOptions(String category) throws UserDefinedException {
		if("Vulnerability Category".equals(category)){
			return manageDAO.getVulnCat();
		} else {
			return manageDAO.getOptions(category);
		}
	}
	
	@Override
	public List<String> getOptionList(String category) throws UserDefinedException {
		List<String> retData = new ArrayList<>();
		if("Vulnerability Category".equals(category)){
			List<Map<String, Object>> data = manageDAO.getVulnCat();
			for(Map<String, Object> temp:data) {
				int srno = (int) temp.get("srno");
				retData.add(String.valueOf(srno));
			}
		} else {
			List<Map<String, Object>> data = manageDAO.getOptions(category);
			for(Map<String, Object> temp:data) {
				retData.add((String) temp.get("lookupcode"));
			}
		}
		return retData;
	}

	@Override
	public int AddDropdown(DynamicDropdown adddropdown, int emp_id) throws UserDefinedException{
		return this.manageDAO.AddDropdown(adddropdown,emp_id);
		
	}

	@Override
	public void deleteDropdown(int id) {
		 this.manageDAO.deleteDropdown(id);
		
	}

	@Override
	public int updateDropdown(int id, DynamicDropdown updateDropdown, int emp_id) throws UserDefinedException {
		 return this.manageDAO.updateDropdown(id,updateDropdown , emp_id); 
		
	}

	@Override
	public List<Map<String, Object>> GetAllDropdown() {
		return this.manageDAO.GetAllDropdown();
	}

	@Override
	public List<Map<String, Object>> GetDropdown() {
		return this.manageDAO.GetDropdown();
	}

	@Override
	public boolean checkExistsInOptions(DynamicDropdown adddropdown) throws UserDefinedException {
		return this.manageDAO.checkExistsInOptions(adddropdown);
	}
	

}
