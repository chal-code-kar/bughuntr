package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Faq;
import com.tcs.utx.digiframe.model.Query;
import com.tcs.utx.digiframe.model.Researcher;
import com.tcs.utx.digiframe.model.Resources;
import com.tcs.utx.digiframe.model.Role;
import com.tcs.utx.digiframe.model.UserRoleDTO;

public interface PermissionHelperService {

    public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int programid, int projectid);

    public Map<String, Object> getTotalAccess();
    
    public void AddRole(Role role);

    public Map<String, Object> getRoleAndPermission();
    
    public void updatePermisions(List<Role> role);

    public void AddBugBountyRole(int role_bounty_id, String role);
    
    public List<UserRoleDTO> AllUsersRole();
    
    public List<Map<String, Object>> getEmployeeDetail(String employeeid);
        
    public String bountyUserExists(int employeeid, int roleid); 
    
    public List<Map<String, Object>> getEmployeeData();



    public void deleterole(int id);
    
    public boolean isAuthorized(int emp_id);

    public Map<String, Object> getData(); 

    public List<Map<String, Object>> getUsers();
    
    public boolean isUserPresent(String employee_number);

	  public boolean isEmployeeMatchesWithETLData(String employeeid);
    
    


}