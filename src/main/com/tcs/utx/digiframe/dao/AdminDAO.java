package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Menu;

public interface AdminDAO {

	public void addMenu(Menu data);

	public void deleteMenu(int srno);

	public void updateMenu(Menu data);

	public List<Map<String, Object>> getMenus();

}
