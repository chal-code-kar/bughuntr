package com.tcs.utx.digiframe.service;

import java.util.List;

import com.tcs.utx.digiframe.dto.UtxMenuItemServiceVo;
import com.tcs.utx.digiframe.exception.UserDefinedException;

public interface MenusService {

	public List<UtxMenuItemServiceVo> getUserMenus(String empNo) throws UserDefinedException;

	public String getRoleName(int roleId) throws UserDefinedException;

}
