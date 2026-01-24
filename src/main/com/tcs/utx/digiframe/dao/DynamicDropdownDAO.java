package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.KeyValuePair;

public interface DynamicDropdownDAO {

	

    public List<Map<String, Object>> getAllOptions();
    ArrayList<KeyValuePair> getKeyValueListFromDB(String queryString, Object[] params) throws UserDefinedException;

}
