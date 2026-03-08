package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public class ResourcesDAOImpl implements ResourcesDAO {
	 private static final Logger LOG = LoggerFactory.getLogger(ResourcesDAOImpl.class);
	 
	 private static final String ERROR_MSG_1 = "ResourcesDAOImpl | Exception in getResources - ";
	 
	 private static final String ERROR_MSG_2 = "ResourcesDAOImpl | Exception in  getChild - ";
	 
	 private static final String ERROR_MSG_3 = "ResourcesDAOImpl | Exception in  getTableResources - ";
	 
	 private static final String ERROR_MSG_4 = "ResourcesDAOImpl | Exception in  getresourcesbyid - ";
	 
	 private static final String ERROR_MSG_5 = "ResourcesDAOImpl | Exception in  deleteResources - ";
	 
	 private static final String ERROR_MSG_6 = "ResourcesDAOImpl | Exception in  addcategory - ";
	 
	 private static final String ERROR_MSG_7 = "ResourcesDAOImpl | Exception in  addcateogoryitem - ";
	 
	 private static final String ERROR_MSG_8 = "ResourcesDAOImpl | Exception in  updateresources - ";
	 
	 
	 
	 
	 
	@Autowired
	public JdbcTemplate jdbcTemplate;
	


	@Override
	public List<Map<String, Object>> getResources() {
		try {
		 List<Map<String, Object>> guidelines = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.StdGuidelines);
		return guidelines;
		}
		catch(DataAccessException e) {
			LOG.error(ERROR_MSG_1, e);
			return null;
		}
	}
	
	
	@Override
	public List<Map<String, Object>> getChild(int id) {
		try {
		 List<Map<String, Object>> data = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.ChildGuidelines, id);
		return data;
		}
		catch(DataAccessException e) {
			LOG.error(ERROR_MSG_2, e);
			return null;
			
		}
	}
	
	@Override
	public List<Map<String, Object>> getTableResources() {
		try {
		List<Map<String, Object>> getdata = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.GETResources);
		return getdata;
		}
		catch(DataAccessException e) {
			LOG.error(ERROR_MSG_3, e);
			return null;
			
		}
		
	}
	
	
	@Override
	public List<Map<String, Object>> getresourcesbyid(int id) {
		try {
		List<Map<String, Object>> datas = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.getresourcesbyid, id);
		return datas;
		}
		catch(DataAccessException e) {
			LOG.error(ERROR_MSG_4, e);
			return null;
			
		}
	}
	
	@Override
	public void deleteResources(int id) {
		try {
		this.jdbcTemplate.update(BugHuntrQueryConstants.DeleteResources,id);
		}
		catch(DataAccessException e) {
			LOG.error(ERROR_MSG_5, e);
		}
		
	}
	

	@Override
	public void addcategory(Resources resource) {
		try {
		this.jdbcTemplate.update(BugHuntrQueryConstants.InsertResources, resource.getGuidelines());
		}
		catch(DataAccessException e) {
			LOG.error(ERROR_MSG_6, e);
		}
		
	}
	
	@Override
	public void addcateogoryitem(Resources resources) {
		try {
		jdbcTemplate.update("INSERT INTO b_standardguidelines(masterid, paneltitle, paneldescription, entries) VALUES(?, ?, ?, ?)",
				resources.getMasterid(), resources.getPaneltitle(), resources.getPaneldescription(), resources.getEntries());
		}
		catch(DataAccessException e) {
			LOG.error(ERROR_MSG_7, e);
		}
	}

	@Override
	public void updateresources(int id, Resources editresources) {
		try {
			this.jdbcTemplate.update("UPDATE b_standardguidelines SET masterid = ?, paneltitle = ?, paneldescription = ?, entries = ? WHERE id = ?",
					editresources.getMasterid(), editresources.getPaneltitle(), editresources.getPaneldescription(), editresources.getEntries(), id);

		}
		catch(DataAccessException e) {
		LOG.error(ERROR_MSG_8, e);
		}
	}
	

}

