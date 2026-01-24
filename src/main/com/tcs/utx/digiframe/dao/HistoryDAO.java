package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.History;

public interface HistoryDAO {
	
	List<Map<String, Object>> getHistory();
	
	void Posthistory(History posthistory);
	
	void updateHistory(int srno, History edithistory);
	
	void deleteHistory(int srno);
}
