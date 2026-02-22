package com.tcs.utx.digiframe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class BrandingDTO {
	@NotBlank @Size(max = 50)
	@Pattern(regexp = "^[a-zA-Z0-9_.-]+$")
	private String userId;

	@NotBlank @Size(max = 100)
	@Pattern(regexp = "^[a-zA-Z .'-]+$")
	private String firstName;

	@NotBlank @Size(max = 100)
	@Pattern(regexp = "^[a-zA-Z .'-]+$")
	private String lastName;

	@Size(max = 100)
	private String icalmsRole;

	@Size(max = 200)
	private String projectName;

	@Size(max = 200)
	private String supervisorName;

	@Size(max = 50)
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
