package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Resources;

public interface ResourcesDAO {

	public List<Map<String, Object>> getResources();

	public List<Map<String, Object>> getChild(int id);

	public List<Map<String, Object>> getTableResources();

	public List<Map<String, Object>> getresourcesbyid(int id);

	public void deleteResources(int id);

	public void addcategory(Resources resource);

	public void addcateogoryitem(Resources resources);

	public void updateresources(int id, Resources editresources);

}
