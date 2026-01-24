package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resources {	
	
	
	private int srno;
	private String paneltitle;
	private String paneldescription ;
	private String entries;
	private String guidelines;
	private int masterid;
	
	public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	public String getPaneltitle() {
		return paneltitle;
	}
	public void setPaneltitle(String paneltitle) {
		this.paneltitle = paneltitle;
	}
	public String getPaneldescription() {
		return paneldescription;
	}
	public void setPaneldescription(String paneldescription) {
		this.paneldescription = paneldescription;
	}
	public String getEntries() {
		return entries;
	}
	public void setEntries(String entries) {
		this.entries = entries;
	}
	public String getGuidelines() {
		return guidelines;
	}
	public void setGuidelines(String guidelines) {
		this.guidelines = guidelines;
	}
	public int getMasterid() {
		return masterid;
	}
	public void setMasterid(int masterid) {
		this.masterid = masterid;
	}

}
