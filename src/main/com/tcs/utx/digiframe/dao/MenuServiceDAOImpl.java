package com.tcs.utx.digiframe.dao;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.controller.BrandingDetailsController;
import com.tcs.utx.digiframe.dto.UtxMenuItemServiceVo;
import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.util.SSAComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class MenuServiceDAOImpl implements MenuServiceDAO {

	private static final Logger LOG = LoggerFactory.getLogger(MenuServiceDAOImpl.class);
	
	private String GET_ROLE_NAME = "SELECT role_name FROM b_roles WHERE srno = ? ";

	public String GET_USER_MENUS = " SELECT T.SRNO, T.MENUNAME, T.SEQUENCEORDER, T.PARENTSRNO, T.LINK, T.STATUS, T.ROLE FROM B_MENUS T "
					+ " WHERE T.ROLE ~ ? " 
					+ " AND T.STATUS = 'Active' ORDER BY SEQUENCEORDER ASC ";

	/** The data source. */
	private DataSource dataSource;

	/** The jdbc template. */
	private JdbcTemplate jdbcTemplate;

	/**
	 * Sets the data source.
	 *
	 * @param dataSource the new data source
	 */
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public List getUserMenus(String empNo) throws UserDefinedException {
		List<UtxMenuItemServiceVo> menuList = new ArrayList<>();
		List<Map<String, Object>> rows = null;
		UtxMenuItemServiceVo menu = null;
		List<UtxMenuItemServiceVo> menus = new ArrayList<UtxMenuItemServiceVo>();
		try {
			menuList = new ArrayList<UtxMenuItemServiceVo>();
			
			List<String> userRolesList = this.jdbcTemplate.queryForList(
					"select roleid from b_employee_role_mapping where employee_srno = ?", String.class,
					new Object[] { Integer.parseInt(empNo) });
			
		if(userRolesList.isEmpty()) {
			userRolesList.add(String.valueOf(0));
			
		}
			
			
		for(String role:userRolesList) {
			String temp = "(^|(.*,))" + java.util.regex.Pattern.quote(role) + "((,.*)|$)";
			rows = this.jdbcTemplate.queryForList(GET_USER_MENUS,
					new Object[] { temp });

			for (Map<String, Object> row : rows) {
				menu = new UtxMenuItemServiceVo();
				menu.setId(Integer.parseInt("" + row.get("SRNO")));
				menu.setText((String) row.get("MENUNAME"));
				menu.setOrder_no(Integer.parseInt("" + row.get("SEQUENCEORDER")));
				menu.setParentid(Integer.parseInt("" + row.get("PARENTSRNO")));
				menu.setUrl((String) row.get("LINK"));
				menuList.add(menu);
			}
		}
		for(UtxMenuItemServiceVo temp :menuList) {
			if(!menus.contains(temp)) {
				menus.add(temp);
			}
		}
		} catch (NumberFormatException nfe) {
			LOG.error("MenuServiceDAOImpl | NumberFormatException in getAllMenus - ", nfe);
			throw new UserDefinedException("NumberFormatException in getAllMenus", nfe);
		} catch (DataAccessException dae) {
			LOG.error("MenuServiceDAOImpl | DataAccessException in getAllMenus - ", dae);
			throw new UserDefinedException("DataAccessException in getAllMenus", dae);
		}
		return menus;
	}



	@Override
	public String getRoleName(int roleId) throws UserDefinedException {
		String roleName = "";
		try {
			roleName = this.jdbcTemplate.queryForObject(GET_ROLE_NAME, String.class,
					new Object[] { Integer.valueOf(roleId) });
		} catch (DataAccessException dae) {
			LOG.error("MenuServiceDAOImpl | DataAccessException in getRoleName - ", dae);
			throw new UserDefinedException("DataAccessException in getRoleName", dae);
		}
		return roleName;
	}

}
