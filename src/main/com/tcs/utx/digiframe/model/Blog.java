package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Blog{
	
	int srno;
	private String title;
	private String brief;
	private String category;
	
	public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	
	 public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBrief() {
			return brief;
		}
		public void setBrief(String brief) {
			this.brief = brief;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		
		
    

}
