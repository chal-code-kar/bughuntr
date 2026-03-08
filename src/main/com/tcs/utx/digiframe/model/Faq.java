package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Faq {


	private int srno;
	@NotBlank @Size(max = 500)
	private String faqtitle;
	@NotBlank @Size(max = 5000)
	private String description;
	
	public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	public String getFaqtitle() {
		return faqtitle;
	}
	public void setFaqtitle(String faqtitle) {
		this.faqtitle = faqtitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
