package com.tcs.utx.digiframe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tcs.utx.digiframe.dao.PermissionHelperDAO;
import com.tcs.utx.digiframe.dao.ProgramDAO;
import com.tcs.utx.digiframe.dao.VulnerabilityDAO;
import com.tcs.utx.digiframe.model.Faq;
import com.tcs.utx.digiframe.model.Query;
import com.tcs.utx.digiframe.model.Researcher;
import com.tcs.utx.digiframe.model.Resources;
import com.tcs.utx.digiframe.model.Role;
import com.tcs.utx.digiframe.model.TempAccess;
import com.tcs.utx.digiframe.model.UserRoleDTO;
import com.tcs.utx.digiframe.util.TSSVStringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PermissionHelperServiceImpl implements PermissionHelperService {

	@Autowired
	private PermissionHelperDAO dao;

	@Autowired
	private ProgramDAO empdao;

	@Autowired
	private VulnerabilityDAO vulnDao;

	@Override
	public boolean isOperationPermissible(String module, String submodule, String Perm_Type, int userid, int vulnid,
			int projectid) {
		List<String> data = this.dao.isOperationPermissible(userid);
		String tobeChecked = (module.concat("_" + submodule)).concat("_" + Perm_Type);
		if (data.size() == 0) {
			data.add("Visitor_Visitor_View");
		}
		boolean retData = false;
		if (data.contains(tobeChecked)) {
			retData = true;
		}
		if (data.contains(tobeChecked.concat("_If_Joined"))) {
			retData = retData || this.empdao.isProgramJoined(userid, projectid);
		}
		if (data.contains(tobeChecked.concat("_If_Reported"))) {
			retData = retData || this.vulnDao.isVulnReported(userid, vulnid);
		}
		if (data.contains(tobeChecked.concat("_If_Bounty_Admin"))) {
			retData = retData || this.empdao.ifprogramAdmin(userid, projectid);
		}
		if (data.contains(tobeChecked.concat("_If_Invited"))) {
			retData = retData || this.empdao.ifInvited(userid, projectid);
		}
		if (data.contains(tobeChecked.concat("_If_Temp_Access"))) {
			retData = retData || this.vulnDao.hasTempAccess(userid, vulnid);
		}
		if (data.contains(tobeChecked.concat("_If_Requested"))) {
			retData = retData || this.empdao.ifprogramjoinRequested(userid, vulnid);
		}
		return retData;
	}

	@Override
	public Map<String, Object> getTotalAccess() {
		return this.dao.getTotalAccess();
	}

	@Override
	public void AddRole(Role role) {
		this.dao.AddRole(role);
	}

	@Override
	public Map<String, Object> getRoleAndPermission() {
		return this.dao.getRoleAndPermission();

	}

	@Override
	public void updatePermisions(List<Role> role) {
		this.dao.updatePermisions(role);
	}

	@Override
	public void AddBugBountyRole(int role_bounty_id, String role) {
		this.dao.AddBugBountyRole(role_bounty_id, role);
	}

	@Override
	public List<UserRoleDTO> AllUsersRole() {
		return this.dao.AllUsersRole();
	}

	@Override
    public List<Map<String, Object>> getEmployeeDetail(String employeeid) {
		return this.dao.getEmployeeDetail(employeeid);
	}

	
	@Override
	public boolean isAuthorized(int emp_id) {
		return this.dao.isAuthorized(emp_id);
	}
	
	@Override
    public List<Map<String, Object>> getEmployeeData() {
        return this.dao.getEmployeeData();
    }



   @Override
    public void deleterole(int id) {
        this.dao.deleterole(id);
        
    }
    

   @Override
    public Map<String, Object> getData() {
        return this.dao.getData();
    }
   
   
   
	@Override
    public List<Map<String, Object>> getUsers() {
        return this.dao.getUsers();
    }

	@Override
	public String bountyUserExists(int employeeid, int roleid) {
		 return this.dao.bountyUserExists(employeeid, roleid);
	}
	
	@Override
	public boolean isUserPresent(String employee_number) {
		return this.dao.isUserPresent(employee_number);

	}

	@Override
	public boolean isEmployeeMatchesWithETLData(String employeeid) {
		return this.dao.isEmployeeMatchesWithETLData(employeeid);

	}
	
}
