package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.exception.UserDefinedException;
import com.tcs.utx.digiframe.model.Announcements;
import com.tcs.utx.digiframe.model.Employee;
import com.tcs.utx.digiframe.model.Jobs;
import com.tcs.utx.digiframe.model.Rules;
import com.tcs.utx.digiframe.model.joinedEmploye;
import com.tcs.utx.digiframe.service.BrandingDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Repository
public class ProgramDAOImpl implements ProgramDAO {

	private static final Logger LOG = LoggerFactory.getLogger(VulnerabilityDAOImpl.class);

	private static final String ERROR_MSG_1 = "ProgramDAOImpl | Exception in createProject : ";

	private static final String ERROR_MSG_2 = "ProgramDAOImpl | Exception in getallProject : ";
	private static final String ERROR_MSG_3 = "ProgramDAOImpl | Exception in ifProjectJoined : ";
	private static final String ERROR_MSG_4 = "ProgramDAOImpl | Exception in getallProject2 : ";
	private static final String ERROR_MSG_5 = "ProgramDAOImpl | Exception in checkProject : ";
	private static final String ERROR_MSG_6 = "ProgramDAOImpl | Exception in  updateproject: ";
	private static final String ERROR_MSG_7 = "ProgramDAOImpl | Exception in  insert_invited_researcher: ";
	private static final String ERROR_MSG_8 = "ProgramDAOImpl | Exception in  addjoinedprogramme: ";

	private static final String ERROR_MSG_9 = "ProgramDAOImpl | Exception in  getjoinedprogram: ";
	private static final String ERROR_MSG_10 = "ProgramDAOImpl | Exception in  getdashboardcount: ";
	private static final String ERROR_MSG_11 = "ProgramDAOImpl | Exception in  resesrcher joined: ";
	private static final String ERROR_MSG_12 = "ProgramDAOImpl | Exception in  ifprogramAdmin: ";
	private static final String ERROR_MSG_13 = "ProgramDAOImpl | Exception in  ifprogramjoinedrequest: ";
	private static final String ERROR_MSG_14 = "ProgramDAOImpl | Exception in  ifInvited ";
	private static final String ERROR_MSG_15 = "ProgramDAOImpl | Exception in ProgrammeDeatilstatics ";
	private static final String ERROR_MSG_16 = "ProgramDAOImpl | Exception in  usersRequestedJoin ";
	private static final String ERROR_MSG_17 = "ProgramDAOImpl | Exception in  getJoinProgramStatus ";
	private static final String ERROR_MSG_18 = "ProgramDAOImpl | Exception in  updateJoinStatus";
	private static final String ERROR_MSG_19 = "ProgramDAOImpl | Exception in   checkSuspendedResearchers";
	private static final String ERROR_MSG_20 = "ProgramDAOImpl | Exception in getMyProject";
	private static final String ERROR_MSG_21 = "ProgramDAOImpl | Exception in  getJoinedProjects";

	private String TEXT_PRIVATE = "Private";

	private String TEXT_JOB_PRICING = "jobpricing";

	private String TEXT_IS_PRG_JOINED = "isprogramjoined";

	private String TEXT_COUNT = "count";

	private String TEXT_VULN_REWARDED = "vulnRewarded";

	private String TEXT_APPROVED_PER = "approvedPercentage";

	private String TEXT_PTYPE = "ptype";

	private String TEXT_INVITE = "invitedResearcher";

	private String TEXT_OTHER = "otherDeatils";

	private String TEXT_JOINPROGRAM = "isProgramJoinRequested";

	private String TEXT_REPORTED = "isReported";
	
	private String ROLE_ADMINISTRATOR = "ROLE_Administrator";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BrandingDetailsService brandingService;

	
	@Autowired
	private VulnerabilityDAO vulndao;

	@Autowired
	public void setDataSource(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public String createProject(Employee detail) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.createProject, detail.getProgram_name(), detail.getOwner_empid(),
					detail.getOwner_empid(), detail.getEnddate(), detail.getApi(),
					detail.getTechnology(), detail.getDescription(), detail.getCriticality(), detail.getOwner_empid(),
					detail.getStaticpage(), detail.getDynamicpage(), detail.getRoles(), detail.getPtype(),
					detail.getApm_id());

