package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {
	
	private int srno;
	private String menuname;
	private int sequenceorder;
	private int parentsrno;
	private String link;
	private String role;
	
	public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	public int getSequenceorder() {
		return sequenceorder;
	}
	public void setSequenceorder(int sequenceorder) {
		this.sequenceorder = sequenceorder;
	}
	public int getParentsrno() {
		return parentsrno;
	}
	public void setParentsrno(int parentsrno) {
		this.parentsrno = parentsrno;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
