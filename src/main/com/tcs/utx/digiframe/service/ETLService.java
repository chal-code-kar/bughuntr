package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Etldata;

public interface ETLService {

	public List<Map<String, Object>> ETLData();

	public void Postetldata(Etldata postetldata);

	public boolean validateETLPost(Etldata postetldata);

	


}
