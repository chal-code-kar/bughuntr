package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class AdminDAOImpl implements AdminDAO {
	private static final Logger LOG = LoggerFactory.getLogger(AdminDAOImpl.class);
	private static final String ERROR_MSG_1 = "AdminDAOImpl | Error in addMenu - ";
	private static final String ERROR_MSG_2 = "AdminDAOImpl | Error in deleteMenu - ";
	private static final String ERROR_MSG_3 = "AdminDAOImpl | Error in updateMenu - ";
	private static final String ERROR_MSG_4 = "AdminDAOImpl | Error in getMenus - ";
	
	
	
	@Autowired
    public JdbcTemplate jdbcTemplate;

	@Override
	public void addMenu(Menu data) {
		try {
		jdbcTemplate.update(BugHuntrQueryConstants.addMenu,data.getMenuname(),data.getLink(),data.getRole());
		}catch(DataAccessException e) {
	    	LOG.error(ERROR_MSG_1, e);
	        
	    }
		}

	@Override
	public void deleteMenu(int srno) {
		try {
		jdbcTemplate.update(BugHuntrQueryConstants.deleteMenu,srno);
		}catch(DataAccessException e) {
	    	LOG.error(ERROR_MSG_2, e);
	        
	    }
		}

	@Override
	public void updateMenu(Menu data) {
		try {
		jdbcTemplate.update(BugHuntrQueryConstants.updateMenu,data.getMenuname(),data.getLink(),data.getRole(),data.getSrno());
	}catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_3, e);
        }
	}

	@Override
	public List<Map<String, Object>> getMenus() {
		try {
		 List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getMenus);
	     return data;
	}catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_4, e);
    	return null;
    }
}

}
