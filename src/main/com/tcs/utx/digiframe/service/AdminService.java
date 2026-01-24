package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Menu;

public interface AdminService {

	public List<Map<String, Object>> getMenus();

	public void updateMenu(Menu data);

	public void deleteMenu(int srno);

	public boolean validateMenu(Menu data);

	public void addMenu(Menu data);

}
