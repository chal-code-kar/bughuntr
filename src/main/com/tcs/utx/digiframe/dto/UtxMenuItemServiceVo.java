package com.tcs.utx.digiframe.dto;

public class UtxMenuItemServiceVo {

	private int id;
	private String text;
	private double order_no;
	private int parentid;
	private String url;

	public UtxMenuItemServiceVo() {
	}

	public UtxMenuItemServiceVo(int id, String text, double order_no, int parentid, String url) {
		this.id = id;
		this.text = text;
		this.order_no = order_no;
		this.parentid = parentid;
		this.url = url;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getOrder_no() {
		return this.order_no;
	}

	public void setOrder_no(float order_no) {
		this.order_no = order_no;
	}

	public int getParentid() {
		return this.parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.id == ((UtxMenuItemServiceVo) obj).getId()) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + getId();
		return result;
	}

}