			List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getLatestProjectSrNo);

			jdbcTemplate.update(BugHuntrQueryConstants.createProjectChild, data.get(0).get("max"), detail.getOverview(),
					detail.getAccess(), detail.getResources());

			jdbcTemplate.update(BugHuntrQueryConstants.createProjectScopeChild, data.get(0).get("max"),
					detail.getInScope(), detail.getOutScope());

			for (Jobs temp : detail.getJobs()) {
				jdbcTemplate.update(BugHuntrQueryConstants.createProjectJobChild, data.get(0).get("max"),
						temp.getJobtype(), temp.getPricing(), temp.getMaxpricing(), temp.getReward_type());
			}

			for (Rules temp : detail.getRules()) {
				jdbcTemplate.update(BugHuntrQueryConstants.createProjectruleChild, data.get(0).get("max"),
						temp.getRules_category(), temp.getRules_details());
			}

			for (Announcements temp : detail.getAnnouncements()) {
				jdbcTemplate.update(BugHuntrQueryConstants.createProjectannouncementChild, data.get(0).get("max"),
						temp.getAnnouncement_category(), temp.getAnnouncement_details());
			}
			if (TEXT_PRIVATE.equals(detail.getPtype())) {
				for (Integer temp : detail.getResearcherList()) {
					jdbcTemplate.update(BugHuntrQueryConstants.insert_invited_researcher, data.get(0).get("max"), temp);
				}
			}

			jdbcTemplate.update(BugHuntrQueryConstants.insertProgramAction, data.get(0).get("max"), "Created",
					detail.getOwner_empid());

			return null;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_1, e);
			return e.getMessage();
		}
	}

	@Override
	public List<Map<String, Object>> getAllProject(int emp_id) {
		try {
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			
			boolean isAdmin = false;
			List<String> userRoles = brandingService.getUserRoles(emp_id+"");
			if (userRoles.contains(ROLE_ADMINISTRATOR)) {
				isAdmin = true;
			}
			if(!isAdmin) {
				data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getAllProject);
				if (data.size() != 0) {
					jdbcTemplate.update(BugHuntrQueryConstants.updatestatus);
				}
			}else {
				data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getAllProjectForAdmin);
				if (data.size() != 0) {
					jdbcTemplate.update(BugHuntrQueryConstants.updatestatus);
				}
			}
			
			
			for (Map<String, Object> temp : data) {

				int srno = (int) temp.get("srno");
				if (TEXT_PRIVATE.equals((String) temp.get(TEXT_PTYPE))) {
					List<Map<String, Object>> invitedResearcher = jdbcTemplate
							.queryForList(BugHuntrQueryConstants.get_invited_researcher, srno);
					temp.put(TEXT_INVITE, invitedResearcher);
				}

				List<Map<String, Object>> jobdata = jdbcTemplate.queryForList(BugHuntrQueryConstants.getProjectJobsbyid,
						srno);
				List<Map<String, Object>> otherdetails = jdbcTemplate
						.queryForList(BugHuntrQueryConstants.getProjectotherbyid, srno);
				temp.put(TEXT_JOB_PRICING, jobdata);
				temp.put(TEXT_OTHER, otherdetails);

				temp.put(TEXT_IS_PRG_JOINED, isProgramJoined(emp_id, srno));
				temp.put(TEXT_JOINPROGRAM, ifprogramjoinRequested(emp_id, srno));

			}
			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_2, e);
			return null;
		} 

	}

	@Override
	public boolean isProgramJoined(int emp_id, int srno) {
		try {
			List<Map<String, Object>> isprogramjoined = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.ifprogramsjoined, emp_id, srno);
			if (isprogramjoined.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_3, e);
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> getAllProject2() {
		try {
			List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getAllProject2);

			for (Map<String, Object> temp : data) {
				int srno = (int) temp.get("srno");
				List<Map<String, Object>> jobdata = jdbcTemplate.queryForList(BugHuntrQueryConstants.getProjectJobsbyid,
						srno);
				temp.put(TEXT_JOB_PRICING, jobdata);
			}
			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_4, e);
			return null;
		}

	}

	public List<Map<String, Object>> getProjectById(int id, int emp_id) {
		try {
			List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getProjectById, id);
			List<Map<String, Object>> jobdata = jdbcTemplate.queryForList(BugHuntrQueryConstants.getProjectJobsbyid,
					id);
			List<Map<String, Object>> scopedata = jdbcTemplate.queryForList(BugHuntrQueryConstants.getProjectscopebyid,
					id);

			List<Integer> invitedEmp = new ArrayList<>();
			List<Map<String, Object>> announcementdata = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getProjectannouncementbyid, id);

			List<Map<String, Object>> rulesdata_reward = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getProjectrulebyid, id, "Rewards");
			List<Map<String, Object>> rulesdata_disclosure = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getProjectrulebyid, id, "Disclosure");
			List<Map<String, Object>> rulesdata_general = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getProjectrulebyid, id, "General");
			List<Map<String, Object>> rulesdata_behaviour = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getProjectrulebyid, id, "Behaviour");
			List<Map<String, Object>> jobdetialsdata = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getProjectDetilasJobsbyid, id);
			Map<String, Object> rules = new HashMap<>();

			rules.put("Rewards", rulesdata_reward);
			rules.put("Disclosure", rulesdata_disclosure);
			rules.put("General", rulesdata_general);
			rules.put("Behaviour", rulesdata_behaviour);

			data.get(0).put(TEXT_JOB_PRICING, jobdata);
			data.get(0).put("scope", scopedata);
			data.get(0).put("rules", rules);
			data.get(0).put("Announcement", announcementdata);
			data.get(0).put("jobdetialsdata", jobdetialsdata);

			List<Map<String, Object>> isprogramjoined = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.ifprogramsjoined, emp_id, id);
			if (isprogramjoined.size() > 0) {
				data.get(0).put(TEXT_IS_PRG_JOINED, true);
			} else {
				data.get(0).put(TEXT_IS_PRG_JOINED, false);
			}

			data.get(0).put("TEXT_IS_PRG_JOINED", isProgramJoined(emp_id, id));

			data.get(0).put(TEXT_JOINPROGRAM, ifprogramjoinRequested(emp_id, id));

			if (TEXT_PRIVATE.equals((String) data.get(0).get(TEXT_PTYPE))) {
				List<Map<String, Object>> invitedResearcher = jdbcTemplate
						.queryForList(BugHuntrQueryConstants.get_invited_researcher, id);
				data.get(0).put(TEXT_INVITE, invitedResearcher);
				for (Map<String, Object> temp : invitedResearcher) {
					invitedEmp.add((int) temp.get("srno"));
				}
			}

			data.get(0).put("invitedResearcherID", invitedEmp);

			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_5, e);
			return null;
		}
	}

	public int updateProgram(Employee detail, int empid) {
		int insertCount = -1;
		try {

			insertCount = jdbcTemplate.update(BugHuntrQueryConstants.updateProgram, detail.getEnddate(),
					detail.getApi(), detail.getTechnology(), detail.getDescription(), detail.getCriticality(),
					detail.getStaticpage(), detail.getDynamicpage(), detail.getRoles(), detail.getPtype(),
					detail.getSrno());

			if (insertCount > 0) {
				jdbcTemplate.update(BugHuntrQueryConstants.updateProjectChild, detail.getOverview(), detail.getAccess(),
						detail.getResources(), detail.getSrno());
				jdbcTemplate.update(BugHuntrQueryConstants.updateProjectScopeChild, detail.getInScope(),
						detail.getOutScope(), detail.getSrno());
				jdbcTemplate.update(BugHuntrQueryConstants.updateProjectJobChild, detail.getSrno());

				for (Jobs temp : detail.getJobs()) {
					jdbcTemplate.update(BugHuntrQueryConstants.createProjectJobChild, detail.getSrno(),
							temp.getJobtype(), temp.getPricing(), temp.getMaxpricing(), temp.getReward_type());
				}

				jdbcTemplate.update(BugHuntrQueryConstants.updateProjectruleChild, detail.getSrno());
				for (Rules temp : detail.getRules()) {
					jdbcTemplate.update(BugHuntrQueryConstants.createProjectruleChild, detail.getSrno(),
							temp.getRules_category(), temp.getRules_details());
				}

				for (Announcements temp : detail.getAnnouncements()) {
					List<Map<String, Object>> data = jdbcTemplate.queryForList(
							BugHuntrQueryConstants.checkAnnouncementsChild, temp.getSrno(), detail.getSrno());
					if (!(data.size() > 0)) {
						jdbcTemplate.update(BugHuntrQueryConstants.createProjectannouncementChild, detail.getSrno(),
								temp.getAnnouncement_category(), temp.getAnnouncement_details());
					}
				}

				if (TEXT_PRIVATE.equals(detail.getPtype())) {
					jdbcTemplate.update(BugHuntrQueryConstants.delete_invited_researcher, detail.getSrno());
					
					for (Integer temp : detail.getResearcherList()) {
						jdbcTemplate.update(BugHuntrQueryConstants.insert_invited_researcher, detail.getSrno(), temp);
					}
	
				}

				jdbcTemplate.update(BugHuntrQueryConstants.insertProgramAction, detail.getSrno(), "Updated", empid);

			} else {
				LOG.error("ProgramDAOImpl | Exception in  updateproject - Expired Program cannot be edited ");
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_6, e);

		}
		return insertCount;
	}

	@Override
	public void joinProject(joinedEmploye join, String pType) {
		try {
			if ("Public".equals(pType) || "Private".equals(pType)) {
				jdbcTemplate.update(BugHuntrQueryConstants.addjoinedprogramme, join.getReseacher_id(),
						join.getProject_id(), join.getReseacher_id(), "Joined");
			} else if ("Protected".equals(pType)) {
				jdbcTemplate.update(BugHuntrQueryConstants.addjoinedprogramme, join.getReseacher_id(),
						join.getProject_id(), join.getReseacher_id(), "Requested");
			} else {
				;
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_8, e);

		}
	}

	@Override
	public List<joinedEmploye> getjoinedProject2() {
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(BugHuntrQueryConstants.getjoinedProject2);
			List<joinedEmploye> joinedEmployeeList = new ArrayList<joinedEmploye>();
			for (Map<String, Object> row : rows) {
				joinedEmploye joinEmployeeDto = new joinedEmploye();

				joinEmployeeDto.setSrno((Integer) row.get("srno"));
				joinEmployeeDto.setReseacher_id((Integer) row.get("reseacher_id"));
				joinEmployeeDto.setProject_id((Integer) row.get("project_id"));
				joinedEmployeeList.add(joinEmployeeDto);
			}

			return joinedEmployeeList;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_9, e);
			return null;
		}

	}

	@Override
	public Map<String, Object> getdashborardcounts() {
		try {
			Map<String, Object> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getdashborardcounts).get(0);
			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_10, e);
			return null;
		}
	}

	@Override
	public Map<String, Object> ResearchersJoined(int id, int emp_id) {
		try {
			List<Map<String, Object>> researcherjoined = new ArrayList<Map<String, Object>>();
			boolean isAdmin = false;
			List<String> userRoles = brandingService.getUserRoles(emp_id+"");
			if (userRoles.contains(ROLE_ADMINISTRATOR)) {
				isAdmin = true;
			}
			
			if (isAdmin) {
				researcherjoined = jdbcTemplate
						.queryForList(BugHuntrQueryConstants.ResearchersJoinedForAdmin, id, id);
			}else {
				researcherjoined = jdbcTemplate
						.queryForList(BugHuntrQueryConstants.ResearchersJoined, id, id);
			}
			
			List<Map<String, Object>> statistics = jdbcTemplate.queryForList(BugHuntrQueryConstants.ProgramStatistics,
					id);
			List<Map<String, Object>> statistics2 = jdbcTemplate.queryForList(BugHuntrQueryConstants.totalpayout3month,
					id);
			int countpayout = 0;
			List<Map<String, Object>> vulnReported = jdbcTemplate.queryForList(BugHuntrQueryConstants.checkReported, id,
					emp_id);
			if (statistics2.get(0).get("sum") != null) {
				countpayout = Integer.valueOf(statistics2.get(0).get("sum").toString());
			}
			Map<String, Object> otherProjectinfo = new HashMap<String, Object>();
			List<Map<String, Object>> totalvuln = this.vulndao.getVulnByProject(id);
			otherProjectinfo.put("researcherjoined", researcherjoined);
			otherProjectinfo.put("statistics2", countpayout);
			if (totalvuln.size() != 0) {
				otherProjectinfo.put(TEXT_VULN_REWARDED, (long) (statistics.get(0).get(TEXT_COUNT)));
				otherProjectinfo.put(TEXT_APPROVED_PER,
						(long) (statistics.get(0).get(TEXT_COUNT)) * 100 / totalvuln.size());
			} else {
				otherProjectinfo.put(TEXT_VULN_REWARDED, 0);
				otherProjectinfo.put(TEXT_APPROVED_PER, 0);
			}
			if (vulnReported.size() != 0) {
				otherProjectinfo.put(TEXT_REPORTED, true);
			} else {
				otherProjectinfo.put(TEXT_REPORTED, false);
			}
			return otherProjectinfo;
		}

		catch (DataAccessException e) {
			LOG.error(ERROR_MSG_11, e);
			return null;
		}
	}

	@Override
	public boolean ifprogramAdmin(int emp_id, int srno) {
		try {
			List<Map<String, Object>> ifprogramAdmin = jdbcTemplate.queryForList(BugHuntrQueryConstants.ifprogramAdmin,
					emp_id, srno);
			if (ifprogramAdmin.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_12, e);
			return false;
		}
	}

	@Override
	public boolean ifprogramjoinRequested(int emp_id, int srno) {
		try {
			List<Map<String, Object>> ifprogramAdmin = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.ifprogramjoinRequested, emp_id, srno);
			if (ifprogramAdmin.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_13, e);
			return false;
		}
	}

	@Override
	public boolean ifInvited(int emp_id, int srno) {
		try {
			List<Map<String, Object>> ifInvited = jdbcTemplate.queryForList(BugHuntrQueryConstants.ifInvited, emp_id,
					srno);
			if (ifInvited.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_14, e);
			return false;
		}
	}

	@Override
	public Map<String, Object> ProgrammeDeatilstatics(int emp_id) {
		try {
			List<Map<String, Object>> ResearchersJoinedAllPrgrammme = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.ResearchersJoinedAllPrgrammme);
			List<Map<String, Object>> statistics3 = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.ProgramStatistics2);
			List<Map<String, Object>> statistics4 = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.totalpayout3monthprgramm);

			List<Map<String, Object>> totalprogrammecount = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.totalprogrammecount);
			List<Map<String, Object>> total_researcher = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.total_researcher);
			List<Map<String, Object>> vulnReported = jdbcTemplate.queryForList(BugHuntrQueryConstants.checkAllReported,
					emp_id);
			Long countpayout = (long) 0;
			if (statistics4.get(0).get("sum") != null) {
				countpayout = (long) (statistics4.get(0).get("sum"));
			}
			Map<String, Object> otherProjectinfo = new HashMap<String, Object>();
			List<Map<String, Object>> totalvuln = this.vulndao.getAllvulnerabilities();
			otherProjectinfo.put("statistics4", countpayout);
			otherProjectinfo.put("ResearchersJoinedAllPrgrammme", ResearchersJoinedAllPrgrammme);
			otherProjectinfo.put("totalprogrammecount", totalprogrammecount.get(0).get(TEXT_COUNT));
			otherProjectinfo.put("total_researcher", total_researcher.get(0).get(TEXT_COUNT));
			if (totalvuln.size() != 0) {
				otherProjectinfo.put(TEXT_VULN_REWARDED, (long) (statistics3.get(0).get(TEXT_COUNT)));
				otherProjectinfo.put(TEXT_APPROVED_PER,
						(long) (statistics3.get(0).get(TEXT_COUNT)) * 100 / totalvuln.size());
			} else {
				otherProjectinfo.put(TEXT_VULN_REWARDED, 0);
				otherProjectinfo.put(TEXT_APPROVED_PER, 0);
			}
			if (vulnReported.size() != 0) {
				otherProjectinfo.put(TEXT_REPORTED, true);
			} else {
				otherProjectinfo.put(TEXT_REPORTED, false);
			}
			return otherProjectinfo;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_15, e);
			return null;

		}

	}

	@Override
	public List<Map<String, Object>> usersRequestedJoin(int id) {
		try {
			List<Map<String, Object>> usersRequestedJoin = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.usersRequestedJoin, id);
			return usersRequestedJoin;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_16, e);
			return null;

		}
	}

	@Override
	public String getJoinProgramStatus(int projectid, int researcherid) {
		try {
			List<Map<String, Object>> usersRequestedJoin = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.getJoinProgramStatus, projectid, researcherid);
			if (usersRequestedJoin.size() == 1) {
				return (String) usersRequestedJoin.get(0).get("status");
			} else {
				return null;
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_17, e);
			return null;

		}
	}

	@Override
	public void updateJoinStatus(joinedEmploye data, String status) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.updateJoinStatus, data.getProject_id(), data.getReseacher_id());
			if ("Suspend".equals(status)) {
				jdbcTemplate.update(BugHuntrQueryConstants.suspendJoinedprogramme, data.getReseacher_id(),
						data.getProject_id(), data.getReseacher_id(), status, data.getSuspendTill());
			} else if ("UnJoin".equals(status) || "Declined".equals(status)) {
				jdbcTemplate.update(BugHuntrQueryConstants.UnJoinprogramme, data.getReseacher_id(),
						data.getProject_id(), data.getReseacher_id(), status);
			} else {
				jdbcTemplate.update(BugHuntrQueryConstants.addjoinedprogramme, data.getReseacher_id(),
						data.getProject_id(), data.getReseacher_id(), status);
			}
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_18, e);

		}
	}

	@Override
	public void checkSuspendedResearchers() {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.checkSuspendedResearchers);
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_19, e);

		}
	}

	@Override
	public List<Map<String, Object>> getMyProject(int emp_id) {
		try {
			List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getMyProject, emp_id);
			if (data.size() != 0) {
				jdbcTemplate.update(BugHuntrQueryConstants.updatestatus);
			}
			for (Map<String, Object> temp : data) {

				int srno = (int) temp.get("srno");
				if (TEXT_PRIVATE.equals((String) temp.get(TEXT_PTYPE))) {
					List<Map<String, Object>> invitedResearcher = jdbcTemplate
							.queryForList(BugHuntrQueryConstants.get_invited_researcher, srno);
					temp.put(TEXT_INVITE, invitedResearcher);
				}

				List<Map<String, Object>> jobdata = jdbcTemplate.queryForList(BugHuntrQueryConstants.getProjectJobsbyid,
						srno);
				List<Map<String, Object>> otherdetails = jdbcTemplate
						.queryForList(BugHuntrQueryConstants.getProjectotherbyid, srno);
				temp.put(TEXT_JOB_PRICING, jobdata);
				temp.put(TEXT_OTHER, otherdetails);

				temp.put(TEXT_IS_PRG_JOINED, isProgramJoined(emp_id, srno));
				temp.put(TEXT_JOINPROGRAM, ifprogramjoinRequested(emp_id, srno));

			}
			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_20, e);
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getJoinedProjects(int emp_id) {
		try {
			
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			
			boolean isAdmin = false;
			List<String> userRoles = brandingService.getUserRoles(emp_id+"");
			if (userRoles.contains(ROLE_ADMINISTRATOR)) {
				isAdmin = true;
			}
			if(!isAdmin) {
				
				data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getJoinedProject, emp_id);
				if (data.size() != 0) {
					jdbcTemplate.update(BugHuntrQueryConstants.updatestatus);
				}
			}else {
				data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getJoinedProjectForAdmin, emp_id);
				if (data.size() != 0) {
					jdbcTemplate.update(BugHuntrQueryConstants.updatestatus);
				}
			}
			
			
			for (Map<String, Object> temp : data) {

				int srno = (int) temp.get("srno");
				if (TEXT_PRIVATE.equals((String) temp.get(TEXT_PTYPE))) {
					List<Map<String, Object>> invitedResearcher = jdbcTemplate
							.queryForList(BugHuntrQueryConstants.get_invited_researcher, srno);
					temp.put(TEXT_INVITE, invitedResearcher);
				}

				List<Map<String, Object>> jobdata = jdbcTemplate.queryForList(BugHuntrQueryConstants.getProjectJobsbyid,
						srno);
				List<Map<String, Object>> otherdetails = jdbcTemplate
						.queryForList(BugHuntrQueryConstants.getProjectotherbyid, srno);
				temp.put(TEXT_JOB_PRICING, jobdata);
				temp.put(TEXT_OTHER, otherdetails);

				temp.put(TEXT_IS_PRG_JOINED, isProgramJoined(emp_id, srno));
				temp.put(TEXT_JOINPROGRAM, ifprogramjoinRequested(emp_id, srno));

			}
			return data;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_21, e);
			return null;

		}

	}

	@Override
	public void deleteAnnouncements(int id) {
		jdbcTemplate.update(BugHuntrQueryConstants.deleteAnnouncement, id);
	}

	@Override
	public List<Map<String, Object>> getData(int id) {
		List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getAnnouncementData, id);
		return data;
	}

	@Override
	public void deleteJobs() {
		jdbcTemplate.update(BugHuntrQueryConstants.deleteJob);
	}
}
