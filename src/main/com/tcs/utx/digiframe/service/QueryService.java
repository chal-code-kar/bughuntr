package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Query;

public interface QueryService {
	
	 public void PostQuery(Query query);
	 
	 public boolean validateQueryPost(Query query);
	 
	 public List<Map<String, Object>> MYQuery(int emp_id);
	  
	 public List<Map<String, Object>> AllQuery();
	 
	 public boolean validateQuery(Query updatequery);
	 
	  public void updatequery(Query updatequery);

	    
}
