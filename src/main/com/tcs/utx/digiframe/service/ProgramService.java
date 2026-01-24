package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.Employee;
import com.tcs.utx.digiframe.model.joinedEmploye;

public interface ProgramService {

	   public String createProject(Employee detail); 

	   public void joinProject(joinedEmploye join, String pType);

	   public List<Map<String, Object>> getAllProject(int emp_id);
	   
	   public List<joinedEmploye> getjoinedProject();

	   public List<Map<String, Object>> getAllProject2();

	   public Map<String, Object> getdashborardcounts();

	   public List<Map<String, Object>> getProjectById(int id, int emp_id);

	   public int updateProgram(Employee detail, int empid);
	   
	   public Map<String, Object> ResearchersJoined(int id, int emp_id);
		
	   public boolean ifprogramAdmin(int emp_id, int srno);

	   public Map<String, Object> ProgrammeDeatilstatics(int emp_id);

	   public String validateCreateProgramme(Employee detail) ;
		
	   public boolean ifInvited(int emp_id, int srno);
		
	   public boolean ifprogramjoinRequested(int emp_id, int srno);

	   public List<Map<String, Object>> usersRequestedJoin(int id);

	   public String getJoinProgramStatus(int projectid, int researcherid);

	   public void updateJoinStatus(joinedEmploye data, String status);

	   public void checkSuspendedResearchers();
	   
       public List<Map<String, Object>> getMyProject(int emp_id);
       
       public List<Map<String, Object>> getJoinedProjects(int emp_id);

	   public void deleteJobs();

	   public List<Map<String,Object>> getData(int id);

	   public void deleteAnnouncements(int id);
	   

}