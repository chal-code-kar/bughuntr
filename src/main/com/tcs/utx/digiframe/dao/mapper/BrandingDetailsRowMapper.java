package com.tcs.utx.digiframe.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tcs.utx.digiframe.dto.BrandingDTO;

public class BrandingDetailsRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int row) throws SQLException {
		BrandingDTO brandingDTO=new BrandingDTO();
		brandingDTO.setUserId(rs.getString("id"));
		brandingDTO.setFirstName(rs.getString("first_name"));
		brandingDTO.setLastName(rs.getString("last_name"));
		brandingDTO.setIcalmsRole(rs.getString("icalms_role"));
		brandingDTO.setProjectName(rs.getString("project_name"));
		brandingDTO.setSupervisorName(rs.getString("supervisor_name"));
		return brandingDTO;
	}

}
