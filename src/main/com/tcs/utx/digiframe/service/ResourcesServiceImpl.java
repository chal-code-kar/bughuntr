package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.ResourcesDAO;
import com.tcs.utx.digiframe.model.Resources;
import com.tcs.utx.digiframe.util.TSSVStringUtil;

@Service
public class ResourcesServiceImpl implements ResourcesService {

	@Autowired
	private ResourcesDAO dao;

	@Override
	public List<Map<String, Object>> getResources() {
		return this.dao.getResources();
	}

	@Override
	public List<Map<String, Object>> getChild(int id) {
		return this.dao.getChild(id);
	}

	@Override
	public List<Map<String, Object>> getTableResources() {

		return this.dao.getTableResources();
	}

	@Override
	public List<Map<String, Object>> getresourcesbyid(int id) {
		return this.dao.getresourcesbyid(id);
	}

	@Override
	public void deleteResources(int id) {
		this.dao.deleteResources(id);

	}

	@Override
	public void addcategory(Resources resource) {
		this.dao.addcategory(resource);

	}

	@Override
	public void addcategoryitem(Resources resources) {
		this.dao.addcateogoryitem(resources);
	}

	@Override
	public void updateresources(int id, Resources editresources) {
		this.dao.updateresources(id, editresources);
	}

	@Override
	public boolean validateGuidelines(Resources data) {
		if (data.getGuidelines() == null || data.getGuidelines().trim().length() < 2
				|| data.getGuidelines().trim().length() > 250 || !TSSVStringUtil
						.matchPattern(data.getGuidelines().trim(), TSSVStringUtil.PATTERN_FOR_DESCRIPTION)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean validateResources(Resources data) {
		if (data.getPaneltitle() == null || data.getPaneltitle().trim().length() < 2
				|| data.getPaneltitle().trim().length() > 100) {
			return false;
		} else if (data.getPaneldescription() == null || data.getPaneldescription().trim().length() < 2
				|| data.getPaneldescription().trim().length() > 1000) {
			return false;
		}else if (data.getEntries() == null || data.getEntries().trim().length() < 4) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean updateresourcesbyid(Resources editresources) {
		if (editresources.getMasterid() == 0) {
			return false;
		} else if (editresources.getPaneltitle() == null || editresources.getPaneltitle().trim().length() > 100 ) {
			return false;

		} else if (editresources.getPaneldescription() == null || editresources.getPaneltitle().trim().length() > 1000) {
			return false;
		} else if (editresources.getEntries() == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean validatecategory(Resources resource) {
		if (resource.getGuidelines() == null || resource.getGuidelines().trim().length() > 250 
				|| !TSSVStringUtil.matchPattern(resource.getGuidelines().trim(),
				TSSVStringUtil.PATTERN_FOR_DESCRIPTION)) {
			return false;
		} else {
			;
		}
		return true;
	}

	@Override
	public boolean validatecategoryitem(Resources resources) {
		if (resources.getMasterid() == 0) {
			return false;
		} else if (resources.getPaneltitle() == null) {
			return false;

		} else if (resources.getPaneldescription() == null || !TSSVStringUtil
				.matchPattern(resources.getPaneldescription().trim(), TSSVStringUtil.PATTERN_FOR_DESCRIPTION)) {
			return false;
		} else if (resources.getEntries() == null) {
			return false;
		} else {
			return true;
		}

	}

}
