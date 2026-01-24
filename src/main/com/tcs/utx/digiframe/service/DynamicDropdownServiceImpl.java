package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.DynamicDropdownDAO;
import com.tcs.utx.digiframe.dao.VulnerabilityDAO;

@Service
public class DynamicDropdownServiceImpl implements DynamicDropdownService {

	
	  @Autowired
	  private DynamicDropdownDAO DynamicDropdown;
	
	@Override
    public List<Map<String, Object>> getAllOptions() {
        return this.DynamicDropdown.getAllOptions();
    }
}
