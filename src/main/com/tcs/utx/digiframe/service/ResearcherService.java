package com.tcs.utx.digiframe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.KeyValuePair;
import com.tcs.utx.digiframe.model.Researcher;

public interface ResearcherService {

	public void addResearchers(Researcher user);
    
    public List<Map<String, Object>> getAllReaserchers();

    public List<Map<String, Object>> getAllhuntHistory();
    
    public List<Map<String, Object>> getResearcherById(int id);
    
    public void updateResearchers(Researcher user);

    public String validateResearchProgramme(Researcher user);

    public void upload(String imageName, int srno);

    public boolean isAvatarAvailable(String avatarName);
    
    public boolean isActionAllowed(int id);

    public void addDetails(int id);
    
    public boolean isResearcher(int emp_id);
    
	public ArrayList<KeyValuePair> getSkills() throws UserDefinedException;
	
	public Researcher getResearcherProfile(int empid);
}
