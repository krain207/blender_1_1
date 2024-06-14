package com.imeritz.blender.comm.beans;

public class BlenderKakaSendBean {
	private String prosClsCode ;
	private String acno;
	private String asno;
	private String codeVal;
	private String codeValdVal;
	
	
	public BlenderKakaSendBean() {

		this.prosClsCode = "";
		this.acno = "";
		this.asno = "";
		this.codeVal = "";
		this.codeValdVal = "";
	}


	public String getProsClsCode() {
		return prosClsCode;
	}


	public void setProsClsCode(String prosClsCode) {
		this.prosClsCode = prosClsCode;
	}


	public String getAcno() {
		return acno;
	}


	public void setAcno(String acno) {
		this.acno = acno;
	}


	public String getAsno() {
		return asno;
	}


	public void setAsno(String asno) {
		this.asno = asno;
	}


	public String getCodeVal() {
		return codeVal;
	}


	public void setCodeVal(String codeVal) {
		this.codeVal = codeVal;
	}


	public String getCodeValdVal() {
		return codeValdVal;
	}


	public void setCodeValdVal(String codeValdVal) {
		this.codeValdVal = codeValdVal;
	}
	
	
	
	
}
