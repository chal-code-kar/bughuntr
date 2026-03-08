package com.tcs.utx.digiframe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.controller.BrandingDetailsController;
import com.tcs.utx.digiframe.controller.ResearcherAPI;
import com.tcs.utx.digiframe.dao.ResearcherDAO;
import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.KeyValuePair;
import com.tcs.utx.digiframe.model.Researcher;
import com.tcs.utx.digiframe.util.TSSVStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ResearcherServiceImpl implements ResearcherService {

	private static final Logger LOG = LoggerFactory.getLogger(ResearcherServiceImpl.class);

	@Autowired
	private ResearcherDAO researchDAO;

	@Override
	public void addResearchers(Researcher user) {
		this.researchDAO.addResearchers(user);
	}

	@Override
	public List<Map<String, Object>> getAllhuntHistory() {
		return this.researchDAO.getAllhuntHistory();
	}

	@Override
	public List<Map<String, Object>> getAllReaserchers() {
		return this.researchDAO.getAllReaserchers();
	}

	@Override
	public List<Map<String, Object>> getResearcherById(int id) {
		return this.researchDAO.getResearcherById(id);
	}

	@Override
	public void updateResearchers(Researcher user) {
		this.researchDAO.updateResearchers(user);
	}

	public String validateResearchProgramme(Researcher user) {
		int emp_id = BrandingDetailsController.getUser();
		try {
			ArrayList<KeyValuePair> INFRASTRUCTURE_SKILLS = this.researchDAO.getLookupValues("R_SKILLS");
			ArrayList<String> validSkills = new ArrayList<String>();
			ArrayList<String> validProfiles = new ArrayList<String>();
			validProfiles.add("F1");
			validProfiles.add("F2");
			validProfiles.add("F3");
			validProfiles.add("F4");
			validProfiles.add("F5");
			validProfiles.add("M1");
			validProfiles.add("M2");
			validProfiles.add("M3");
			validProfiles.add("M4");
			validProfiles.add("M5");
			
			for (int i = 0; i < INFRASTRUCTURE_SKILLS.size(); i++) {
				validSkills.add(INFRASTRUCTURE_SKILLS.get(i).getValue());
			}
			if (user.getBio() == null || user.getBio().trim().length() < 5 || user.getBio().trim().length() > 500
					|| !TSSVStringUtil.matchPattern(user.getBio(), TSSVStringUtil.PATTERN_MARKDOWN)) {
				return "Please enter proper BIO";
			}

			else if (!validProfiles.contains((String) user.getProfile_pic())) {
				return "please add proper Profile pic";
			} else if (user.getId() != emp_id) {
				return "You are not authorized to update data for another researcher";
			}

			else if (!user.getSkills().isEmpty()) {
				String a[] = user.getSkills().toString().split(",");
				for (int i = 0; i < a.length; i++) {
					if (!validSkills.contains(a[i])) {
						return "Please enter proper skills";
					}
				}

			}


			else {
				;
			}
		} catch (UserDefinedException e) {
			LOG.error("Exception | Validation Failed in Researcher API", e);
		}
		return null;
	}

	@Override
	public void upload(String imageName, int srno) {
		this.researchDAO.upload(imageName, srno);
	}

	@Override
	public boolean isAvatarAvailable(String avatarName) {
		// Path Traversal fix (defense-in-depth): reject invalid avatar names at service layer
		if (avatarName == null || !avatarName.matches("^[a-zA-Z0-9_-]{1,50}$")) {
			return false;
		}
		return this.researchDAO.isAvatarAvailable(avatarName);
	}

	@Override
	public boolean isActionAllowed(int id) {
		return this.researchDAO.isActionAllowed(id);
	}

	@Override
	public void addDetails(int id) {
		this.researchDAO.addDetails(id);
	}

	@Override
	public boolean isResearcher(int emp_id) {
		return this.researchDAO.isResearcher(emp_id);
	}

	@Override
	public ArrayList<KeyValuePair> getSkills() throws UserDefinedException {

		return researchDAO.getLookupValues("R_SKILLS");
	}

	@Override
	public Researcher getResearcherProfile(int empid) {
		return this.researchDAO.getResearcherProfile(empid);
	}

}
