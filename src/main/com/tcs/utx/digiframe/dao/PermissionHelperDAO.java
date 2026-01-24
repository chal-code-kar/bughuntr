package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Faq;
import com.tcs.utx.digiframe.model.Query;
import com.tcs.utx.digiframe.model.Researcher;
import com.tcs.utx.digiframe.model.Resources;
import com.tcs.utx.digiframe.model.Role;
import com.tcs.utx.digiframe.model.UserRoleDTO;

public interface PermissionHelperDAO {

    public List<String> isOperationPermissible(int userid);

    public Map<String, Object> getTotalAccess();
    
    public void AddRole(Role role);

    public Map<String, Object> getRoleAndPermission();
    
    public void updatePermisions(List<Role> role);

    public void AddBugBountyRole(int role_bounty_id, String role);

    public List<String> getUserRole(int userid);
    
    public List<UserRoleDTO> AllUsersRole();

    public List<Map<String, Object>> getEmployeeDetail(String employeeid);
    
    
    public List<Map<String, Object>> getEmployeeData();

    public String bountyUserExists(int employeeid, int roleid);

    public void deleterole(int id);
    public boolean employeeExist(int id);
    
    public boolean isAuthorized(int emp_id);

    public Map<String, Object> getData();
    
    public List<Map<String, Object>> getUsers();
    
    public boolean isUserPresent(String employee_number);

	boolean isEmployeeMatchesWithETLData(String employeeid);


}
