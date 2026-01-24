package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.KeyValuePair;
import com.tcs.utx.digiframe.model.Researcher;

public interface ResearcherDAO {
	
    public void addResearchers(Researcher user);
    
    public List<Map<String, Object>> getAllReaserchers();

    public List<Map<String, Object>> getAllhuntHistory();

    public List<Map<String, Object>> Researcher_huntHistory(int empid);
    
    public List<Map<String, Object>> getResearcherById(int id);
    
    public void updateResearchers(Researcher user);

    public void upload(String imageName, int srno);

    public boolean isAvatarAvailable(String avatarName);

    public boolean isActionAllowed(int id);

    public void addDetails(int id);
    
    public boolean isResearcher(int emp_id);
    
    ArrayList<KeyValuePair> getLookupValues(String lookupGroup) throws UserDefinedException;
    
    public Researcher getResearcherProfile(int empid);
}
