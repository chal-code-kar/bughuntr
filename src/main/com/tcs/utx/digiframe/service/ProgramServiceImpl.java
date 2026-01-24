package com.tcs.utx.digiframe.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.controller.ProgramAPI;
import com.tcs.utx.digiframe.dao.ProgramDAO;
import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.Announcements;
import com.tcs.utx.digiframe.model.Employee;
import com.tcs.utx.digiframe.model.Jobs;
import com.tcs.utx.digiframe.model.Rules;
import com.tcs.utx.digiframe.model.joinedEmploye;
import com.tcs.utx.digiframe.util.TSSVStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProgramServiceImpl implements ProgramService {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramServiceImpl.class);

	@Autowired
	private ProgramDAO Dao;

	@Autowired
	private ManageOptionsService optionService;

	public String createProject(Employee detail) {
		return this.Dao.createProject(detail);
	}

	public void joinProject(joinedEmploye join, String pType) {
		this.Dao.joinProject(join, pType);
	}

	public List<Map<String, Object>> getAllProject(int emp_id) {
		return this.Dao.getAllProject(emp_id);
	}

	public List<joinedEmploye> getjoinedProject() {
		return this.Dao.getjoinedProject2();
	}

	public List<Map<String, Object>> getAllProject2() {
		return this.Dao.getAllProject2();
	}

	public Map<String, Object> getdashborardcounts() {
		return this.Dao.getdashborardcounts();
	}

	public List<Map<String, Object>> getProjectById(int id, int emp_id) {
		return this.Dao.getProjectById(id, emp_id);
	}

	public int updateProgram(Employee detail, int empid) {
		return this.Dao.updateProgram(detail, empid);
	}

	@Override
	public Map<String, Object> ResearchersJoined(int id, int emp_id) {
		return this.Dao.ResearchersJoined(id, emp_id);
	}

	public String validateCreateProgramme(Employee detail) {

		List<String> array = new ArrayList<>();
		array.add("Critical");
		array.add("High");
		array.add("Medium");
		array.add("Low");

		List<Map<String, Object>> P_vunloptionsList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> P_AnnouncementList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> P_RulesList = new ArrayList<Map<String, Object>>();


		try {
			P_vunloptionsList = this.optionService.getOptions("P_vunloptions");
			P_AnnouncementList = this.optionService.getOptions("P_Announcement");
			P_RulesList = this.optionService.getOptions("P_Rules");
		} catch (UserDefinedException e) {
			LOG.error("ProgramServiceImpl | UserDefinedException in validateCreateProgramme", e);
		}

		if (detail.getProgram_name() == null || "".equals(detail.getProgram_name().trim()) || !TSSVStringUtil
				.matchPattern(detail.getProgram_name(), TSSVStringUtil.PATTERN_ALPHANUMERICSPACE_WITH_UNDERSCORE)) {
			return "Program Name validation failed";
		} else if (detail.getProgram_name().trim().length() > 100) {
			return "Program name can have maximum 100 characters ";
		}

		else if (detail.getEnddate() == null || detail.getStartdate() == null) {
			return "Program Date validation failed ";
		}

		
		else if (detail.getApm_id().length()>0 && (detail.getApm_id().length() >10 || !TSSVStringUtil.matchPattern(detail.getApm_id(), TSSVStringUtil.PATTERN_ALPHANUMERIC))) {
			return "APM id validation failed";
		}

		else if (detail.getDescription() == null || "".equals(detail.getDescription().trim())
				|| !TSSVStringUtil.matchPattern(detail.getDescription(), TSSVStringUtil.PATTERN_MARKDOWN)) {
			return "Description validation failed";
		} else if (detail.getDescription().trim().length() > 10000) {
			return "Description can have maximum 10000 characters ";
		}
		if (detail.getInScope() == null || "".equals(detail.getInScope().trim())
				|| !TSSVStringUtil.matchPattern(detail.getInScope(), TSSVStringUtil.PATTERN_MARKDOWN)) {
			return "Please enter proper Inscope";
		} else if (detail.getInScope().trim().length() > 10000) {
			return "Inscope can have maximum 10000 characters ";
		}

		else if (detail.getOutScope() == null || "".equals(detail.getOutScope().trim())
				|| !TSSVStringUtil.matchPattern(detail.getOutScope(), TSSVStringUtil.PATTERN_MARKDOWN)) {
			return "Please enter proper outscope";
		} else if (detail.getInScope().trim().length() > 10000) {
			return "Outscope can have maximum 10000 characters ";
		}

		else if (!array.contains((String) detail.getCriticality())) {
			return "Please enter proper Criticality";
		}

		else if (detail.getRules().size() == 0) {
			return "Please enter proper number of Rules";
		} else if (detail.getJobs().size() != 4) {
			return "Please enter proper number of Jobs";
		} else if (detail.getTechnology() == null || "".equals(detail.getTechnology().trim())
				|| detail.getTechnology().length() < 4) {
			return "Please enter proper Technology";
		} else if (detail.getResources() == null || "".equals(detail.getResources().trim())
				|| detail.getResources().length() < 4) {
			return "Please enter proper Resources";
		} else if (detail.getAccess() == null || "".equals(detail.getAccess().trim())
				|| detail.getAccess().length() < 4) {
			return "Please enter proper access details";
		} else if (detail.getOverview() == null || "".equals(detail.getOverview().trim())
				|| detail.getOverview().length() < 4) {
			return "Please enter proper overview";
		} else {
			;
		}
		for (Rules rule : detail.getRules()) {

			if (!TSSVStringUtil.checkValidityForDynamicDropdowns(P_RulesList, rule.getRules_category())) {
				return "Please enter proper category";
			}

			else if (rule.getRules_details().trim() == null || "".equals(rule.getRules_details().trim())
					|| !TSSVStringUtil.matchPattern(rule.getRules_details(), TSSVStringUtil.PATTERN_MARKDOWN)) {
				return "Please enter proper rules details";
			} else if (rule.getRules_details().length() > 10000) {
				return "Rules can have maximum 10000 characters ";
			} else {
				;
			}
		}

		for (Jobs job : detail.getJobs()) {
			int indexJobs = TSSVStringUtil.checkValidityForDynamicDropdownsIndex(P_vunloptionsList,
					(String) job.getJobtype());

			if (indexJobs != -1) {
				P_vunloptionsList.remove(indexJobs);
			} else {
				return "Please enter proper vulnetability types details";
			}
			if (job.getReward_type().trim() == null || "".equals(job.getReward_type().trim())
					|| !"GEMS".equals(job.getReward_type())) {
				return "Please enter proper details for Reward Unit";
			} else if (job.getPricing() < 1 || (job.getPricing()+"").length()>8) {
				return "Please enter proper pricing, pricing can not be negative or greater than 8 digits";
			} else if (job.getMaxpricing() < 1 || (job.getMaxpricing()+"").length()>8) {
				return "Please enter proper max pricing, it cannot be negative or greater than 8 digits";
			} else if (job.getMaxpricing() < job.getPricing()) {
				return "Please enter proper pricing, max pricing should not be less that min prizing";
			} else {
				;
			}
		}

		for (Announcements announcement : detail.getAnnouncements()) {

			if (!TSSVStringUtil.checkValidityForDynamicDropdowns(P_AnnouncementList,
					(String) announcement.getAnnouncement_category())) {
				return "Please enter proper catgeory announcment";
			}

			else if (announcement.getAnnouncement_details().trim() == null
					|| "".equals(announcement.getAnnouncement_details().trim()) || !TSSVStringUtil
							.matchPattern(announcement.getAnnouncement_details(), TSSVStringUtil.PATTERN_MARKDOWN)) {
				return "Please enter announcement";
			} else if (announcement.getAnnouncement_details().length() > 10000) {
				return "Announcement can have maximum 10000 characters ";
			} else {
				;
			}

		}

		return null;
	}

	public boolean ifprogramAdmin(int emp_id, int srno) {
		return this.Dao.ifprogramAdmin(emp_id, srno);
	}

	@Override
	public Map<String, Object> ProgrammeDeatilstatics(int empid) {
		return this.Dao.ProgrammeDeatilstatics(empid);
	}

	@Override
	public boolean ifInvited(int emp_id, int srno) {
		return this.Dao.ifInvited(emp_id, srno);
	}

	@Override
	public boolean ifprogramjoinRequested(int emp_id, int srno) {
		return this.Dao.ifprogramjoinRequested(emp_id, srno);
	}

	@Override
	public List<Map<String, Object>> usersRequestedJoin(int id) {
		return this.Dao.usersRequestedJoin(id);
	}

	@Override
	public String getJoinProgramStatus(int projectid, int researcherid) {
		return this.Dao.getJoinProgramStatus(projectid, researcherid);
	}

	@Override
	public void updateJoinStatus(joinedEmploye data, String status) {
		this.Dao.updateJoinStatus(data, status);
	}

	@Override
	public void checkSuspendedResearchers() {
		this.Dao.checkSuspendedResearchers();
	}

	@Override
	public List<Map<String, Object>> getMyProject(int emp_id) {
		return this.Dao.getMyProject(emp_id);
	}

	@Override
	public void deleteAnnouncements(int id) {
		this.Dao.deleteAnnouncements(id);

	}

	@Override
	public List<Map<String, Object>> getData(int id) {
		return this.Dao.getData(id);
	}

	@Override
	public void deleteJobs() {
		this.Dao.deleteJobs();
	}

	@Override
	public List<Map<String, Object>> getJoinedProjects(int emp_id) {
		return this.Dao.getJoinedProjects(emp_id);
	}

}
