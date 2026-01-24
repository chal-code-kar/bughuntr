package com.tcs.utx.digiframe.dao;

import java.util.List;

import com.tcs.utx.digiframe.exception.UserDefinedException;


public interface MenuServiceDAO {

	public List getUserMenus(String empNo) throws UserDefinedException;

	public String getRoleName(int roleId) throws UserDefinedException;

}
