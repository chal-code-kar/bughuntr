package com.tcs.utx.digiframe.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
@JsonIgnoreProperties(ignoreUnknown = false)
public class hasAccess {

	@NotNull
	private Date access_till;
	@Min(1)
	private int access_to;
	private int createdby;
	private Date createddt;
	private int srno;
	@Min(1)
	private int vulnid;
	
	public Date getAccess_till() {
		return access_till;
	}
	public void setAccess_till(Date access_till) {
		this.access_till = access_till;
	}
	public int getAccess_to() {
		return access_to;
	}
	public void setAccess_to(int access_to) {
		this.access_to = access_to;
	}
	public int getCreatedby() {
		return createdby;
	}
	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}
	public Date getCreateddt() {
		return createddt;
	}
	public void setCreateddt(Date createddt) {
		this.createddt = createddt;
	}
	public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	public int getVulnid() {
		return vulnid;
	}
	public void setVulnid(int vulnid) {
		this.vulnid = vulnid;
	}
	
	
}
