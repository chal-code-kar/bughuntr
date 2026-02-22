package com.tcs.utx.digiframe.dto;

public class BrandingDTO {
	private String userId;
	private String firstName;
	private String lastName;
	private String icalmsRole;
	private String projectName;
	private String supervisorName;
	private String lastLogin;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getIcalmsRole() {
		return icalmsRole;
	}
	public void setIcalmsRole(String icalmsRole) {
		this.icalmsRole = icalmsRole;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	
}
