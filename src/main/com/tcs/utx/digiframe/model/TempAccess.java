package com.tcs.utx.digiframe.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
@JsonIgnoreProperties(ignoreUnknown = false)
public class TempAccess {


    @Min(1)
    int empid;
    @Min(1)
    int access_to;
    @NotNull
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
