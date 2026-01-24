package com.tcs.utx.digiframe.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class WorkListDAOImpl implements WorkListDAO {

	private static final Logger LOG = LoggerFactory.getLogger(WorkListDAOImpl.class);

	private static final String ERROR_MSG_1 = "WorkListDAOImpl | Exception in  getWorklistForResearcher - ";

	private static final String ERROR_MSG_2 = "WorkListDAOImpl | Exception in  getWorklistForProgramAdmin - ";

	private static final String ERROR_MSG_3 = "WorkListDAOImpl | Exception in  reportsForResearcher - ";

	private static final String ERROR_MSG_4 = "WorkListDAOImpl | Exception in  reportsForAdmin- ";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, Object> getWorklistForResearcher(int emp_id) {
		try {
			Map<String, Object> retData = new HashMap<>();
			List<Map<String, Object>> vulnWorkList = jdbcTemplate.queryForList(BugHuntrQueryConstants.RvulnWorkList,
					emp_id);
			List<Map<String, Object>> programInvitedWorkList = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.RprogramInvitedWorkList, emp_id, emp_id);
			List<Map<String, Object>> programJoinedWorkList = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.RprogramJoinedWorkList, emp_id);
			List<Map<String, Object>> programAnnouncement = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.RprogramAnnouncement, emp_id, emp_id);

			programInvitedWorkList.addAll(programJoinedWorkList);
			programInvitedWorkList.addAll(programAnnouncement);
			if (vulnWorkList.size() > 0) {
				retData.put("Vulnerability", vulnWorkList);
			}
			if (programInvitedWorkList.size() > 0) {
				retData.put("Program", programInvitedWorkList);
			}
			return retData;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_1, e);
			return null;
		}

	}

	@Override
	public Map<String, Object> getWorklistForProgramAdmin(int emp_id) {
		try {
			Map<String, Object> retData = new HashMap<>();
			List<Map<String, Object>> vulnWorkList = jdbcTemplate.queryForList(BugHuntrQueryConstants.RvulnWorkList,
					emp_id);
			List<Map<String, Object>> PAvulnWorkList = jdbcTemplate.queryForList(BugHuntrQueryConstants.PAvulnWorkList,
					emp_id);

			List<Map<String, Object>> programInvitedWorkList = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.RprogramInvitedWorkList, emp_id, emp_id);
			List<Map<String, Object>> programJoinedWorkList = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.RprogramJoinedWorkList, emp_id);
			List<Map<String, Object>> programAnnouncement = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.RprogramAnnouncement, emp_id, emp_id);
			List<Map<String, Object>> PAprogramJoinedWorkList = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.PAprogramJoinedWorkList, emp_id);
			List<Map<String, Object>> PAprogramCreatedWorkList = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.PAprogramCreatedWorkList, emp_id);

			List<Map<String, Object>> RprogramClosedWorkList = jdbcTemplate
					.queryForList(BugHuntrQueryConstants.RprogramClosedWorkList, emp_id, emp_id);

			PAprogramJoinedWorkList.addAll(PAprogramCreatedWorkList);
			PAprogramJoinedWorkList.addAll(programAnnouncement);
			PAprogramJoinedWorkList.addAll(programJoinedWorkList);
			PAprogramJoinedWorkList.addAll(programInvitedWorkList);
			PAprogramJoinedWorkList.addAll(RprogramClosedWorkList);

			vulnWorkList.addAll(PAvulnWorkList);
			if (vulnWorkList.size() > 0) {
				retData.put("Vulnerability", vulnWorkList);
			}

			if (PAprogramJoinedWorkList.size() > 0) {
				retData.put("Program", PAprogramJoinedWorkList);
			}
			return retData;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_2, e);
			return null;

		}

	}

	@Override
	public List<Map<String, Object>> reportsForResearcher(int emp_id, String role) {
		try {
			List<Map<String, Object>> vulnWorkList = new ArrayList<>();

			if ("Researcher".equals(role)) {
				vulnWorkList = jdbcTemplate.queryForList(BugHuntrQueryConstants.RReports, emp_id, emp_id);
			} else if ("Bounty Admin".equals(role)) {
				vulnWorkList = jdbcTemplate.queryForList(BugHuntrQueryConstants.BAReports, emp_id);
				List<Map<String, Object>> asResearcher = jdbcTemplate.queryForList(BugHuntrQueryConstants.RReports,
						emp_id, emp_id);
				vulnWorkList.addAll(asResearcher);
			} else if ("Admin".equals(role)) {
				vulnWorkList = jdbcTemplate.queryForList(BugHuntrQueryConstants.AReports);
			} else {
				;
			}

			return vulnWorkList;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_3, e);
			return null;

		}
	}

	@Override
	public List<Map<String, Object>> reportsForAdmin(int emp_id) {
		try {
			List<Map<String, Object>> vulnWorkList = jdbcTemplate.queryForList(BugHuntrQueryConstants.RReports, emp_id,
					emp_id);
			return vulnWorkList;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_4, e);
			return null;

		}

	}
}
