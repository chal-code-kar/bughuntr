package com.tcs.utx.digiframe.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class TempAccess {
	
	
    int empid;
    int access_to;
    Date date = new Date();

    public int getEmpid() {
        return empid;
    }
    public void setEmpid(int empid) {
        this.empid = empid;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    
    public int getAccess_to() {
		return access_to;
	}
	public void setAccess_to(int access_to) {
		this.access_to = access_to;
	}
}
