package com.imeritz.blender.comm.beans;

public class LognMangBean {
	private String userId; // 사용자ID
	private String userPwd; // 사용자ID 비밀번호
	private String pbcrPwd; // 공인인증서 비밀번호
	private String selTimeOut; // 공인인증서 비밀번호

	private String elctSgntUseYn; // 전자서명사용여부
	private String connPwdInitYn; // 접속비밀번호초기화여부
	private String authAthnIstuDsblYn; // 공인인증기관장애여부
	private String pwdMstkAbleTmnu; // 비밀번호오류가능횟수
	private String connPrmsClsCode; // 접속허용구분코드
	private String emplCustConnClsCode; // 직원고객접속구분코드
	private String connWarnClsCode; // 접속경고구분코드
	private String sstmDate; // 시스템일자
	private String userName; // 사용자명
	private String pwdErrTmnu; // 비밀번호에러횟수
	private String pbcrPwdMstkTmnu; // 공인인증서비밀번호오류횟수
	private String bsopDate; // 영업일자
	private String rcno; // 실명확인번호
	private String pbcrPwdMstkAble; // 공인인증서비밀번호오류가능
	private String connPwdModJobYn; // 접속비밀번호변경작업여부
	private String ownSecuMdiaClsCode; // 소유보안매체구분코드
	private String csno; // 고객번호
	private String menuAthrClsCode; // 메뉴권한구분코드
	private String useStaTime; // 사용시작시각
	private String d1BefBsopDate; // 1일전영업일자
	private String lognSparDomnCntt; // 로그인예비영역내용
	private String secuMdiaPwdMstkTmnu; // 보안매체비밀번호오류횟수
	private String emailAddr; // 메일주소
	private String loginClsCode; // 로그인 구분 코드 1=공인인증서 , 2=일반로그인
	private String dnData; // 인증서 dn값
	private String sessionKey;	//세션키
	private String authAthnKeyLngt;	//공인인증 키 길이
	private String authAthnKeyVal;	//공인인증 키 값
	//2012.08.09 남웅주 추가
	private String userIp;	//사용자 접속IP (공인)
	private String userPrivIp;	//사용자 접속IP (사설)
	//2013.10.31 Exture Plus 추가
	private String macAddrF;  // Mac 주소 앞 10자리
	private String macAddrR;  // Mac 주소 뒤 2자리
	private String macAddrType;  // Mac 주소 타입 0: 공인인증서, 1:Socket (Flash)		
	private String discSN;  // 디스크 일련번호		
	private String rcnoPip;
	private String ntatSrno;
	
