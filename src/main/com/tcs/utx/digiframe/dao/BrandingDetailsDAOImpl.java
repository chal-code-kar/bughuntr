package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.controller.BrandingDetailsController;
import com.tcs.utx.digiframe.dto.BrandingDTO;
import com.tcs.utx.digiframe.exception.UserDefinedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class BrandingDetailsDAOImpl implements BrandingDetailsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(BrandingDetailsController.class);
	public JdbcTemplate jdbcTemplateRead;

	@Autowired
	public void setDataSourceRead(DataSource dataSource) {

		this.jdbcTemplateRead = new JdbcTemplate(dataSource);
	}

	@Override
	public BrandingDTO getBrandingDetails(String userId) {

		BrandingDTO brandingDTO = new BrandingDTO();
		try {
			String userName = this.jdbcTemplateRead.queryForObject(
					"SELECT full_name FROM ssa_eai_hr_emp_basic_dtls WHERE employee_number = ? :: text",String.class,
					new Object[] { Integer.valueOf(userId) });

			if (null != userName) {
				brandingDTO.setFirstName(userName);
			}
			brandingDTO.setUserId(userId);

		} catch (DataAccessException e) {
			LOG.error("BrandingDetailsDAOImpl | DataAccessException in getBrandingDetails", e);
		}
		return brandingDTO;
	}

	@Override
	public List<String> getUserRoles(String empNo) throws UserDefinedException{
		LOG.debug("UserSpecificDetailsDaoImpl | getUserRoles Begin");
		List<String> userRolesList = new ArrayList<>();
		List<String> rolesList = new ArrayList<>();
		try {
			userRolesList = this.jdbcTemplateRead.queryForList(
					"SELECT (select role_name from b_roles where srno = roleid)" +
					" FROM b_employee_role_mapping WHERE employee_srno = ?",String.class,
					new Object[] { Integer.parseInt(empNo) });
			
			if (!userRolesList.isEmpty()) {
				for(String team:userRolesList) {
					rolesList.add("ROLE_".concat(team));
				}
			} else {
				rolesList.add("ROLE_Guest");
			}
		} catch (DataAccessException dae) {
			LOG.error("UserSpecificDetailsDaoImpl | DataAccessException in getUserRoles - ", dae);
			throw new UserDefinedException("DataAccessException in getUserRoles", dae);
		}
		return rolesList;
	}

}
