package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.QueryDAO;
import com.tcs.utx.digiframe.model.Query;
import com.tcs.utx.digiframe.util.TSSVStringUtil;

@Service
public class QueryServiceImpl implements QueryService{
	
	@Autowired
	 private QueryDAO QueryDAO;
	
	 @Override
	   public void PostQuery(Query query) {
	       this.QueryDAO.PostQuery(query);
	   }

	@Override
	  public boolean validateQueryPost(Query query) {
	       if (query.getQuery() == null || query.getQuery().length() > 100) {
	           return false;
	    }
	    else {
	        ;
	    }
	   return true;
	  }
	
	 @Override
	  public List<Map<String, Object>> MYQuery(int emp_id) { 
	       return this.QueryDAO.MYQuery(emp_id);
	  }
	 
	 @Override
	    public List<Map<String, Object>> AllQuery() {
	        return this.QueryDAO.AllQuery();
	    }
	 @Override
	   public void updatequery(Query updatequery) {
	       this.QueryDAO.updatequery(updatequery);    
	   }

	 @Override
	  public boolean validateQuery(Query updatequery) {
	       if (updatequery.getAnswer() == null || updatequery.getAnswer().length() > 1000 || !TSSVStringUtil.matchPattern(updatequery.getAnswer().trim(), TSSVStringUtil.PATTERN_MARKDOWN)) {
	              return false;
	       }
	       else {
	           ;
	       }
	      
	      return true;
	  }
	  
}
