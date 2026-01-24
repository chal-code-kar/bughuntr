package com.tcs.utx.digiframe.dao;

public class BugHuntrQueryConstants {

	public static String getEmployeedetails = "SELECT srno, employee_srno AS empid, roleid, full_name AS employee_name "
			+ "FROM ssa_eai_hr_emp_basic_dtls " + "RIGHT JOIN b_employee_role_mapping "
			+ "ON CAST(b_employee_role_mapping.employee_srno AS VARCHAR) = ssa_eai_hr_emp_basic_dtls.employee_number";

	public static String AllUsersRole = "select (Select role_name from b_roles where srno = roleid) As role_name, "
			+ "(select full_name from ssa_eai_hr_emp_basic_dtls "
			+ " where CAST(employee_number AS VARCHAR) = CAST(employee_srno AS VARCHAR)) As employee_name "
			+ "from b_employee_role_mapping";

	public static final String GET_LOOKUP_VALUES = "SELECT LOOKUPCODE AS lookup_key, LOOKUPVALUE AS lookup_value "
			+ "FROM bb_lookup_data WHERE LOOKUPGROUP = ? AND ACTIVE = TRUE ORDER BY ORDERSEQ";

	
	public static String createProject = "INSERT INTO bproject_master (bprogramme_name, bproject_ownemp_id, bugproject_ownemp_name, "
			+ " bprojectstart_date, bprojectend_date, bproject_api, bproject_tech, bproject_description, "
			+ " bappln_criticality, createddate, unit, staticpage, dynamicpage, roles, ptype, status, apm_id) "
			+ "values(?, ?, (select full_name from ssa_eai_hr_emp_basic_dtls where CAST(employee_number AS VARCHAR) = CAST(? AS VARCHAR)), "
			+ " CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, "
			+ " (select iou_name from ssa_eai_hr_emp_basic_dtls where CAST(employee_number AS VARCHAR) = CAST(? AS VARCHAR)), "
			+ " ?, ?, ?, ?, 'true', ?)";

	public static String createProjectChild = "INSERT INTO bproject_child (masterid,overview,access,resources,createddate) values(?,?,?,?,CURRENT_TIMESTAMP)";

	public static String createProjectJobChild = "INSERT INTO bprojectjob_child (masterjobid,jobtype,pricing,maxpricing,status,createddate,reward_type) "
			+ "values(?, ?, ?, ?, TRUE, CURRENT_TIMESTAMP, ?)";

	public static String createProjectruleChild = "INSERT INTO bprojectrule_child (masterruleid,rules_category,rules_details,createddate) values( ?, ?, ?, CURRENT_TIMESTAMP)";

	public static String createProjectannouncementChild = "INSERT INTO bprojectannouncement_child (masterrannouncementid,announcement_category,announcement_details,createddate) "
			+ "values( ?, ?, ?, CURRENT_TIMESTAMP)";

	public static String Recentprogramcounts = "Select * from bproject_master WHERE createddate >= DATEADD('WEEK', -1, CURRENT_TIMESTAMP)";

	public static String getAllProject2 = "Select * from bproject_master where bprojectend_date >= CURRENT_TIMESTAMP "
			+ "AND createddate >= DATEADD('WEEK', -1, CURRENT_TIMESTAMP) order by srno desc";

	public static String getAllReasercherslasweek = "select * from researcher_master WHERE createddate >= DATEADD('WEEK', -1, CURRENT_TIMESTAMP)";

	public static String ResearchersJoinedAllPrgrammme = "select srno, emp_bio, skills, emp_avatar_blob, createddate, avatar, profile_pic, points "
			+ "from researcher_master where emp_id IN "
			+ "(Select DISTINCT(reseacher_id) from researcher_join_programme "
			+ " where createddate >= DATEADD('WEEK', -1, CURRENT_TIMESTAMP) AND status='Joined' AND active = TRUE)";

	public static String totalpayout3month = "select SUM(total_reward) from b_reward_earned "
			+ "where active = TRUE AND createddate >= DATEADD('MONTH', -3, CURRENT_TIMESTAMP) "
			+ "AND vulnid IN (SELECT id FROM bb_vulnerability WHERE programid = ?)";

	public static String totalpayout3monthprgramm = "select SUM(total_reward) from b_reward_earned "
			+ "where active = TRUE AND createddate >= DATEADD('MONTH', -3, CURRENT_TIMESTAMP)";

	public static String getdashborardcounts = "Select "
			+ "(select count(srno) from bproject_master where bprojectend_date > CURRENT_TIMESTAMP) AS TotalProgram, "
			+ "(Select count(*) from bproject_master WHERE createddate >= DATEADD('WEEK', -1, CURRENT_TIMESTAMP)) AS ProgramsLastWeek, "
			+ "(select SUM(total_reward) from b_reward_earned where active = TRUE) AS TotalBounty, "
			+ "(select count(distinct(programid)) from bb_vulnerability "
			+ "  where id IN (Select distinct(vulnid) from b_reward_earned where active = TRUE)) AS DistinctBountyPrograms, "
			+ "(select count(*) from researcher_master) AS TotalResearchers, "
			+ "(Select count(*) from researcher_master WHERE createddate >= DATEADD('WEEK', -1, CURRENT_TIMESTAMP)) AS NewResearcherAdded, "
			+ "(Select count(*) from bb_vulnerability where status='Approved' ) AS expolitablebugs, "
			+ "(Select count(*) from bb_vulnerability where status='Approved' AND createddt >= DATEADD('MONTH', -3, CURRENT_TIMESTAMP)) AS BugsLastQuarter";

	public static String deleteJob = "delete from bprojectjob_child where status = FALSE";

	public static String updateProjectJobChild = "update bprojectjob_child set status = FALSE where masterjobid = ?";

	public static String updateJoinStatus = "update researcher_join_programme set active = FALSE where project_id = ? AND reseacher_id = ? AND active = TRUE";


