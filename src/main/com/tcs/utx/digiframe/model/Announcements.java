package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public class Announcements {
	 
	public int srno;
	public int masterannouncementid;
    public String announcement_category;
    public String announcement_details;
    
    public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	public int getMasterannouncementid() {
		return masterannouncementid;
	}
	public void setMasterannouncementid(int masterannouncementid) {
		this.masterannouncementid = masterannouncementid;
	}
    public String getAnnouncement_category() {
        return announcement_category;
    }
    public void setAnnouncement_category(String announcement_category) {
        this.announcement_category = announcement_category;
    }
    public String getAnnouncement_details() {
        return announcement_details;
    }
    public void setAnnouncement_details(String announcement_details) {
        this.announcement_details = announcement_details;
    }

    
    
}
