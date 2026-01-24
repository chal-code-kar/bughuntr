package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.AdminDAO;
import com.tcs.utx.digiframe.model.Menu;
import com.tcs.utx.digiframe.util.TSSVStringUtil;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	 private AdminDAO dao;

	@Override
	public List<Map<String, Object>> getMenus() {
		return this.dao.getMenus();
	}

	@Override
	public void updateMenu(Menu data) {
		this.dao.updateMenu(data);
	}

	@Override
	public void deleteMenu(int srno) {
		this.dao.deleteMenu(srno);
	}

	@Override
	public boolean validateMenu(Menu data) {
		if (data.getMenuname() == null || data.getMenuname().trim().length() < 2 || data.getMenuname().trim().length() > 100
                || !TSSVStringUtil.matchPattern(data.getMenuname().trim(), TSSVStringUtil.PATTERN_FOR_DESCRIPTION)){
            return false;
        } else if(data.getLink() == null || data.getLink().trim().length() < 1 || data.getLink().trim().length() > 100
        		|| !TSSVStringUtil.matchPattern(data.getLink().trim(), TSSVStringUtil.PATTERN_FOR_DESCRIPTION)) {
        	return false;
        }else if(data.getRole() == null || data.getRole().trim().length() < 1 || data.getRole().trim().length() > 100
        		|| !TSSVStringUtil.matchPattern(data.getRole().trim(), TSSVStringUtil.PATTERN_FOR_NAMES)) {
        	return false;
        }  
        else {
            return true;
        }
	}

	@Override
	public void addMenu(Menu data) {
		this.dao.addMenu(data);
	}

}