	public static String addResearchers = "INSERT INTO researcher_master (emp_id, emp_name, emp_bio, skills, createddate, teams, avatar, profile_pic) "
			+ "values(?, (select full_name from ssa_eai_hr_emp_basic_dtls where CAST(employee_number AS VARCHAR) = CAST(? AS VARCHAR)), "
			+ " ?, ?, CURRENT_TIMESTAMP, (select iou_name from ssa_eai_hr_emp_basic_dtls where CAST(employee_number AS VARCHAR) = CAST(? AS VARCHAR)), ?, ?)";

	public static String usersRequestedJoin = "Select Distinct(reseacher_id), status, "
			+ "(select full_name from ssa_eai_hr_emp_basic_dtls where CAST(employee_number AS VARCHAR) = CAST(reseacher_id AS VARCHAR)) As reseacher_name "
			+ "from researcher_join_programme where project_id = ? AND active = TRUE";

	public static String getJoinProgramStatus = "Select *, "
			+ "(select full_name from ssa_eai_hr_emp_basic_dtls where CAST(employee_number AS VARCHAR) = CAST(reseacher_id AS VARCHAR)) As reseacher_name "
			+ "from researcher_join_programme where project_id = ? AND active = TRUE AND reseacher_id = ?";


	public static final String INSERT_VULN = " INSERT INTO bb_vulnerability("
			+ "programid, researcherid, title, assessmenttype, description, replication, impact, remediation,"
			+ "extreferences, remarks, identificationdt, createddt, cvss3vector,"
			+ "portidentified, cvss_score, type, affectedcia, vulnerabilitytype, asvs)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?);"
			+ " insert into bb_vulnerability_actions (vulnid, status, empid, remarks) "
			+ " select max(id), 'Reported', ?, CONCAT( (select avatar from researcher_master where emp_id = ?), ? ) "
			+ " from bb_vulnerability";

	public static String updateVuln = "update bb_vulnerability set "
			+ "title = ?, assessmenttype = ?, description = ?, replication = ?, impact = ?, remediation = ?, "
			+ "extreferences = ?, remarks = ?, cvss3vector = ?, updateddt = CURRENT_TIMESTAMP, "
			+ "portidentified = ?, cvss_score = ?, type = ?, affectedcia = ?, vulnerabilitytype = ?, asvs = ?, status = 'Updated' "
			+ "where id = ?; " + "insert into bb_vulnerability_actions (vulnid, status, empid, remarks) "
			+ "values(?, 'Updated', ?, CONCAT((select avatar from researcher_master where emp_id = ?), ' updated ', ? ))";


	public static String UPDATE_OPTION = "UPDATE bb_lookup_data SET lookupvalue = ?, lookupdescription = ?, updatedby = ?, updateddt = CURRENT_TIMESTAMP WHERE id = ? AND active = TRUE";

	public static String DELETE_OPTION = "UPDATE bb_lookup_data SET ACTIVE = FALSE WHERE id = ?";

	public static String INSERT_OPTION = "INSERT INTO bb_lookup_data(id, lookupgroup, lookupvalue, lookupdescription, lookupcode, createdby, orderseq) "
			+ "VALUES(((select max(id) from bb_lookup_data) + 1), ?, ?, ?, ((SELECT COUNT(lookupcode) FROM bb_lookup_data) + 1), ?, "
			+ "       ((select COUNT(id) from bb_lookup_data where lookupgroup = ?) + 1))";


	public static String addBlog = "INSERT INTO b_blog (title, brief, emp_name, createdate, category) "
			+ "values(?, ?, (select full_name from ssa_eai_hr_emp_basic_dtls where CAST(employee_number AS VARCHAR) = CAST(? AS VARCHAR)), CURRENT_TIMESTAMP, ?)";

	public static String POSTHistory = "INSERT INTO b_bughuntr_announcement (fontname, releaseverinfo, releaseitemname) VALUES(?, ?, ?)"; // (no
																																				// NOW()
																																				// fields
																																				// in
																																				// table
																																				// per
																																				// snippet)


	private static final String CASE = "case ";

	public static final StringBuilder BUGHUNTR_OPTION_EXISTS = new StringBuilder();

	public static String RvulnWorkList = "Select " + CASE
			+ "when status = 'Approved' then CONCAT('Vulnerability (',(select title from bb_vulnerability where id = vulnid), ') ', status, ' and rewarded with ', (select total_reward from b_reward_earned where b_reward_earned.vulnid = bb_vulnerability_actions.vulnid), ' Gems for Program (', (select bprogramme_name from bproject_master where srno IN (select programid from bb_vulnerability where id = vulnid)), '). ') "
			+ "else "
			+ "(Select CONCAT('Vulnerability (',(select title from bb_vulnerability where id = vulnid), ') ', status, ' for Program (',  (select bprogramme_name from bproject_master where srno IN (select programid from bb_vulnerability where id = vulnid)), ') ') )"
			+ "end as remarks, CONCAT('Vulnerability ', status) As filter, vulnid As targetid, createddt, '/ViewVulnerability/' AS link from bb_vulnerability_actions where vulnid IN (select id from bb_vulnerability where researcherid = ?)  order by createddt DESC";

	public static String RprogramInvitedWorkList = "select emp_srno, programid AS targetid, ? As researcher_id, '/project/' AS link, createddt, CONCAT('You were invited for Program (', (select bprogramme_name from bproject_master where srno = programid), '). ') AS remarks, 'Researcher Invited' As filter from b_invited_researcher where emp_srno IN (select srno from researcher_master where emp_id = ?) order by createddt DESC";


