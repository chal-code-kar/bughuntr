package com.tcs.utx.digiframe.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import com.tcs.utx.digiframe.model.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class QueryDAOImpl implements QueryDAO{
	 private static final Logger LOG = LoggerFactory.getLogger(QueryDAOImpl.class);
	 
	  private static final String ERROR_MSG_1 = "QueryDaoImpl | Exception in PostQuery - ";
	  
	  private static final String ERROR_MSG_2 = "QueryDaoImpl | Exception in MYQuery - ";
	  
	  private static final String ERROR_MSG_3 = "QueryDaoImpl | Exception in AllQuery - ";
	  
	  private static final String ERROR_MSG_4 = "QueryDaoImpl | Exception in updatequery - ";
	  
	  
	@Autowired
	  public JdbcTemplate jdbcTemplate;
	  @Override
	    public void PostQuery(Query query) {
	        try {
	        	jdbcTemplate.update("INSERT INTO b_query(createdby, query, publish, active) VALUES(?, ?, ?, ?)",
	        			query.getCreatedby(), query.getQuery(), query.isPublish(), true);
	        }
	        catch(DataAccessException e) {
	        	LOG.error(ERROR_MSG_1, e);
	        }
	        
	    }
	  
	  
	  @Override
	    public List<Map<String, Object>> MYQuery(int emp_id) {
		  try {
	        List<Map<String, Object>> faqdatas1 = jdbcTemplate.queryForList(BugHuntrQueryConstants.Myquery,emp_id);
	        return faqdatas1;
		  }
		  catch(DataAccessException e) {
			  LOG.error(ERROR_MSG_2, e);
			  return null;
		  }
	   }
	  
	  @Override
		public List<Map<String, Object>> AllQuery() {
		  try {
		 List<Map<String, Object>> allquery = jdbcTemplate.queryForList(BugHuntrQueryConstants.AllQuery);
			return allquery;
		  }
		  catch(DataAccessException e) {
			  LOG.error(ERROR_MSG_3, e);
			  return null; 
			  
		  }
		}
	  
	  @Override
	  public void updatequery(Query updatequery) {
	    try {
	        jdbcTemplate.update(BugHuntrQueryConstants.UpdateQuery, updatequery.getAnswer(), updatequery.getAnswerby() , updatequery.getSrno(),true);
	        jdbcTemplate.update(BugHuntrQueryConstants.UpdateActive, false);
	        
	    }
	    catch(DataAccessException e) {
	    	LOG.error(ERROR_MSG_4, e);
	    }
	    	
	    }

}

