package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public class History {
	
	private int srno;
	private String fontname;
	private String releaseverinfo;
	private String releaseitemname;

	public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	public String getFontname() {
		return fontname;
	}
	public void setFontname(String fontname) {
		this.fontname = fontname;
	}
	public String getReleaseverinfo() {
		return releaseverinfo;
	}
	public void setReleaseverinfo(String releaseverinfo) {
		this.releaseverinfo = releaseverinfo;
	}
	public String getReleaseitemname() {
		return releaseitemname;
	}
	public void setReleaseitemname(String releaseitemname) {
		this.releaseitemname = releaseitemname;
	}
	
}
