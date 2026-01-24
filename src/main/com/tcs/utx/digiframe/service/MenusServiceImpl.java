package com.tcs.utx.digiframe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.utx.digiframe.dao.MenuServiceDAO;
import com.tcs.utx.digiframe.dto.UtxMenuItemServiceVo;
import com.tcs.utx.digiframe.exception.UserDefinedException;

@Service
public class MenusServiceImpl implements MenusService {

	@Autowired
	private MenuServiceDAO menuServiceDAO;

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public List<UtxMenuItemServiceVo> getUserMenus(String empNo) throws UserDefinedException {
		return this.menuServiceDAO.getUserMenus(empNo);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public String getRoleName(int roleId) throws UserDefinedException {
		return this.menuServiceDAO.getRoleName(roleId);
	}

}
