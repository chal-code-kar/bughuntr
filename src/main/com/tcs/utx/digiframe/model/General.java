package com.tcs.utx.digiframe.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = false)
public class General {
	
	
    private String program_name;
    private int owner_empid;
    private int srno;
    private String owner_name;
    private Date startdate;
    private Date enddate;
    private String api;
    private String technology;
    private String description;
    private String criticality;
    private String unit;
    private int staticpage;
    private String ptype;
    private String status;
    private int dynamicpage;
    private String roles;
    
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
    public int getSrno() {
        return srno;
    }
    public void setSrno(int srno) {
        this.srno = srno;
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
    public String getApi() {
        return api;
    }
    public void setApi(String api) {
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
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    
    
}