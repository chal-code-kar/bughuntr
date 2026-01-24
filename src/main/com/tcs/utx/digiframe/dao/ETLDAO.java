package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Etldata;

public interface ETLDAO {

	public List<Map<String, Object>> ETLData();

	public void Postetldata(Etldata postetldata);



}
