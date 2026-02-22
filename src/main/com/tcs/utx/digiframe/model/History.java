package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = false)
public class History {

	private int srno;
	@NotBlank @Size(max = 255)
	private String fontname;
	@NotBlank @Size(max = 500)
	private String releaseverinfo;
	@NotBlank @Size(max = 1000)
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
