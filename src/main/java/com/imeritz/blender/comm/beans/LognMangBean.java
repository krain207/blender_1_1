package com.imeritz.blender.comm.beans;

public class LognMangBean {
	private String userId; // �����ID
	private String userPwd; // �����ID ��й�ȣ
	private String pbcrPwd; // ���������� ��й�ȣ
	private String selTimeOut; // ���������� ��й�ȣ

	private String elctSgntUseYn; // ���ڼ����뿩��
	private String connPwdInitYn; // ���Ӻ�й�ȣ�ʱ�ȭ����
	private String authAthnIstuDsblYn; // �������������ֿ���
	private String pwdMstkAbleTmnu; // ��й�ȣ��������Ƚ��
	private String connPrmsClsCode; // ������뱸���ڵ�
	private String emplCustConnClsCode; // ���������ӱ����ڵ�
	private String connWarnClsCode; // ���Ӱ�����ڵ�
	private String sstmDate; // �ý�������
	private String userName; // ����ڸ�
	private String pwdErrTmnu; // ��й�ȣ����Ƚ��
	private String pbcrPwdMstkTmnu; // ������������й�ȣ����Ƚ��
	private String bsopDate; // ��������
	private String rcno; // �Ǹ�Ȯ�ι�ȣ
	private String pbcrPwdMstkAble; // ������������й�ȣ��������
	private String connPwdModJobYn; // ���Ӻ�й�ȣ�����۾�����
	private String ownSecuMdiaClsCode; // �������ȸ�ü�����ڵ�
	private String csno; // ����ȣ
	private String menuAthrClsCode; // �޴����ѱ����ڵ�
	private String useStaTime; // �����۽ð�
	private String d1BefBsopDate; // 1������������
	private String lognSparDomnCntt; // �α��ο��񿵿�����
	private String secuMdiaPwdMstkTmnu; // ���ȸ�ü��й�ȣ����Ƚ��
	private String emailAddr; // �����ּ�
	private String loginClsCode; // �α��� ���� �ڵ� 1=���������� , 2=�Ϲݷα���
	private String dnData; // ������ dn��
	private String sessionKey;	//����Ű
	private String authAthnKeyLngt;	//�������� Ű ����
	private String authAthnKeyVal;	//�������� Ű ��
	//2012.08.09 ������ �߰�
	private String userIp;	//����� ����IP (����)
	private String userPrivIp;	//����� ����IP (�缳)
	//2013.10.31 Exture Plus �߰�
	private String macAddrF;  // Mac �ּ� �� 10�ڸ�
	private String macAddrR;  // Mac �ּ� �� 2�ڸ�
	private String macAddrType;  // Mac �ּ� Ÿ�� 0: ����������, 1:Socket (Flash)		
	private String discSN;  // ��ũ �Ϸù�ȣ		
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
		this.macAddrF = "";  // Mac �ּ� �� 10�ڸ�
		this.macAddrR = "";  // Mac �ּ� �� 2�ڸ�
		this.macAddrType = "";  // Mac �ּ� Ÿ�� 0: ����������, 1:Socket (Flash)		
		this.discSN = "";  // ��ũ �Ϸù�ȣ	
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
