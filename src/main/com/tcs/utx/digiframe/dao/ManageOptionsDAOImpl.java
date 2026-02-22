package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.DynamicDropdown;

@Repository
public class ManageOptionsDAOImpl implements ManageOptionsDAO{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
	@Override
	public List<Map<String, Object>> getOptions(String category) throws UserDefinedException {
		List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getOptions, category);
		return data;
	}
    
	@Override
	public List<Map<String, Object>> getVulnCat() throws UserDefinedException {
		List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getVulnCat);
		return data;
	}

	@Override
	public int AddDropdown(DynamicDropdown adddropdown, int emp_id) throws UserDefinedException {	
		int insertCnt=0;
		try {
		 String emp = Integer.toString(emp_id);
		 adddropdown.setCreatedby(emp);
		 insertCnt=jdbcTemplate.update("INSERT INTO bb_lookup_data(id, lookupgroup, lookupvalue, lookupdescription,lookupcode, createdby,orderseq) VALUES((SELECT MAX(id) FROM bb_lookup_data) + 1, ?, ?, ?, (SELECT COUNT(lookupcode) FROM bb_lookup_data) + 1, ?, (SELECT COUNT(id) FROM bb_lookup_data WHERE lookupgroup = ?) + 1)",
				 adddropdown.getLookupgroup(), adddropdown.getLookupvalue(), adddropdown.getLookupdescription(), emp, adddropdown.getLookupgroup());
		}
		catch(DataAccessException e)
		{
			throw new UserDefinedException("DataAccessException in addOption", e);
		}
		return insertCnt;
	}

	@Override
	public void deleteDropdown(int id) {
		 jdbcTemplate.update(BugHuntrQueryConstants.DELETE_OPTION,id);
		
	}

	@Override
	public int updateDropdown(int id, DynamicDropdown updateDropdown, int emp_id) throws UserDefinedException {
		int insertCnt=0;
		try {
			insertCnt=jdbcTemplate.update("UPDATE bb_lookup_data SET lookupvalue = ?, lookupdescription = ?, updatedby = ?, updateddt = NOW() WHERE id = ? AND active = true",
					updateDropdown.getLookupvalue(), updateDropdown.getLookupdescription(), emp_id, id);
		}
		catch(DataAccessException e)
		{
			throw new UserDefinedException("DataAccessException in addOption", e);
		}
		return insertCnt;
	}

	@Override
	public List<Map<String, Object>> GetAllDropdown() {
		List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.GET_ALL_OPTIONS);
		return data;
	}

	@Override
	public List<Map<String, Object>> GetDropdown() {
		List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.GETDROPDOWNS);
		return data;
	}
	
	
	
	@Override
	public boolean checkExistsInOptions(DynamicDropdown adddropdown) throws UserDefinedException {
		List<Map<String, Object>> rows = null;

		try {

			rows = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.BUGHUNTR_OPTION_EXISTS.toString(),
					new Object[] { adddropdown.getLookupvalue(), adddropdown.getLookupgroup(),adddropdown.getLookupdescription() });

			if (rows.size() > 0) {
				return true;
			}

		} catch (NumberFormatException e) {
			throw new UserDefinedException("Exception in checkExistsInOptions", e);
		} catch (DataAccessException e) {
			throw new UserDefinedException("Exception in checkExistsInOptions", e);
		}

		return false;
	}
}