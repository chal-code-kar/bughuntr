package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.Faq;
import com.tcs.utx.digiframe.model.Query;
import com.tcs.utx.digiframe.model.Researcher;
import com.tcs.utx.digiframe.model.Resources;
import com.tcs.utx.digiframe.model.Role;
import com.tcs.utx.digiframe.model.UserRoleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class PermissionHelperDAOImpl implements PermissionHelperDAO {
	private static final Logger LOG = LoggerFactory.getLogger(PermissionHelperDAOImpl.class);

	private static final String ERROR_MSG_1 = " PermissionHelperDAOImpl | Exception in  isOperationPermissible - ";

	private static final String ERROR_MSG_2 = " PermissionHelperDAOImpl | Exception in  getTotalAccess - ";

	private static final String ERROR_MSG_3 = " PermissionHelperDAOImpl | Exception in  AddRole - ";

	private static final String ERROR_MSG_4 = " PermissionHelperDAOImpl | Exception in   getRoleAndPermission - ";

	private static final String ERROR_MSG_5 = " PermissionHelperDAOImpl | Exception in   AddBugBountyRole - ";

	private static final String ERROR_MSG_6 = " PermissionHelperDAOImpl | Exception in  getUserRole - ";

	private static final String ERROR_MSG_7 = " PermissionHelperDAOImpl | Exception in  AllUsersRoles - ";

	private static final String ERROR_MSG_8 = " PermissionHelperDAOImpl | Exception in   getEmployeeDetail - ";

	private static final String ERROR_MSG_9 = " PermissionHelperDAOImpl | Exception in   deleterole - ";

	private static final String ERROR_MSG_10 = " PermissionHelperDAOImpl | Exception in   getData - ";

	private static final String ERROR_MSG_11 = " PermissionHelperDAOImpl | Exception in   getUsers - ";

	private static final String ERROR_MSG_12 = " PermissionHelperDAOImpl | Exception in   getData - ";

	private static final String ERROR_MSG_13 = " PermissionHelperDAOImpl | Exception in  getUsers - ";

	private JdbcTemplate jdbcTemplate;

	private String TEXT_CONCAT = "concat";

	private String TEXT_PERMISSION = "permission";

	private String ROLE_NAME = "role_name";

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<String> isOperationPermissible(int userid) {
		try {
			List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getRoles, userid);
			List<String> accessList = new ArrayList<String>();
			for (Map<String, Object> temp : data) {
				String tempAccess = (String) temp.get("access");
				accessList.add(accessList.size(), tempAccess);
			}
			getTotalAccess();
			return accessList;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_1, e);
			return null;
		}
	}

	@Override
	public Map<String, Object> getTotalAccess() {
		try {
			Map<String, Object> retList = new HashMap<>();

			List<Map<String, Object>> totalRoles = jdbcTemplate.queryForList(BugHuntrQueryConstants.getTotalRoles);

			List<Map<String, Object>> totalPermission = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getTotalPermission);
			List<Map<String, Object>> NA = jdbcTemplate.queryForList(BugHuntrQueryConstants.notAchievable);

			List<String> notAchievable = new ArrayList<>();
			for (Map<String, Object> temp : NA) {
				notAchievable.add((String) temp.get(TEXT_CONCAT));
			}

			List<Map<String, Object>> getModules = jdbcTemplate.queryForList(BugHuntrQueryConstants.getModules);
			List<String> totalModules = new ArrayList<>();
			for (Map<String, Object> temp : getModules) {
				totalModules.add((String) temp.get(TEXT_CONCAT));
			}

			for (Map<String, Object> temp : totalRoles) {
				List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getTotalAccess,
						(String) temp.get(ROLE_NAME));
				Map<String, Object> role_object = new HashMap<>();

				List<String> availablePermission = new ArrayList<>();
				for (Map<String, Object> tempData1 : data) {
					String perm = ((String) tempData1.get(TEXT_CONCAT))
							.concat("_".concat((String) tempData1.get(TEXT_PERMISSION)));
					availablePermission.add(perm);
				}

				for (String module : totalModules) {
					Map<String, Object> object = new HashMap<>();
					object.put("category", module);
					for (Map<String, Object> tempPermission : totalPermission) {
						String NAString = (module.concat("_".concat((String) tempPermission.get(TEXT_PERMISSION))));
						if (notAchievable.contains(NAString)) {
							object.put((String) tempPermission.get(TEXT_PERMISSION), null);
						} else if (availablePermission.contains(NAString)) {
							object.put((String) tempPermission.get(TEXT_PERMISSION), true);
						} else {
							object.put((String) tempPermission.get(TEXT_PERMISSION), false);
						}
					}
					role_object.put(module, object);
				}
				retList.put((String) temp.get(ROLE_NAME), role_object);
			}

			return retList;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_2, e);
			return null;
		}
	}

	@Override
	public void AddRole(Role role) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.insertRole, role.getRoleName(), role.getRoleDescription());
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_3, e);
		}
	}

	@Override
	public Map<String, Object> getRoleAndPermission() {
		try {
			Map<String, Object> retData = new HashMap<>();
			List<Map<String, Object>> totalRoles = jdbcTemplate.queryForList(BugHuntrQueryConstants.getTotalRoles);
			List<Map<String, Object>> totalPermission = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getTotalPermission);
			retData.put("totalRoles", totalRoles);
			retData.put("totalPermission", totalPermission);
			return retData;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_4, e);
			return null;
		}
	}

	@Override
	public void updatePermisions(List<Role> role) {
		try {
			List<Map<String, Object>> NA = jdbcTemplate.queryForList(BugHuntrQueryConstants.notAchievable);
			List<String> notAchievable = new ArrayList<>();
			for (Map<String, Object> temp : NA) {
				notAchievable.add((String) temp.get(TEXT_CONCAT));
			}

			for (Role temp : role) {
				String NAString = temp.getModule()
						.concat("_".concat(temp.getSubmodule().concat("_".concat(temp.getPermission()))));
				if (notAchievable.contains(NAString)) {
					;
				} else if (temp.isStatus()) {
					jdbcTemplate.update(BugHuntrQueryConstants.updatePermission, temp.getRoleName(), temp.getModule(),
							temp.getSubmodule(), temp.getPermission());
				} else {
					jdbcTemplate.update(BugHuntrQueryConstants.deleteRolePermissionExists, temp.getRoleName(),
							temp.getModule(), temp.getSubmodule(), temp.getPermission());
				}

			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_5, e);
		}
	}

	@Override
	public void AddBugBountyRole(int role_bounty_id, String role) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.insertbountyAdminRole, role_bounty_id, role);
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_6, e);
		}
	}

	@Override
	public List<String> getUserRole(int userid) {
		try {
			List<String> roles = new ArrayList<>();
			List<Map<String, Object>> dbroles = jdbcTemplate.queryForList(BugHuntrQueryConstants.getUserRoles, userid);
			for (Map<String, Object> role : dbroles) {
				roles.add((String) role.get(ROLE_NAME));
			}
			return roles;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_7, e);
			return null;

		}
	}

	@Override
	public List<UserRoleDTO> AllUsersRole() {
		try {
			List<UserRoleDTO> roles = new ArrayList<>();
			List<Map<String, Object>> dbroles = jdbcTemplate.queryForList(BugHuntrQueryConstants.AllUsersRole);
			for (Map<String, Object> role : dbroles) {
				UserRoleDTO dto = new UserRoleDTO();
				dto.setEmployeeName((String) role.get("employee_name"));
				dto.setRoles((String) role.get(ROLE_NAME));

				roles.add(dto);
			}
			return roles;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_8, e);
			return null;

		}
	}


	@Override
	public List<Map<String, Object>> getEmployeeDetail(String employeeid) {
		try {
			List<Map<String, Object>> userName=null;
			  userName= jdbcTemplate.queryForList("select * from ssa_eai_hr_emp_basic_dtls where employee_number = ?", employeeid);
		        
		        return userName;
		       
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_9, e);
			return null;

		}
	}

	@Override
	public List<Map<String, Object>> getEmployeeData() {
		try {
			List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getEmployeedetails);
			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_10, e);
			return null;
		}
	}

	@Override
	public void deleterole(int id) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.deleteEmployee, id);
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_11, e);

		}

	}


	@Override
	public Map<String, Object> getData() {
		try {
			Map<String, Object> retData = new HashMap<>();
			List<Map<String, Object>> VulnDetail = jdbcTemplate.queryForList(BugHuntrQueryConstants.VulnCat);
			List<Map<String, Object>> TopProgram = jdbcTemplate.queryForList(BugHuntrQueryConstants.BountyPaid);
			retData.put("VulnCat", VulnDetail);
			retData.put("Program", TopProgram);
			return retData;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_12, e);
			return null;

		}
	}


	@Override
	public List<Map<String, Object>> getUsers() {
		try {
			List<Map<String, Object>> Data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getUsers);
			return Data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_13, e);
			return null;

		}
	}

	@Override
	public String bountyUserExists(int employeeid, int roleid) {
		String exists = "FALSE";		
		int cnt = 0; 
		try {
				cnt = this.jdbcTemplate.queryForObject(BugHuntrQueryConstants.BOUNTY_ROLE_EXISTS, Integer.class,
				new Object[] { Integer.valueOf(employeeid), Integer.valueOf(roleid) });
			
			if (cnt>0) {
				exists = "TRUE";
			}
			return exists;
			
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_13, e);
			return null;

		}
	}
	
	
	
	@Override
	
	public boolean employeeExist(int id)
	{
		List<Boolean> temp = new ArrayList<>();
		temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isActionAllowed, boolean.class,new Object[] {id});
		if(!temp.get(0))
			
		{
			return true;
		}
		
			return false;
		
	}
	
	
	@Override
    public boolean isAuthorized(int emp_id) {
    	List<Boolean> temp = new ArrayList<>();
        temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isAuthorized, boolean.class,new Object[] {emp_id});
        return temp.get(0);
    }
    
	
	@Override
	public boolean isUserPresent(String employee_number) {
		List<Boolean> temp = new ArrayList<>();
		temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isUserPresent, boolean.class,new Object[] {employee_number});
		return temp.get(0);
	}
	
	@Override
	public boolean isEmployeeMatchesWithETLData(String employeeid) {
		List<Boolean> temp = new ArrayList<>();
		temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isEmployeeMatchesWithETLData, Boolean.class,new Object[] {employeeid});
		return temp.get(0);
	}
	

}
