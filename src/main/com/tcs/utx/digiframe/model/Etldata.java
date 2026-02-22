
package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = false)
	public class Etldata {

		public Etldata() {
			
		}
		

		private int person_id;
		@Min(1000) @Max(999999)
		private int employee_number;
		@NotBlank @Size(max = 100)
		private String first_name;
		@NotBlank @Size(max = 100)
		private String last_name;
		@Size(max = 200)
		private String full_name;
		@Email @Size(max = 255)
		private String email_address;
		@Size(max = 255)
		private String iou_name;
		private int assignment_id;
		@Size(max = 50)
		private String assignment_status;
		@Size(max = 255)
		private String base_branch;
		@Size(max = 255)
		private String depute_branch;
		@Size(max = 100)
		private String country_of_depute;
		private int base_dc_id;
		@Size(max = 50)
		private String per_system_status;
		private int last_update_date;
		private int eai_update_date;
		public int getPerson_id() {
			return person_id;
		}
		public void setPerson_id(int person_id) {
			this.person_id = person_id;
		}
		public int getEmployee_number() {
			return employee_number;
		}
		public void setEmployee_number(int employee_number) {
			this.employee_number = employee_number;
		}
		public String getFirst_name() {
			return first_name;
		}
		public void setFirst_name(String first_name) {
			this.first_name = first_name;
		}
		public String getLast_name() {
			return last_name;
		}
		public void setLast_name(String last_name) {
			this.last_name = last_name;
		}
		public String getFull_name() {
			return full_name;
		}
		public void setFull_name(String full_name) {
			this.full_name = full_name;
		}
		public String getEmail_address() {
			return email_address;
		}
		public void setEmail_address(String email_address) {
			this.email_address = email_address;
		}
		public String getIou_name() {
			return iou_name;
		}
		public void setIou_name(String iou_name) {
			this.iou_name = iou_name;
		}
		public int getAssignment_id() {
			return assignment_id;
		}
		public void setAssignment_id(int assignment_id) {
			this.assignment_id = assignment_id;
		}
		public String getAssignment_status() {
			return assignment_status;
		}
		public void setAssignment_status(String assignment_status) {
			this.assignment_status = assignment_status;
		}
		public String getBase_branch() {
			return base_branch;
		}
		public void setBase_branch(String base_branch) {
			this.base_branch = base_branch;
		}
		public String getDepute_branch() {
			return depute_branch;
		}
		public void setDepute_branch(String depute_branch) {
			this.depute_branch = depute_branch;
		}
		public String getCountry_of_depute() {
			return country_of_depute;
		}
		public void setCountry_of_depute(String country_of_depute) {
			this.country_of_depute = country_of_depute;
		}
		public int getBase_dc_id() {
			return base_dc_id;
		}
		public void setBase_dc_id(int base_dc_id) {
			this.base_dc_id = base_dc_id;
		}
		public String getPer_system_status() {
			return per_system_status;
		}
		public void setPer_system_status(String per_system_status) {
			this.per_system_status = per_system_status;
		}
		public int getLast_update_date() {
			return last_update_date;
		}
		public void setLast_update_date(int last_update_date) {
			this.last_update_date = last_update_date;
		}
		public int getEai_update_date() {
			return eai_update_date;
		}
		public void setEai_update_date(int eai_update_date) {
			this.eai_update_date = eai_update_date;
		}
		
		
		
	}



