package com.tcs.utx.digiframe.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = false)
public class joinedEmploye {
	
	
    private int srno;
    private String status;
    private String old_status;
    private int reseacher_id;
    private Integer project_id;
    private Integer researcher_profile_id;
    private Date suspendTill;
    
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getOld_status() {
		return old_status;
	}
	public void setOld_status(String old_status) {
		this.old_status = old_status;
	}

    public int getSrno() {
        return srno;
    }
    public void setSrno(int srno) {
        this.srno = srno;
    }
    
    public Integer getResearcher_profile_id() {
        return researcher_profile_id;
    }
    public void setResearcher_profile_id(Integer researcher_profile_id) {
        this.researcher_profile_id = researcher_profile_id;
    }
    
    public int getReseacher_id() {
        return reseacher_id;
    }
    public void setReseacher_id(int reseacher_id) {
        this.reseacher_id = reseacher_id;
    }
    public Integer getProject_id() {
        return project_id;
    }
    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }
	public Date getSuspendTill() {
		return suspendTill;
	}
	public void setSuspendTill(Date suspendTill) {
		this.suspendTill = suspendTill;
	}
    
    

    
}