	public static String RprogramJoinedWorkList = "select " + CASE
			+ "	when status = 'Joined' then (CONCAT('You have joined Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "	when status = 'UnJoin' then (CONCAT('You have unjoined Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "	when status = 'Expel' then (CONCAT('You were expeled from Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "	when status = 'Requested' then (CONCAT('You had Requested to join Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "	when status = 'Declined' then (CONCAT('Your Request to join Program was declined (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "	when status = 'Suspend' then (CONCAT('You were suspended from Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "end as remarks, researcher_profile_id As emp_srno, project_id AS targetid, reseacher_id As researcher_id, '/project/' AS link, createddate As createddt, status As filter from researcher_join_programme where reseacher_id = ?";



public static String RprogramAnnouncement =
    "SELECT " +
    "  masterrannouncementid AS targetid, " +
    "  ? AS researcher_id, " +
    "  '/project/' AS link, " +
    "  createddate AS createddt, " +
    "  ('Announcement (' || (SELECT bprogramme_name FROM bproject_master WHERE srno = masterrannouncementid) || ') - ' || announcement_details) AS remarks, " +
    "  'Joined Program Announcements' AS filter " +
    "FROM bprojectannouncement_child " +
    "WHERE masterrannouncementid IN ( " +
    "  SELECT project_id " +
    "  FROM researcher_join_programme " +
    "  WHERE reseacher_id = ? " +
    "    AND status = 'Joined' " +
    "    AND active = TRUE " + // If VARCHAR: use 'true'
    ") " +
    "ORDER BY createddate DESC";

	


	public static String PAprogramJoinedWorkList = "select " + CASE
			+ "when status = 'Joined' then (CONCAT('Researcher ', researcher_master.avatar ,' joined Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "when status = 'UnJoin' then (CONCAT('Researcher ', researcher_master.avatar ,' unjoined Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "when status = 'Expel' then (CONCAT('Researcher ', researcher_master.avatar ,' is expeled from (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "when status = 'Requested' then (CONCAT('Researcher ', researcher_master.avatar ,' has requested to join Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "when status = 'Declined' then (CONCAT('Researcher ', researcher_master.avatar ,' request to join Program (', (select bprogramme_name from bproject_master where srno = project_id), ') is declined. ')) "
			+ "when status = 'Suspend' then (CONCAT('Researcher ', researcher_master.avatar ,' is suspended from Program (', (select bprogramme_name from bproject_master where srno = project_id), '). ')) "
			+ "end as remarks, project_id AS targetid, reseacher_id As researcher_id, '/project/' AS link, researcher_join_programme.createddate As createddt, CONCAT( 'Program ' ,status) As filter from "
			+ "researcher_join_programme join researcher_master on researcher_master.emp_id = researcher_join_programme.reseacher_id where project_id IN (select srno from bproject_master where bproject_ownemp_id = ?) order by researcher_join_programme.createddate DESC";

	public static String PAprogramCreatedWorkList = "select projectid As targetid, empid As researcher_id, '/project/' AS link, createddt, CONCAT('You have ', status ,' Program (', (select bprogramme_name from bproject_master where srno = projectid) ,').') As remarks, remarks As filter from bproject_actions where empid = ?";

	public static String PAvulnWorkList = "Select " + CASE + "when status = 'Approved' "
			+ "	then CONCAT('You have Approved Vulnerability (',(select title from bb_vulnerability where id = vulnid), ') and rewarded ', (Select researcherid from bb_vulnerability where id = vulnid), ' with ', (select total_reward from b_reward_earned where b_reward_earned.vulnid = bb_vulnerability_actions.vulnid), ' Gems for Program (',  (select bprogramme_name from bproject_master where srno IN (select programid from bb_vulnerability where id = vulnid)), '). ') "
			+ "when status = 'Updated' "
			+ "	then CONCAT('Researcher ', avatar, ' has Updated Vulnerability (',(select title from bb_vulnerability where id = vulnid), ') for Program (',  (select bprogramme_name from bproject_master where srno IN (select programid from bb_vulnerability where id = vulnid)), ') ') "
			+ "when status = 'Reported' "
			+ "	then CONCAT('Researcher ', avatar, ' has Reported Vulnerability (',(select title from bb_vulnerability where id = vulnid), ') for Program (',  (select bprogramme_name from bproject_master where srno IN (select programid from bb_vulnerability where id = vulnid)), ') ') "
			+ "else "
			+ "	CONCAT('You have ', status,' Vulnerability (',(select title from bb_vulnerability where id = vulnid), ') for Program (',  (select bprogramme_name from bproject_master where srno IN (select programid from bb_vulnerability where id = vulnid)), ') ') "
			+ "end as remarks, CONCAT('Vulnerability ', status) As filter, vulnid As targetid, createddt, '/ViewVulnerability/' AS link from bb_vulnerability_actions join researcher_master on bb_vulnerability_actions.empid = researcher_master.emp_id where vulnid IN (select id from bb_vulnerability where programid IN (select srno from bproject_master where bproject_ownemp_id = ?)) order by createddt DESC";

	public static String RprogramClosedWorkList = "\n"
			+ "SELECT\n"
			+ "  srno AS targetid,\n"
			+ "  ? AS researcher_id,\n"
			+ "  '/project/' AS link,\n"
			+ "  createddate AS createddt,\n"
			+ "  'Program (' || bprogramme_name || ') has been closed' AS remarks,\n"
			+ "  'Program Closed' AS filter\n"
			+ "FROM bproject_master\n"
			+ "WHERE srno IN (\n"
			+ "  SELECT project_id\n"
			+ "  FROM researcher_join_programme\n"
			+ "  WHERE reseacher_id = ? AND status = 'Joined' AND active = TRUE\n"
			+ ")\n"
			+ "AND bprojectend_date > CURRENT_TIMESTAMP;\n"
			+ "";

	

	

public static String RReports =
    "SELECT " +
    "  (SELECT bprogramme_name FROM bproject_master WHERE srno = vulnerability.programid) as bprogramme_name, " +
    "  (SELECT avatar FROM researcher_master WHERE emp_id = vulnerability.researcherid) as avatar, " +
    "  vulnerability.title, " +
    "  (SELECT total_reward FROM b_reward_earned AS reward " +
    "     WHERE reward.vulnid = vulnerability.id AND reward.active = TRUE), " + // If VARCHAR: reward.active = 'true'
    "  CAST(vulnerability.createddt AS DATE) as createddt , " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND status = 'Updated') AS modifiedDt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND (status = 'Rejected' OR status = 'Approved')) AS rejectedDt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND status = 'Approved') AS rewardeddt, " +
    "  (SELECT vrt_category FROM b_vulnerability_category WHERE srno = vulnerability.assessmenttype) " +
    "FROM bb_vulnerability AS vulnerability " +
    "WHERE vulnerability.researcherid = ? " +
    "  AND vulnerability.programid IN ( " +
    "    SELECT project_id " +
    "    FROM researcher_join_programme " +
    "    WHERE reseacher_id = ? " +
    "      AND status = 'Joined' " +
    "      AND active = TRUE" + // If VARCHAR: active = 'true'
    "  )";


public static String AReports =
    "SELECT " +
    "  (SELECT bprogramme_name FROM bproject_master WHERE srno = vulnerability.programid) as bprogramme_name, " +
    "  (SELECT avatar FROM researcher_master WHERE emp_id = vulnerability.researcherid) as avatar, " +
    "  vulnerability.title, " +
    "  (SELECT total_reward FROM b_reward_earned AS reward " +
    "     WHERE reward.vulnid = vulnerability.id AND reward.active = TRUE), " + // If VARCHAR: reward.active = 'true'
    "  CAST(vulnerability.createddt AS DATE) as createddt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND status = 'Updated') AS modifiedDt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND (status = 'Rejected' OR status = 'Approved')) AS rejectedDt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND status = 'Approved') AS rewardeddt, " +
    "  (SELECT vrt_category FROM b_vulnerability_category WHERE srno = vulnerability.assessmenttype) " +
    "FROM bb_vulnerability AS vulnerability " +
    "WHERE vulnerability.programid IN (SELECT srno FROM bproject_master)";


public static String BAReports =
    "SELECT " +
    "  (SELECT bprogramme_name FROM bproject_master WHERE srno = vulnerability.programid) as bprogramme_name, " +
    "  (SELECT avatar FROM researcher_master WHERE emp_id = vulnerability.researcherid) as avatar, " +
    "  vulnerability.title, " +
    "  (SELECT total_reward FROM b_reward_earned AS reward " +
    "     WHERE reward.vulnid = vulnerability.id AND reward.active = TRUE), " + // If VARCHAR: reward.active = 'true'
    "  CAST(vulnerability.createddt AS DATE) as createddt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND status = 'Updated') AS modifiedDt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND (status = 'Rejected' OR status = 'Approved')) AS rejectedDt, " +
    "  (SELECT CAST(MAX(createddt) AS DATE) FROM bb_vulnerability_actions " +
    "     WHERE vulnid = vulnerability.id AND status = 'Approved') AS rewardeddt, " +
    "  (SELECT vrt_category FROM b_vulnerability_category WHERE srno = vulnerability.assessmenttype) " +
    "FROM bb_vulnerability AS vulnerability " +
    "WHERE vulnerability.programid IN ( " +
    "  SELECT srno " +
    "  FROM bproject_master " +
    "  WHERE bproject_ownemp_id = ? " +
    ")";


	// PermissionHelper Query Constants

	public static String getRoles = "select CONCAT( module.module, '_', module.submodule, '_', permission.permission) AS Access from b_permission_role_mapping, b_module_details module, b_permission permission where roleid IN (select roleid from b_employee_role_mapping where employee_srno = ?) AND module.srno = moduleid  AND permission.srno = permissionid";

	public static String getTotalAccess = "select CONCAT( module.module, '_', module.submodule), permission.permission from b_permission_role_mapping, b_module_details module, b_permission permission, b_roles roles where module.srno = moduleid AND permission.srno = permissionid AND roleid = roles.srno AND roles.role_name = ?";

	public static String insertRole = "insert into b_roles(role_name, role_desc) values (?, ?)";

	public static String getTotalRoles = "select * from b_roles";

	public static String getTotalPermission = "select * from b_permission";

	public static String updatePermission = "insert into b_permission_role_mapping(roleid, moduleid, permissionid) select (select srno from b_roles where role_name = ?), (select srno from b_module_details where module = ? AND submodule = ?), (select srno from b_permission where permission = ?)";

	public static String deleteRolePermissionExists = "delete from b_permission_role_mapping  where roleid IN (select srno from b_roles where role_name = ?) AND moduleid IN (select srno from b_module_details where module = ? AND submodule = ?) AND permissionid IN (select srno from b_permission where permission = ?)";

	public static String notAchievable = "select concat(module, '_', submodule, '_', permission) from b_module_permission_not_achievable mapping, b_module_details module, b_permission permission where module.srno = mapping.moduleid AND permission.srno = mapping.permissionid";

	public static String getModules = "select CONCAT(module, '_', submodule) from b_module_details";

	public static String insertbountyAdminRole = "insert into b_employee_role_mapping (employee_srno, roleid) select ?, (SELECT srno from b_roles where role_name = ?)";

	public static String getUserRoles = "select (Select role_name from b_roles where srno = roleid) from b_employee_role_mapping where employee_srno = ?";


	public static String getEmployeeDetail = "select * from ssa_eai_hr_emp_basic_dtls where employee_number = ?";


	public static String addjoinedprogramme = "INSERT INTO researcher_join_programme(reseacher_id,project_id,researcher_profile_id, status, active) values(?,?, (select srno from  researcher_master where  emp_id=?), ?, 'true')";

	public static String suspendJoinedprogramme = "INSERT INTO researcher_join_programme(reseacher_id,project_id,researcher_profile_id, status, active, revoketill) values(?,?, (select srno from  researcher_master where  emp_id=?), ?, 'true', ?)";

	public static String UnJoinprogramme = "INSERT INTO researcher_join_programme(reseacher_id,project_id,researcher_profile_id, status, active) values(?,?, (select srno from  researcher_master where emp_id=?), ?, 'false')";

	public static String getAllProjectForAdmin = "Select * from bproject_master where bprojectend_date >= now() order by srno desc";

	public static String getAllProject = "Select srno, bprogramme_name,  bprojectstart_date, bprojectend_date, bproject_api, bproject_tech, bproject_description, bappln_criticality, createddate, staticpage, dynamicpage, roles, ptype ,status  from bproject_master where bprojectend_date >= now() order by srno desc";

	public static String updatestatus = "UPDATE bproject_master SET status = 'DEACTIVE' WHERE bprojectend_date < now()";


	public static String getjoinedProject2 = "Select * from researcher_join_programme where status = 'Joined' AND active = 'true'";

	public static String updatejoinedprogramme = "UPDATE researcher_join_programme SET project_id = ? WHERE srno=? AND status = 'Joined' AND active = 'true'";


	public static String getProjectById = "Select * from bproject_master master,bproject_child child WHERE master.srno=? AND child.masterid=master.srno";

	public static String updateProgram = "UPDATE bproject_master SET bprojectend_date=?, bproject_api=?, bproject_tech=?, bproject_description=?, bappln_criticality=?, staticpage = ?, dynamicpage = ?, roles = ?, ptype = ? WHERE srno = ? AND status = 'true'";

	public static String getMaxProjectSrNo = "Select max(srno) from bproject_master";

	public static String getLatestProjectSrNo = "SELECT srno as max FROM bproject_master ORDER BY createddate DESC LIMIT 1";

	public static String checkAnnouncementsChild = "select * from bprojectannouncement_child where srno=? and masterrannouncementid=?";


	public static String createProjectScopeChild = "INSERT INTO bprojectscope_child (masterscopeid,inscope,outscope,createddate) values( ?,?,?,NOW())";

	public static String getProjectscopebyid = "Select * from bprojectscope_child where masterscopeid=? ";

	public static String getProjectannouncementbyid = "Select * from bprojectannouncement_child where masterrannouncementid=?";

	public static String getProjectrulebyid = "Select * from bprojectrule_child where masterruleid = ? AND rules_category = ?";

	public static String getProjectotherbyid = "Select * from bproject_child where masterid=?";

	public static String ifprogramsjoined = "\n"
			+ "SELECT *\n"
			+ "FROM researcher_join_programme\n"
			+ "WHERE reseacher_id = ?\n"
			+ "  AND project_id = ?\n"
			+ "  AND status = 'Joined'\n"
			+ "  AND active = TRUE;\n"
			+ "";

	public static String ifprogramAdmin = "Select * from bproject_master where bproject_ownemp_id = ? AND srno = ? ";


	

public static String ifprogramjoinRequested =
    "SELECT * " +
    "FROM researcher_join_programme " +
    "WHERE reseacher_id = ? " +
    "  AND project_id = ? " +
    "  AND status = 'Requested' " +
    "  AND active = TRUE"; // If VARCHAR: AND active = 'true'

static String ResearchersJoinedForAdmin =
    "SELECT DISTINCT " +
    "  rjp.reseacher_id, " +
    "  r.avatar, " +
    "  r.profile_pic, " +
    "  r.teams, " +
    "  r.points, " +
    "  COALESCE( " +
    "    ( " +
    "      SELECT SUM(be.total_reward) " +
    "      FROM b_reward_earned AS be " +
    "      WHERE be.researcherid = rjp.reseacher_id " +
    "        AND be.vulnid IN ( " +
    "          SELECT v.id " +
    "          FROM bb_vulnerability AS v " +
    "          WHERE v.programid = ? " +
    "        ) " +
    "    ), 0 " +
    "  ) AS total_bounty_earned " +
    "FROM researcher_join_programme AS rjp " +
    "JOIN researcher_master AS r " +
    "  ON r.emp_id = rjp.reseacher_id " +
    "WHERE rjp.project_id = ? " +
    "  AND rjp.status = 'Joined' " +
    "  AND rjp.active = TRUE"; // If VARCHAR: AND rjp.active = 'true'

static String ResearchersJoined =
    "SELECT DISTINCT " +
    "  r.avatar, " +
    "  r.profile_pic, " +
    "  r.teams, " +
    "  r.points, " +
    "  COALESCE( " +
    "    ( " +
    "      SELECT SUM(be.total_reward) " +
    "      FROM b_reward_earned AS be " +
    "      WHERE be.researcherid = rjp.reseacher_id " +
    "        AND be.vulnid IN ( " +
    "          SELECT v.id " +
    "          FROM bb_vulnerability AS v " +
    "          WHERE v.programid = ? " +
    "        ) " +
    "    ), 0 " +
    "  ) AS total_bounty_earned " +
    "FROM researcher_join_programme AS rjp " +
    "JOIN researcher_master AS r " +
    "  ON r.emp_id = rjp.reseacher_id " +
    "WHERE rjp.project_id = ? " +
    "  AND rjp.status = 'Joined' " +
    "  AND rjp.active = TRUE"; // If VARCHAR: AND rjp.active = 'true'

	
	public static String ProgramStatistics = "Select count(id) AS count from bb_vulnerability where programid = ? AND status = 'Approved'";



	public static String getProjectJobsbyid = "Select MIN(pricing) as min_gems, MAX(maxpricing) as max_gems from bprojectjob_child where masterjobid=? AND status = TRUE";

	public static String getProjectDetilasJobsbyid = "select * from bprojectjob_child  where masterjobid=? AND status = TRUE";

	public static String updateProjectChild = "update bproject_child set overview = ?, access = ?, resources = ? where masterid = ?";

	public static String updateProjectScopeChild = "update bprojectscope_child set inscope = ?, outscope = ? where masterscopeid = ?";


	public static String updateProjectruleChild = "delete from bprojectrule_child where masterruleid = ?";

	public static String updateProjectannouncementChild = "delete from bprojectannouncement_child where masterrannouncementid = ?";


	public static String ProgramStatistics2 = "Select count(id) AS count from bb_vulnerability where status = 'Approved'";


	public static String totalprogrammecount = "select count(srno) from bproject_master";

	public static String total_researcher = "select count(srno) from researcher_master";

	public static String insert_invited_researcher = "insert into b_invited_researcher(programid, emp_srno) values (?, ?)";

	public static String delete_invited_researcher = "delete from b_invited_researcher where programid = ?";


	public static String get_invited_researcher = "select distinct empdtls.srno, empdtls.avatar, empdtls.profile_pic, (Select sum(total_reward) from b_reward_earned where researcherid IN (select emp_id from researcher_master where srno = empdtls.srno)) AS total_bounty_earned from b_invited_researcher invRe, researcher_master empdtls where programid = ? AND invRe.emp_srno=empdtls.srno";

	public static String ifInvited = "Select * from b_invited_researcher where emp_srno IN (select srno from researcher_master where emp_id = ?) AND programid = ?";

	public static String insertProgramAction = "insert into bproject_actions(projectid, status, empid, remarks) values (?, ?, ?, 'Program Updated')";


	public static String checkSuspendedResearchers = "Insert into researcher_join_programme (reseacher_id, project_id, researcher_profile_id,createddate,status,active,revoketill) \r\n"
			+ "select reseacher_id, project_id, researcher_profile_id, now(), 'Joined', TRUE, null from researcher_join_programme where status = 'Suspend' AND revoketill < now()  AND active = 'true';\r\n"
			+ "\r\n"
			+ "update researcher_join_programme set active = 'false' where status = 'Suspend' AND revoketill < now()  AND active = 'true'\r\n"
			+ "";

	public static String checkReported = "select * from bb_vulnerability where programid=? and researcherid=?";

	public static String checkAllReported = "select * from bb_vulnerability where researcherid=?";

	public static String getMyProject = "Select * from bproject_master where  bproject_ownemp_id=? order by srno desc ";


	public static String deleteAnnouncement = "delete from bprojectannouncement_child where srno=?";

	public static String getAnnouncementData = "select * from bproject_master where srno IN (Select masterrannouncementid from bprojectannouncement_child where srno=?);";


public static String getJoinedProject =
    "SELECT srno, bprogramme_name, bprojectstart_date, bprojectend_date, " +
    "       bproject_api, bproject_tech, bproject_description, bappln_criticality, " +
    "       createddate, staticpage, dynamicpage, roles, ptype, status " +
    "FROM bproject_master " +
    "WHERE srno IN ( " +
    "    SELECT project_id " +
    "    FROM researcher_join_programme " +
    "    WHERE reseacher_id = ? " +
    "      AND status = 'Joined' " +
    "      AND active = TRUE " + // If VARCHAR: use 'true'
    ")";

	// Vulnerability Query Constants

public static String getJoinedProjectForAdmin =
    "SELECT * " +
    "FROM bproject_master " +
    "WHERE srno IN ( " +
    "    SELECT project_id " +
    "    FROM researcher_join_programme " +
    "    WHERE reseacher_id = ? " +
    "      AND status = 'Joined' " +
    "      AND active = TRUE " + // If VARCHAR: AND active = 'true'
    ");";

	public static String getPoints = "select points from researcher_master where emp_id=?";

	public static String updatePoints = "update researcher_master set points= ? where emp_id=?";


	public static final String GET_ALL_VULN = "select * from bb_vulnerability";

	public static final String GET_VULN_BY_ID = "select *, (select bprogramme_name from bproject_master where srno= programid) AS bprogramme_name, (select avatar from researcher_master where emp_id = researcherid) AS researcherName, (select srno from researcher_master where emp_id = researcherid) AS srno from bb_vulnerability where id = ?";

	public static final String GET_RECENT_SRNO = "SELECT max(ID) FROM bb_vulnerability";

	public static final String GET_VULN_BY_PROJECT = "SELECT v.*,(SELECT vc.vrt_category FROM b_vulnerability_category AS vc WHERE vc.srno = v.assessmenttype) AS vrt_category, (SELECT rm.avatar FROM researcher_master AS rm WHERE rm.emp_id = v.researcherid) AS avatar FROM bb_vulnerability AS v WHERE v.programid = ? ORDER BY v.identificationdt DESC";

	public static final String UPDATE_VULN_STATUS = "update bb_vulnerability set status = ? where id=?;"
			+ "insert into bb_vulnerability_actions (vulnid, status, empid, remarks) VALUES(?,?,?,?);";

	public static final String INSERT_REWARD = "INSERT INTO b_reward_earned(researcherid, vulnid, approvedby, total_reward, active,createddate) VALUES ((select researcherid from bb_vulnerability where id=?), ?, ?, ?, TRUE,now())";

	public static final String UPDATE_REWARD = "update b_reward_earned set active = FALSE where vulnid=?";


	public static final String GET_HISTORY_BASED_ON_ID = "select *  from bb_vulnerability_actions where vulnid=? order by createddt desc";

	public static String ifVulnReported = "Select * from bb_vulnerability where researcherid=? AND id=? ";


	public static String getVulnCategory = "Select * from b_vulnerability_category";


	public static String getTempAccessList = "Select *, (select full_name from ssa_eai_hr_emp_basic_dtls where employee_number = access_to :: text) from bb_vulnerability_access where vulnid = ? AND access_till > NOW()";

	public static String provideTempAccess = "insert into bb_vulnerability_access(vulnid, access_to, access_till, createdby) values(?, ?, ?, ?)";

	public static String vulnStatistics = "select * from bb_vulnerability_actions where vulnid = ? order by createddt DESC";

	public static String updateTempAccess = "update bb_vulnerability_access set access_till = ? where vulnid = ? AND access_to = ?";

	public static String hasTempAccess = "Select * from bb_vulnerability_access where access_to = ? AND vulnid = ? AND access_till > NOW()";

	public static String hasAccessEntry = "Select * from bb_vulnerability_access where access_to = ? AND vulnid = ?";

	public static String isEmployeePresent = "Select EXISTS (Select * from bb_vulnerability_access where access_to = ? AND createdby = ? and vulnid = ?)";

	public static String AccessDelete = "DELETE from bb_vulnerability_access WHERE access_to = ?  and createdby = ? and vulnid = ? ";

	public static String isEmployeeMatchesWithETLData = "Select EXISTS (Select * from ssa_eai_hr_emp_basic_dtls where employee_number = ?)";


	public static String getAllReaserchers = "\n"
			+ "SELECT\n"
			+ "  master.srno,\n"
			+ "  master.emp_id,\n"
			+ "  master.emp_name,\n"
			+ "  master.emp_bio,\n"
			+ "  master.skills,\n"
			+ "  master.emp_avatar_blob,\n"
			+ "  master.createddate,\n"
			+ "  master.avatar,\n"
			+ "  master.teams,\n"
			+ "  master.profile_pic,\n"
			+ "  master.points,\n"
			+ "  CAST(COALESCE(SUM(reward.total_reward), 0) AS INT) AS total_bounty_earned\n"
			+ "FROM researcher_master AS master\n"
			+ "LEFT JOIN b_reward_earned AS reward\n"
			+ "  ON master.emp_id = reward.researcherid\n"
			+ "     AND reward.active = TRUE -- If BOOLEAN in H2. If it's VARCHAR, change to 'TRUE'\n"
			+ "GROUP BY\n"
			+ "  master.srno,\n"
			+ "  master.emp_id,\n"
			+ "  master.emp_name,\n"
			+ "  master.emp_bio,\n"
			+ "  master.skills,\n"
			+ "  master.emp_avatar_blob,\n"
			+ "  master.createddate,\n"
			+ "  master.avatar,\n"
			+ "  master.teams,\n"
			+ "  master.profile_pic,\n"
			+ "  master.points\n"
			+ "ORDER BY\n"
			+ "  SUM(reward.total_reward) DESC NULLS LAST;\n";


	public static String getResearcherById = "Select * from researcher_master WHERE srno=?";

	public static String updateResearchers = "UPDATE researcher_master SET  emp_bio=?, skills = ?,  profile_pic=? WHERE srno=?";

	public static String getBountyForID = "select SUM(total_reward) as total_bounty_earned from b_reward_earned where researcherid = ? AND active = TRUE";

	public static String getAllhuntHistory = "select distinct(vrt_category) AS title, count(*) AS vulncount from bb_vulnerability, b_vulnerability_category category where assessmenttype = category.srno group by vrt_category";

	public static String particular_hunt_history = "select distinct(vrt_category) AS title, count(*) as vuln_count from bb_vulnerability, b_vulnerability_category category where assessmenttype = category.srno AND researcherid = ? group by vrt_category";

public static String detailed_particular_huntHistory = "\n"
			+ "SELECT\n"
			+ "  v.*,\n"
			+ "  p.bprogramme_name,\n"
			+ "  (SELECT r.total_reward\n"
			+ "   FROM b_reward_earned r\n"
			+ "   WHERE r.vulnid = v.id AND r.active = TRUE\n"
			+ "   -- Uncomment next line if there can be multiple active rows\n"
			+ "   -- ORDER BY r.total_reward DESC\n"
			+ "   -- LIMIT 1\n"
			+ "  ) AS total_reward,\n"
			+ "  (SELECT c.vrt_category\n"
			+ "   FROM b_vulnerability_category c\n"
			+ "   WHERE c.srno = v.assessmenttype\n"
			+ "  ) AS vrt_category\n"
			+ "FROM bb_vulnerability AS v\n"
			+ "JOIN bproject_master AS p\n"
			+ "  ON p.srno = v.programid\n"
			+ "WHERE v.researcherid = ?";
 
	

	public static String insertResearcherRole = "insert into b_employee_role_mapping (employee_srno, roleid) select ?, (SELECT srno from b_roles where role_name ='Researcher')";

	public static String updateResearcherPicture = "UPDATE researcher_master SET profile_pic = ? WHERE srno=?";

	public static String isAvatarAvailable = "SELECT EXISTS(select * from researcher_master where UPPER(avatar) = ?)";

	public static String whitelistUser = "insert into b_employee_role_mapping (employee_srno, roleid) values(?,(SELECT srno from b_roles where role_name ='Visitor'))";

	public static String getResearcherProfileByEmpId = "Select * from researcher_master WHERE emp_id=?";

	// Help Query Constants

	public static String gethelpdetails = "select * from help";

	public static String addhelp = "INSERT INTO help (name,description,parentsrno) values(?,?,?)";

	public static String getWord = "Select * from b_help_wordcloud where word=?";

	public static String insertWord = "insert into b_help_wordcloud (word,count) values (?,1)";

	public static String updateCount = "Update b_help_wordcloud set count=? where word=?";

	public static String deletehelp = "delete from help where srno=?";

	public static String updatehelp = "Update help set name=?, description=?, parentsrno=? where srno=?";

	// Manage Options Constants

	public static String getOptions = "select lookupcode,lookupvalue,lookupdescription,orderseq from bb_lookup_data where lookupgroup = ? AND ACTIVE = TRUE";

	public static String getVulnCat = "select * from b_vulnerability_category";

	public static String GET_ALL_OPTIONS = "SELECT id, lookupgroup, lookupcode, lookupvalue, lookupdescription, createdby, createddt, updatedby, updateddt, active, orderseq from bb_lookup_data where active=true order by lookupgroup, lookupcode";

	public static String isActionAllowed = "Select EXISTS(Select * from b_login_details where emp_id=?)";

	public static String insertUser = "INSERT INTO b_login_details(emp_id,emp_fullname,emp_name,emp_surname) values(?,((select full_name from ssa_eai_hr_emp_basic_dtls where employee_number = ? :: text)),((select first_name from ssa_eai_hr_emp_basic_dtls where employee_number = ? :: text)),((select last_name from ssa_eai_hr_emp_basic_dtls where employee_number = ? :: text)))";

	// Blog Query Constant

	public static String getBlogs = "select * from b_blog";

	public static String deleteBlog = "delete from b_blog where srno=?";

	public static String updateBlog = "UPDATE b_blog set title=?,brief=?,category=? where srno=?";


	public static String AllQuery = "SELECT *,(select full_name from ssa_eai_hr_emp_basic_dtls where employee_number = b_query.createdby::text) FROM b_query;";

	public static String getData = "select * from b_help_wordcloud";

	public static String isResearcher = "Select EXISTS(Select * from researcher_master where emp_id=?)";


	public static String GETResources = "SELECT id , guidelines , paneltitle , paneldescription , entries from b_parentstandardguidelines, b_standardguidelines WHERE b_parentstandardguidelines.srno = b_standardguidelines.masterid ";

	public static String ChildGuidelines = "SELECT * from b_standardguidelines WHERE masterid = ?";

	public static String StdGuidelines = "SELECT * from b_parentstandardguidelines";



	public static String InsertResources = "INSERT into b_parentstandardguidelines (guidelines) values ( ? )";

	public static String InsertResourcesitem = "insert into b_standardguidelines (masterid,paneltitle, paneldescription, entries) values( ? , ? , ? , ?)";

	public static String UpdateResources = "Update b_standardguidelines set masterid = ?,paneltitle = ?, paneldescription = ?, entries = ? where id = ?";

	public static String getresourcesbyid = "Select * FROM b_standardguidelines WHERE id = ?";

	public static String DeleteResources = "DELETE FROM b_standardguidelines WHERE id = ?";

	// Analaytics Query
	public static String VulnCat = "select distinct(vrt_category) AS name,count(*) As y,vrt_category AS drilldown  from bb_vulnerability, b_vulnerability_category category where assessmenttype = category.srno group by vrt_category";

	public static String BountyPaid = "select vulnerability.id,vulnerability.programid,vulnerability.status, project.bprogramme_name, (select total_reward from b_reward_earned reward where reward.vulnid=vulnerability.id AND reward.active = true) from bb_vulnerability vulnerability, bproject_master project where project.srno = vulnerability.programid ";

	// Query Section Query constant
	public static String PostQuery = "INSERT into b_query ( createdby ,query,publish,active) VALUES (? , ? , ?,?)";

	
	public static String deleteEmployee = "DELETE FROM b_employee_role_mapping WHERE srno = ?";

	public static String UpdateQuery = "UPDATE b_query SET answer = ? , answerby = ?  WHERE srno = ? and active=?";

	public static String UpdateActive = "update b_query A set active=? where A.answer IS NOT NULL";


//FAQ Queries

	public static String FaqPostQuery = " insert into b_faq (faqtitle,description) VALUES(?, ?)";

	public static String FaqDelete = "DELETE FROM b_faq WHERE srno = ?";

	public static String FaqAllQuery = "select * from  b_faq";

	public static String FaqUpdate = "Update b_faq set faqtitle=?, description=? WHERE srno=?";

	public static String Myquery = "select * from b_query where createdby=?";

	public static String getUsers = "select * from b_login_details";

	// DynamicDropdown


	public static String GETDROPDOWNS = "select * from b_dropdowns";

	// Menu Queries
	public static String getMenus = "select * from b_menus";

	public static String addMenu = "insert into b_menus(menuname,link,role,status,application,sequenceorder,parentsrno) values(?,?,?,'Active','Bughuntr',1,0)";

	public static String deleteMenu = "delete from b_menus where srno=?";

	public static String updateMenu = "update b_menus set menuname=?,link=?,role=? where srno=?";

	static {
		BUGHUNTR_OPTION_EXISTS.append(
				"SELECT 1 FROM bb_lookup_data WHERE lookupvalue = ? and lookupgroup = ? AND lookupdescription=? AND ACTIVE = TRUE");
	}

	// History

	public static String GETHistory = "SELECT * FROM b_bughuntr_announcement";


	public static String updateHistory = "UPDATE b_bughuntr_announcement SET fontname = ?,  releaseverinfo=?, releaseitemname=? WHERE srno=?";

	public static String deleteHistory = "DELETE FROM b_bughuntr_announcement WHERE srno = ?";

	// ETL
	public static String ETLquey = "SELECT * FROM ssa_eai_hr_emp_basic_dtls ORDER BY person_id DESC  LIMIT 50";

	public static String PostEtl = "INSERT INTO ssa_eai_hr_emp_basic_dtls (person_id,employee_number,first_name,last_name,full_name,email_address,iou_name,assignment_id,assignment_status,base_branch,depute_branch,country_of_depute,base_dc_id,per_system_status,last_update_date,eai_update_date) VALUES((SELECT MAX(person_id) from ssa_eai_hr_emp_basic_dtls)+1,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now())";

	// role exists for bounty

	public static String BOUNTY_ROLE_EXISTS = "select count(*) from b_employee_role_mapping where employee_srno = ? AND roleid = ?";

	public static String isAuthorized = "Select EXISTS(Select * from b_employee_role_mapping where employee_srno=?)";

	public static String isUserPresent = "Select EXISTS(Select * from ssa_eai_hr_emp_basic_dtls where employee_number = ?)";
}
