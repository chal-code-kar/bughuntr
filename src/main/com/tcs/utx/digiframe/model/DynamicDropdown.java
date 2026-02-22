package com.tcs.utx.digiframe.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties(ignoreUnknown = false)
public class DynamicDropdown {

	private int id;
	@NotBlank @Size(max = 100)
	private String lookupgroup;
	@NotBlank @Size(max = 100)
	private String lookupcode;
	@NotBlank @Size(max = 255)
	private String lookupvalue;
	@Size(max = 1000)
	private String lookupdescription;
	private String createdby;
	private Date createddt;
	private String updatedby;
	private Date updateddt;
	private boolean active;
	private int orderseq;
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLookupgroup() {
		return lookupgroup;
	}

	public void setLookupgroup(String lookupgroup) {
		this.lookupgroup = lookupgroup;
	}

	public String getLookupcode() {
		return lookupcode;
	}

	public void setLookupcode(String lookupcode) {
		this.lookupcode = lookupcode;
	}

	public String getLookupvalue() {
		return lookupvalue;
	}

	public void setLookupvalue(String lookupvalue) {
		this.lookupvalue = lookupvalue;
	}

	public String getLookupdescription() {
		return lookupdescription;
	}

	public void setLookupdescription(String lookupdescription) {
		this.lookupdescription = lookupdescription;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddt() {
		return createddt;
	}

	public void setCreateddt(Date createddt) {
		this.createddt = createddt;
	}

	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	public Date getUpdateddt() {
		return updateddt;
	}

	public void setUpdateddt(Date updateddt) {
		this.updateddt = updateddt;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOrderseq() {
		return orderseq;
	}

	public void setOrderseq(int orderseq) {
		this.orderseq = orderseq;
	}

	

}
