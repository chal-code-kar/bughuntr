package com.tcs.utx.digiframe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Employee {

	private String apm_id;
    private String program_name;
    private int owner_empid;
    private int srno;
    private String overview;
    private String owner_name;
    private Date startdate;
    private Date enddate;
    private int api;			
    private String technology;
    private String description;
    private String criticality;
    private String unit;
    private int staticpage;		
    private String ptype;
    private String status;
    private int dynamicpage;
    private int roles;
    private String access;
    private String resources;
    private List<Jobs> jobs;
    private List<Rules> rules;
    private String inScope;
    private List<Announcements> announcements;
    private String outScope;
    private List<Integer> researcherList = new ArrayList<>();

    
    public String getApm_id() {
        return apm_id;
    }
    public void setApm_id(String apm_id) {
        this.apm_id = apm_id;
    }
    public String getProgram_name() {
        return program_name;
    }
    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }
    public int getOwner_empid() {
        return owner_empid;
    }
    public void setOwner_empid(int owner_empid) {
        this.owner_empid = owner_empid;
    }
    public String getOwner_name() {
        return owner_name;
    }
    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
    public Date getStartdate() {
        return startdate;
    }
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }
    public Date getEnddate() {
        return enddate;
    }
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
    public int getApi() {
        return api;
    }
    public void setApi(int api) {
        this.api = api;
    }
    public String getTechnology() {
        return technology;
    }
    public void setTechnology(String technology) {
        this.technology = technology;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCriticality() {
        return criticality;
    }
    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public int getStaticpage() {
        return staticpage;
    }
    public void setStaticpage(int staticpage) {
        this.staticpage = staticpage;
    }
    public String getPtype() {
        return ptype;
    }
    public void setPtype(String ptype) {
        this.ptype = ptype;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getDynamicpage() {
        return dynamicpage;
    }
    public void setDynamicpage(int dynamicpage) {
        this.dynamicpage = dynamicpage;
    }
    public int getRoles() {
        return roles;
    }
    public void setRoles(int roles) {
        this.roles = roles;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
   
    public String getAccess() {
        return access;
    }
    public void setAccess(String access) {
        this.access = access;
    }
    public String getResources() {
        return resources;
    }
    public void setResources(String resources) {
        this.resources = resources;
    }
    
    public List<Jobs> getJobs() {
        return jobs;
    }
    public void setJobs(List<Jobs> jobs) {
        this.jobs = jobs;
    }

    public List<Rules> getRules() {
        return rules;
    }
    public void setRules(List<Rules> rules) {
        this.rules = rules;
    }

    public List<Announcements> getAnnouncements() {
        return announcements;
    }
    public void setAnnouncements(List<Announcements> announcements) {
        this.announcements = announcements;
    }

    public String getInScope() {
        return inScope;
    }
    public void setInScope(String inScope) {
        this.inScope = inScope;
    }
    public String getOutScope() {
        return outScope;
    }
    public void setOutScope(String outScope) {
        this.outScope = outScope;
    }
    public int getSrno() {
        return srno;
    }
    public void setSrno(int srno) {
        this.srno = srno;
    }
    public List<Integer> getResearcherList() {
        return researcherList;
    }
    public void setResearcherList(List<Integer> researcherList) {
        this.researcherList = researcherList;
    }
   
}