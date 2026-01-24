package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Query;

public interface QueryDAO {

	 public void PostQuery(Query query);
	 
	 public List<Map<String, Object>> MYQuery(int emp_id);
	 
	 public List<Map<String, Object>> AllQuery();
	 
	 public void updatequery(Query updatequery);
}
