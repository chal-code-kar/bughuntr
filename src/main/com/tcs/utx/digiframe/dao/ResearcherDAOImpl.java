package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.KeyValuePair;
import com.tcs.utx.digiframe.model.Researcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ResearcherDAOImpl implements ResearcherDAO {
	private static final Logger LOG = LoggerFactory.getLogger(ResearcherDAOImpl.class);
	
	private static final String ERROR_MSG_1 = "ResearcherDAOImpl | Error in addResearchers - ";
	private static final String ERROR_MSG_2 = "ResearcherDAOImpl | Error in getAllReaserchers - ";
	private static final String ERROR_MSG_3 = "ResearcherDAOImpl | Error in getAllhuntHistory - ";
	private static final String ERROR_MSG_4 = "ResearcherDAOImpl | Error in getResearcherById - ";
	private static final String ERROR_MSG_5 = "ResearcherDAOImpl | Error in Researcher_huntHistory - ";
	private static final String ERROR_MSG_6 = "ResearcherDAOImpl | Error in updateResearchers - ";
	private static final String ERROR_MSG_7 = "ResearcherDAOImpl | Error in upload - ";
	private static final String ERROR_MSG_8 = "ResearcherDAOImpl | Error in isAvatarAvailable - ";
	private static final String ERROR_MSG_9 = "ResearcherDAOImpl | Error in isActionAllowed - ";
	private static final String ERROR_MSG_10 = "ResearcherDAOImpl | Error in addDetails - ";
	private static final String ERROR_MSG_11 = "ResearcherDAOImpl | Error in isResearcher - ";
	
	private String TEXT_EMPID = "emp_id";
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DynamicDropdownDAO dynamicDropdownDAO;
    

    @Override
    public void addResearchers(Researcher user) {
    	try {

        jdbcTemplate.update(BugHuntrQueryConstants.insertResearcherRole, user.getId());

        jdbcTemplate.update(BugHuntrQueryConstants.addResearchers, user.getId(), user.getId(), user.getBio(), user.getSkills(),user.getId(), user.getAvatar(), user.getProfile_pic());
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_1, e);
       }
	}

    @Override
    public List<Map<String, Object>> getAllReaserchers() {
    	try {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getAllReaserchers);
        return data;
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_2, e);
        return null;
    }
	}


    @Override
    public List<Map<String, Object>> getAllhuntHistory() {
    	try {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getAllhuntHistory);
        return data;
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_3, e);
        return null;
    }
	}

    @Override
    public List<Map<String, Object>> getResearcherById(int id) {
    	try {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getResearcherById, id);
        List<Map<String, Object>> total_bounty_earned = jdbcTemplate.queryForList(BugHuntrQueryConstants.getBountyForID, data.get(0).get(TEXT_EMPID));
        List<Map<String, Object>> particular_hunt_history = jdbcTemplate.queryForList(BugHuntrQueryConstants.particular_hunt_history, data.get(0).get(TEXT_EMPID));
            
        data.get(0).put("total_bounty_earned", total_bounty_earned.get(0).get("total_bounty_earned"));
        data.get(0).put("particular_hunt_history",particular_hunt_history);
        data.get(0).put("detailed_particular_huntHistory",Researcher_huntHistory((int) data.get(0).get(TEXT_EMPID)));
        return data;
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_4, e);
        return null;
    }
	}

    @Override
    public List<Map<String, Object>> Researcher_huntHistory(int empid) {
    	try {
        List<Map<String, Object>> detailed_particular_huntHistory = jdbcTemplate.queryForList(BugHuntrQueryConstants.detailed_particular_huntHistory, empid);
        return detailed_particular_huntHistory;
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_5, e);
        return null;
    }
	}

    @Override
    public void updateResearchers(Researcher user) {
    	try {
        jdbcTemplate.update(BugHuntrQueryConstants.updateResearchers, user.getBio(), user.getSkills(), user.getProfile_pic(), user.getSrno()); 
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_6, e);
        
    }
	}

    @Override
    public void upload(String imageName, int srno) {
    	try {
        jdbcTemplate.update(BugHuntrQueryConstants.updateResearcherPicture, imageName, srno); 
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_7, e);
        
    }
	}

	@Override
	public boolean isAvatarAvailable(String avatarName) {
		try {
		List<Boolean> temp = new ArrayList<>();
		temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isAvatarAvailable, boolean.class, new Object[] { avatarName.toUpperCase() });
		return !temp.get(0);
	}catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_8, e);
        return false;
    }
	}
	
	
	@Override
    public boolean isActionAllowed(int id) {
		try {
        List<Boolean> temp = new ArrayList<>();
        temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isActionAllowed, boolean.class,new Object[] {id});
        return temp.get(0);
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_9, e);
        return false;
    }
	}

    @Override
    public void addDetails(int id) {
    	try {
        List<Boolean> temp = new ArrayList<>();
        temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isActionAllowed, boolean.class,new Object[] {id});
        if(!temp.get(0)) {
            jdbcTemplate.update(BugHuntrQueryConstants.insertUser,id,id,id,id);
            jdbcTemplate.update(BugHuntrQueryConstants.whitelistUser,id);
        }

    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_10, e);
        
    }
	}
    
    
    @Override
    public boolean isResearcher(int emp_id) {
    	try {
        List<Boolean> temp = new ArrayList<>();
        temp = this.jdbcTemplate.queryForList(BugHuntrQueryConstants.isResearcher, boolean.class,new Object[] {emp_id});
        return temp.get(0);
    }
     catch(DataAccessException e) {
	LOG.error(ERROR_MSG_11, e);
    return false;
}
    }
    
    
    @Override
	public ArrayList<KeyValuePair> getLookupValues(String lookupGroup) throws UserDefinedException {

		return dynamicDropdownDAO.getKeyValueListFromDB(BugHuntrQueryConstants.GET_LOOKUP_VALUES, new Object[] { lookupGroup });

	}
    
    
    @Override
	public Researcher getResearcherProfile(int empid) {
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(BugHuntrQueryConstants.getResearcherProfileByEmpId, empid);
			Researcher researcherDTO = new Researcher();
			for (Map<String, Object> row : rows) {
				researcherDTO.setProfile_pic(String.valueOf(row.get("profile_pic")));
				researcherDTO.setSrno(Integer.parseInt(""+row.get("srno")));
			}
			return researcherDTO;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_4, e);
			return null;
		}
	}
}