	public LognMangBean() {
		this.userId = "";
		this.userPwd = "";
		this.pbcrPwd = "";
		this.selTimeOut = "";
		this.elctSgntUseYn = "";
		this.connPwdInitYn = "";
		this.authAthnIstuDsblYn = "";
		this.pwdMstkAbleTmnu = "";
		this.connPrmsClsCode = "";
		this.emplCustConnClsCode = "";
		this.connWarnClsCode = "";
		this.sstmDate = "";
		this.userName = "";
		this.pwdErrTmnu = "";
		this.pbcrPwdMstkTmnu = "";
		this.bsopDate = "";
		this.rcno = "";
		this.pbcrPwdMstkAble = "";
		this.connPwdModJobYn = "";
		this.ownSecuMdiaClsCode = "";
		this.csno = "";
		this.menuAthrClsCode = "";
		this.useStaTime = "";
		this.d1BefBsopDate = "";
		this.lognSparDomnCntt = "";
		this.secuMdiaPwdMstkTmnu = "";
		this.emailAddr = "";
		this.loginClsCode = "";
		this.dnData = "";
		this.sessionKey = "";
		this.authAthnKeyLngt = "";
		this.authAthnKeyVal = "";
		this.userIp = "";
		this.userPrivIp = "";
		this.macAddrF = "";  // Mac 주소 앞 10자리
		this.macAddrR = "";  // Mac 주소 뒤 2자리
		this.macAddrType = "";  // Mac 주소 타입 0: 공인인증서, 1:Socket (Flash)		
		this.discSN = "";  // 디스크 일련번호	
		this.rcnoPip = "";
		this.ntatSrno = "";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getPbcrPwd() {
		return pbcrPwd;
	}

	public void setPbcrPwd(String pbcrPwd) {
		this.pbcrPwd = pbcrPwd;
	}

	public String getSelTimeOut() {
		return selTimeOut;
	}

	public void setSelTimeOut(String selTimeOut) {
		this.selTimeOut = selTimeOut;
	}

	public String getElctSgntUseYn() {
		return elctSgntUseYn;
	}

	public void setElctSgntUseYn(String elctSgntUseYn) {
		this.elctSgntUseYn = elctSgntUseYn;
	}

	public String getConnPwdInitYn() {
		return connPwdInitYn;
	}

	public void setConnPwdInitYn(String connPwdInitYn) {
		this.connPwdInitYn = connPwdInitYn;
	}

	public String getAuthAthnIstuDsblYn() {
		return authAthnIstuDsblYn;
	}

	public void setAuthAthnIstuDsblYn(String authAthnIstuDsblYn) {
		this.authAthnIstuDsblYn = authAthnIstuDsblYn;
	}

	public String getPwdMstkAbleTmnu() {
		return pwdMstkAbleTmnu;
	}

	public void setPwdMstkAbleTmnu(String pwdMstkAbleTmnu) {
		this.pwdMstkAbleTmnu = pwdMstkAbleTmnu;
	}

	public String getConnPrmsClsCode() {
		return connPrmsClsCode;
	}

	public void setConnPrmsClsCode(String connPrmsClsCode) {
		this.connPrmsClsCode = connPrmsClsCode;
	}

	public String getEmplCustConnClsCode() {
		return emplCustConnClsCode;
	}

	public void setEmplCustConnClsCode(String emplCustConnClsCode) {
		this.emplCustConnClsCode = emplCustConnClsCode;
	}

	public String getConnWarnClsCode() {
		return connWarnClsCode;
	}

	public void setConnWarnClsCode(String connWarnClsCode) {
		this.connWarnClsCode = connWarnClsCode;
	}

	public String getSstmDate() {
		return sstmDate;
	}

	public void setSstmDate(String sstmDate) {
		this.sstmDate = sstmDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwdErrTmnu() {
		return pwdErrTmnu;
	}

	public void setPwdErrTmnu(String pwdErrTmnu) {
		this.pwdErrTmnu = pwdErrTmnu;
	}

	public String getPbcrPwdMstkTmnu() {
		return pbcrPwdMstkTmnu;
	}

	public void setPbcrPwdMstkTmnu(String pbcrPwdMstkTmnu) {
		this.pbcrPwdMstkTmnu = pbcrPwdMstkTmnu;
	}

	public String getBsopDate() {
		return bsopDate;
	}

	public void setBsopDate(String bsopDate) {
		this.bsopDate = bsopDate;
	}

	public String getRcno() {
		return rcno;
	}

	public void setRcno(String rcno) {
		this.rcno = rcno;
	}

	public String getPbcrPwdMstkAble() {
		return pbcrPwdMstkAble;
	}

	public void setPbcrPwdMstkAble(String pbcrPwdMstkAble) {
		this.pbcrPwdMstkAble = pbcrPwdMstkAble;
	}

	public String getConnPwdModJobYn() {
		return connPwdModJobYn;
	}

	public void setConnPwdModJobYn(String connPwdModJobYn) {
		this.connPwdModJobYn = connPwdModJobYn;
	}

	public String getOwnSecuMdiaClsCode() {
		return ownSecuMdiaClsCode;
	}

	public void setOwnSecuMdiaClsCode(String ownSecuMdiaClsCode) {
		this.ownSecuMdiaClsCode = ownSecuMdiaClsCode;
	}

	public String getCsno() {
		return csno;
	}

	public void setCsno(String csno) {
		this.csno = csno;
	}

	public String getMenuAthrClsCode() {
		return menuAthrClsCode;
	}

	public void setMenuAthrClsCode(String menuAthrClsCode) {
		this.menuAthrClsCode = menuAthrClsCode;
	}

	public String getUseStaTime() {
		return useStaTime;
	}

	public void setUseStaTime(String useStaTime) {
		this.useStaTime = useStaTime;
	}

	public String getD1BefBsopDate() {
		return d1BefBsopDate;
	}

	public void setD1BefBsopDate(String d1BefBsopDate) {
		this.d1BefBsopDate = d1BefBsopDate;
	}

	public String getLognSparDomnCntt() {
		return lognSparDomnCntt;
	}

	public void setLognSparDomnCntt(String lognSparDomnCntt) {
		this.lognSparDomnCntt = lognSparDomnCntt;
	}

	public String getSecuMdiaPwdMstkTmnu() {
		return secuMdiaPwdMstkTmnu;
	}

	public void setSecuMdiaPwdMstkTmnu(String secuMdiaPwdMstkTmnu) {
		this.secuMdiaPwdMstkTmnu = secuMdiaPwdMstkTmnu;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getLoginClsCode() {
		return loginClsCode;
	}

	public void setLoginClsCode(String loginClsCode) {
		this.loginClsCode = loginClsCode;
	}

	public String getDnData() {
		return dnData;
	}

	public void setDnData(String dnData) {
		this.dnData = dnData;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getAuthAthnKeyLngt() {
		return authAthnKeyLngt;
	}

	public void setAuthAthnKeyLngt(String authAthnKeyLngt) {
		this.authAthnKeyLngt = authAthnKeyLngt;
	}

	public String getAuthAthnKeyVal() {
		return authAthnKeyVal;
	}

	public void setAuthAthnKeyVal(String authAthnKeyVal) {
		this.authAthnKeyVal = authAthnKeyVal;
	}

	@Override
	public String toString() {
		return "LognMangBean [userId=" + userId + ", userPwd=" + userPwd
				+ ", pbcrPwd=" + pbcrPwd + ", selTimeOut=" + selTimeOut
				+ ", elctSgntUseYn=" + elctSgntUseYn + ", connPwdInitYn="
				+ connPwdInitYn + ", authAthnIstuDsblYn=" + authAthnIstuDsblYn
				+ ", pwdMstkAbleTmnu=" + pwdMstkAbleTmnu + ", connPrmsClsCode="
				+ connPrmsClsCode + ", emplCustConnClsCode="
				+ emplCustConnClsCode + ", connWarnClsCode=" + connWarnClsCode
				+ ", sstmDate=" + sstmDate + ", userName=" + userName
				+ ", pwdErrTmnu=" + pwdErrTmnu + ", pbcrPwdMstkTmnu="
				+ pbcrPwdMstkTmnu + ", bsopDate=" + bsopDate + ", rcno=" + rcno+ ", rcnoPip=" + rcnoPip
				+ ", pbcrPwdMstkAble=" + pbcrPwdMstkAble + ", connPwdModJobYn="
				+ connPwdModJobYn + ", ownSecuMdiaClsCode="
				+ ownSecuMdiaClsCode + ", csno=" + csno + ", menuAthrClsCode="
				+ menuAthrClsCode + ", useStaTime=" + useStaTime
				+ ", d1BefBsopDate=" + d1BefBsopDate + ", lognSparDomnCntt="
				+ lognSparDomnCntt + ", secuMdiaPwdMstkTmnu="
				+ secuMdiaPwdMstkTmnu + ", emailAddr=" + emailAddr
				+ ", loginClsCode=" + loginClsCode + ", dnData=" + dnData
				+ ", sessionKey=" + sessionKey + ", authAthnKeyLngt="
				+ authAthnKeyLngt + ", authAthnKeyVal=" + authAthnKeyVal
				+ ", userIp=" + userIp+ ", userPrivIp=" + userPrivIp + ", macAddrF=" + macAddrF 
				+ ", macAddrR=" + macAddrR + ", macAddrType=" + macAddrType 
				+ ", discSN=" + discSN + "]";
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getMacAddrF() {
		return macAddrF;
	}

	public void setMacAddrF(String macAddrF) {
		this.macAddrF = macAddrF;
	}

	public String getMacAddrR() {
		return macAddrR;
	}

	public void setMacAddrR(String macAddrR) {
		this.macAddrR = macAddrR;
	}

	public String getMacAddrType() {
		return macAddrType;
	}

	public void setMacAddrType(String macAddrType) {
		this.macAddrType = macAddrType;
	}

	public String getDiscSN() {
		return discSN;
	}

	public void setDiscSN(String discSN) {
		this.discSN = discSN;
	}

	public String getUserPrivIp() {
		return userPrivIp;
	}

	public void setUserPrivIp(String userPrivIp) {
		this.userPrivIp = userPrivIp;
	}

	public String getRcnoPip() {
		return rcnoPip;
	}

	public void setRcnoPip(String rcnoPip) {
		this.rcnoPip = rcnoPip;
	}
	
	public String getNtatSrno() {
		return ntatSrno;
	}

	public void setNtatSrno(String ntatSrno) {
		this.ntatSrno = ntatSrno;
	}



}
