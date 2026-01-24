package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Employee;
import com.tcs.utx.digiframe.model.joinedEmploye;

public interface ProgramDAO {

	public String createProject(Employee detail);  
	
	public void joinProject(joinedEmploye join, String pType); 

	public List<Map<String, Object>> getAllProject(int emp_id);
	
	public List<joinedEmploye> getjoinedProject2();
	
	public List<Map<String, Object>> getAllProject2();
	
	public Map<String, Object> getdashborardcounts();

	public List<Map<String, Object>> getProjectById(int id, int emp_id);

	public int updateProgram(Employee detail, int empid);

	public Map<String, Object> ResearchersJoined(int id, int emp_id);
	
	public boolean isProgramJoined(int emp_id, int srno);
	
	public boolean ifprogramAdmin(int emp_id, int srno);
	
	public boolean ifInvited(int emp_id, int srno);
	
	public boolean ifprogramjoinRequested(int emp_id, int srno);

	public Map<String, Object> ProgrammeDeatilstatics(int empid);

	public List<Map<String, Object>> usersRequestedJoin(int id);

	public String getJoinProgramStatus(int projectid, int researcherid);

	public void updateJoinStatus(joinedEmploye data, String status);

	public void checkSuspendedResearchers();
	
    public List<Map<String, Object>> getMyProject(int emp_id);
         
    public List<Map<String, Object>> getJoinedProjects(int emp_id);

	public void deleteAnnouncements(int id);

	public List<Map<String, Object>> getData(int id);

	public void deleteJobs();
}


