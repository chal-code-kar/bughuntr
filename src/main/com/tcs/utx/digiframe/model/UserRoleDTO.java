package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = false)
public class UserRoleDTO {
	
	
	private String employee_name;
	private String roles;
	
	public String getEmployeeName() {
		return employee_name;
	}
	public void setEmployeeName(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}

}
