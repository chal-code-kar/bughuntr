package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.History;

public interface HistoryService {

	List<Map<String, Object>> getHistory();
    
	void Posthistory(History posthistory);
	
	void updateHistory(int srno, History edithistory);
	
	void deleteHistory(int srno);
	
	boolean validateAddHistory(History posthistory);
}
