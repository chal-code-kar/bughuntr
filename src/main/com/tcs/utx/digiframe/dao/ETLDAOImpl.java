package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.Etldata;

@Repository
public class ETLDAOImpl implements ETLDAO {
	
	
	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> ETLData() {
		
		List<Map<String, Object>> data = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.ETLquey);
	    return data;
	}

	@Override
	public void Postetldata(Etldata postetldata) {
		jdbcTemplate.update(BugHuntrQueryConstants.PostEtl, postetldata.getEmployee_number(), postetldata.getFirst_name(), postetldata.getLast_name(),postetldata.getFull_name(),postetldata.getEmail_address(),postetldata.getIou_name(),postetldata.getAssignment_id(),postetldata.getAssignment_status(),postetldata.getBase_branch(),postetldata.getDepute_branch(),postetldata.getCountry_of_depute(),postetldata.getBase_dc_id(),postetldata.getPer_system_status());
	}

	
	}
	 

