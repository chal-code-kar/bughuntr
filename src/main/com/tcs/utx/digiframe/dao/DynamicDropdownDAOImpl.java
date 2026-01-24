package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.KeyValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class DynamicDropdownDAOImpl implements DynamicDropdownDAO {
	private static final Logger LOG = LoggerFactory.getLogger(DynamicDropdownDAOImpl.class);

	private static final String ERROR_MSG_1 = "DynamicDropdownDAOImp | Exception in getAllOptions - ";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Map<String, Object>> getAllOptions() {
		try {
			List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.GET_ALL_OPTIONS);
			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_1, e);
			return null;

		}
	}
	
	
	@Override
	public ArrayList<KeyValuePair> getKeyValueListFromDB(String queryString, Object[] params)
			throws UserDefinedException {

		ArrayList<KeyValuePair> retLst = new ArrayList<KeyValuePair>();

		try {

			List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(queryString, params);

			for (Map<String, Object> row : rows) {
			KeyValuePair ptKeyValuePairDTO = new KeyValuePair();

				ptKeyValuePairDTO.setKey(String.valueOf(row.get("lookup_key")));
				ptKeyValuePairDTO.setValue(String.valueOf(row.get("lookup_value")));

				retLst.add(ptKeyValuePairDTO);
			}

		} catch (DataAccessException dae) {
			LOG.error("DataAccessException occurred");
		}

		return retLst;
	}

}
