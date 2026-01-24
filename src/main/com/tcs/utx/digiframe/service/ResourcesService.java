package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Resources;

public interface ResourcesService {

	public List<Map<String, Object>> getChild(int id);

	public List<Map<String, Object>> getTableResources();

	public List<Map<String, Object>> getresourcesbyid(int id);

	public void deleteResources(int id);

	public void addcategory(Resources resource);

	public void addcategoryitem(Resources resources);

	public void updateresources(int id, Resources editresources);

	public boolean updateresourcesbyid(Resources editresources);

	boolean validateGuidelines(Resources data);

	boolean validateResources(Resources data);

	public boolean validatecategory(Resources resource);

	public boolean validatecategoryitem(Resources resources);

	public List<Map<String, Object>> getResources();

}
