package com.imeritz.blender.tran;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

//import javax.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BldTRCall {
	
	protected static Logger logger = LoggerFactory.getLogger(BldTRCall.class);
	
	//WSCli cli;
	int len, msglen;
	private int rc;
	private static final int IO_SIZE = 8;					/* IS SIZE 8byte */
	private static final int HEADER_SIZE = 385;				/* 가변을 제외한 헤더 사이즈 */
	private static final int HEADER_CHAIN_SIZE = 94;		/* chain 다음 문자인경우 94byte를 제외해야 본 data 임 */
	private static final int HEADER_MSG_CODE_POS = 251;		/* MSG CODE 시작 위치 4byte*/
	private static final int HEADER_MSG_MSG_POS = 255;		/* MSG 시작위치 80byte */
	private static final int HEADER_MSG_SUB_MSG_POS = 335;	/* 서브 MSG 시작위치 40byte */	
	private static final int HEADER_MSG_ERROR_POS = 248;	/* MSG ERROR 시작 위치 1byte */
	/**
	 * callTR
	 * 설명 : 소켓통신을 요청후 결과를 받아온다.
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable callTR(String trno, String[] outBlocks, byte[] HeaderIn, HashMap info
			){
		int packetLen = 0;									//맨앞 8자리 전체 길이를 제외한 수신 전체 길이
		byte[] Header = null;
		byte[] HeaderBuff = new byte[102400];
		byte[] Output  = new byte[102400];
		byte[] OutputNext  = new byte[1024];
		byte[] InputNext  = new byte[1024];
		
		int inputNextSize  = 0;
		
		Hashtable h = new Hashtable();
		
		try {
			Header = getHeader(trno, info).toString().getBytes();    // 로그인 IP를 세션에서 가져 오기 위해 req  추가 2018.11.22
			if(info.get("NextYn") != null){				
				if(info.get("NextYn").equals("Y")){				
					InputNext = info.get("NextBlockString").toString().getBytes();
					inputNextSize = Integer.parseInt(info.get("NextLength").toString());
				}
			}	
			String HeaderLen = BTUtil.lpad(Integer.toString(Header.length+HeaderIn.length+inputNextSize),IO_SIZE);		
			/* HeaderBuff = "길이" */
			HeaderBuff = BTUtil.ArrayCopyRtn(HeaderBuff, 0, HeaderLen.getBytes(), 0, IO_SIZE);
			/* HeaderBuff = "길이+헤더" */
			HeaderBuff = BTUtil.ArrayCopyRtn(HeaderBuff, IO_SIZE, Header, 0, Header.length);
			if(inputNextSize > 0){				
				/* HeaderBuff = "길이+헤더+연속조회키" */
				HeaderBuff = BTUtil.ArrayCopyRtn(HeaderBuff, IO_SIZE+Header.length, InputNext, 0, inputNextSize);
			}				
			/* HeaderBuff = "길이+헤더+연속조회키+사용자 INPUT " */
			HeaderBuff = BTUtil.ArrayCopyRtn(HeaderBuff, IO_SIZE+Header.length+inputNextSize, HeaderIn, 0, HeaderIn.length);
//			System.out.println("[IN         ]["+trno+"]"+"["+HeaderIn.length+"]["+BTUtil.getBytePData(HeaderIn, 0, HeaderIn.length)+"]");	
//			for(int i=0;i<HeaderIn.length;i++){
//				System.out.print(" >> [" + (i+1) + "] [" + HeaderIn[i] + "]"); 
//			}			
//			System.out.println("\n[IN WSTRCall]["+trno+"]"+"["+BTUtil.getBytePData(HeaderBuff, 0, (Integer.parseInt(HeaderLen)+IO_SIZE))+"]");
			
			logger.info("**********************************************************************************************************");
			logger.info("\n[MCI INPUT]["+trno+"]"+"["+BTUtil.getBytePData(HeaderBuff, 0, (Integer.parseInt(HeaderLen)+IO_SIZE))+"]");
			logger.info("**********************************************************************************************************");
			
			BldConnector trc = connect();
			
			byte[] rcvPacket = null;
			if(trc != null){
//				BldConnMgr cm = new BldConnMgr(); 
				trc.setMsg(BTUtil.getByte(HeaderBuff, 0, Integer.parseInt(HeaderLen)+IO_SIZE));
				trc.sendMsg();
				trc.receiveMsg();
				rcvPacket = trc.getReceiveMsg();
				
				// For TEST - must remove this before deploy
//				rcvPacket = "00001486S9001S00000215        6C2B59CBCC23366CB43FA74A6990EBB95FDF0695FBooopc_cuma_023r     ME11    FFLS082034605         1                             010172.017.121.109                        172.017.121.109                                       00A5820                                                                                                                                  050000000000000120010A00002024031907571327078611202561BBMQ 1000487485용홍시                                                                                              1055-973-****                            02-6098-****   010-4244-****       21986 어호 자오문 송하마용기06박눈 87 790사 208형 (송완완,송사영코동목눈황수파박보해)                                                                                                                         07326 감말 창탕산호 무인노밤광 77 JBH (이심보심)                                                                                                                                                                                                                                                                                                                                                                            vnzql207@gmail.com                                                                                                                                                                                      01자택주소                                YYYY".getBytes("MS949");
//				rcvPacket = "00001486S9001S00000215        6C2B59CBCC23366CB43FA74A6990EBB95FDF0695FBooopc_cuma_023r     ME11    FFLS082034605         1                             010172.017.121.109                        172.017.121.109                                       00A5820                                                                                                                                  050000000000000120010A00002024031907571327078611202561BBMQ 1000487485용홍시                                                                                              1055-973-****                            02-6098-****   010-4244-****       21986 어호 자오문 송하마용기06박눈 87 790사 208형 (송완완,송사영코동목눈황수파박보해)                                                                                                                         07326 감말 창탕산호 무인노밤광 77 JBH (이심보심)                                                                                                                                                                                                                                                                                                                                                                            vnzql207@gmail.com                                                                                                                                                                                      01자택주소                                YYYY".getBytes("EUC-KR");
//				rcvPacket = cm.useConnector(trc, BTUtil.getByte(HeaderBuff, 0, Integer.parseInt(HeaderLen)+IO_SIZE));
//				if(trc.isChain()){
//					// chain 여부 log용
//					System.out.println("[OUT Chain  ][true]");
//				}
			}

			logger.info("**********************************************************************************************************");
			logger.info("Send To WEB["+trno+"]"+"["+BTUtil.getBytePData(rcvPacket, 0, rcvPacket.length)+"]");
			logger.info("**********************************************************************************************************");
						
			if(rcvPacket != null){
				/**
				 * chnl_user_len 채널 데이터 길이
				 * authorized_len 서명데이터 길이
				 * next_key_len Next Key 길이
				 **/
				int chnl_user_len = Integer.parseInt(BTUtil.getBytePData(rcvPacket, 385, 3));
				int authorized_len = Integer.parseInt(BTUtil.getBytePData(rcvPacket, 388, 3));
				int next_key_len = Integer.parseInt(BTUtil.getBytePData(rcvPacket, 391, 3));
				
				int cutPos = 0, cutPoe = 0;
				int inlen = 0;

				if(info.get("FID").equals("Y")){
					packetLen = Integer.parseInt(BTUtil.getBytePData(rcvPacket ,cutPoe ,IO_SIZE));
					cutPoe += packetLen;
					int packetHeader = cutPos+HEADER_SIZE+(chnl_user_len+authorized_len+next_key_len)+9;	// 9 = chnl_user_len+chnl_user_len+next_key_len 3+3+3, 15 = 배열 데이터 코드(‘$’)+데이터길이	요구 레코드 개수/응답개수(RowCount)+	액션 구분(ActionKey)+상태구분(Status)
					inlen = BTUtil.ArrayCopy(Output, cutPos, rcvPacket, packetHeader, (cutPoe+IO_SIZE));						
				}else{
					if(next_key_len > 0){
						int nextPos = cutPos+HEADER_SIZE+(chnl_user_len+authorized_len)+9;
						inlen = BTUtil.ArrayCopy(OutputNext, 0, rcvPacket, nextPos, nextPos+next_key_len);
						h.put("nextyn","Y"); 
					}else{
						h.put("nextyn","N"); 
					}
					int startPos = cutPos+HEADER_SIZE+(chnl_user_len+authorized_len+next_key_len)+9;
					packetLen = Integer.parseInt(BTUtil.getBytePData(rcvPacket ,cutPoe ,IO_SIZE));
					cutPoe += packetLen+IO_SIZE;
					inlen = BTUtil.ArrayCopy(Output, 0, rcvPacket, startPos, cutPoe);
				}
				//헤더를 제외한 실데이터를 넣는다.
				h.put("OUT_DATA", Output);
				h.put("OUT_DATA_NEXT", OutputNext);
				for(int i=0;i<outBlocks.length;i++){	
					try{
						if (cutPoe != 0){
							h.put(outBlocks[i], Output);
						}else{
							h.put(outBlocks[i], "");
						}
					} 
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				h.put("error", BTUtil.getBytePData(rcvPacket, HEADER_MSG_ERROR_POS, 1));		//9 error
				h.put("msgcode", BTUtil.getBytePData(rcvPacket, HEADER_MSG_CODE_POS, 4));		//251 msgCode
				h.put("msg", BTUtil.getBytePData(rcvPacket, HEADER_MSG_MSG_POS, 80));			//255 msg 위치
				h.put("submsg", BTUtil.getBytePData(rcvPacket, HEADER_MSG_SUB_MSG_POS, 40));	//335 sub msg 위치
				
//				System.out.println("[OUT MSG    ]["+trno+"]"+"["+String.valueOf(h.get("msg")).trim()+"] \n");
			}else{
				h.put("nextyn","N");
				h.put("error", "9");
				h.put("msg", "error");
				h.put("submsg", "알수없는 오류");
				h.put("msgcode", "9999");
			}

		} catch (UnknownHostException e) {
//			System.out.println("WSTRCall.callTR UnknownHostException error : "+e.getMessage());
			h.put("msg", "error");
			e.printStackTrace();
		} catch (Exception e) {
//			System.out.println("WSTRCall.callTR Exception error : "+e.getMessage());
			h.put("msg", "error");
			e.printStackTrace();
		}		
		return h;
	}
	
	/**
	 * getHeader
	 * 설명 : MCI 헤더 조립
	 **/
	@SuppressWarnings("rawtypes")
	public StringBuffer getHeader(String trno, HashMap info
			){

		StringBuffer header = new StringBuffer();
						/* [  1] C header_type [헤더형태]
						 **********************************************
						 	R   : TRAN 요청
							H   : 암호화 Handshaking Init
							U   : 암호화 Handshaking Update
							F   : 암호화 Handshaking Final
							0(N): Subscribe Set
							1(N): Subscribe Reset
							O(A): Polling
							I(A): MCI Init
							G   : 클라이언트 등록
						 */	
		if(info.get("HeaderType") != null){
			if(info.get("HeaderType").toString().length()==1){
				header.append(info.get("HeaderType").toString());
			}else{
				header.append("R");
			}			
		}else{			
			header.append("R");						
		}
						/* [  1] C svc_dest_vrfy [목적지 구분]
						 **********************************************
							1 : 시세FID
							9 : 원장
							C : 시세TRAN
							3 : Local MCI
							M : MCI MASTER
							Y : 자동갱신 SET/RESET
						 */
		if(info.get("FID").equals("Y")){
			header.append("1");
		}else{
			if(info.get("SvcDestVrfy") != null){
				header.append(info.get("SvcDestVrfy").toString());
			}else{
				header.append("9");
			}									
		}
						/* [  1] N is_crypt [암호화여부]
						 **********************************************
						 * 0: 평문
						 * 1: 암호화
						 */
		if(info.get("IsCrypt") != null){
			header.append(info.get("IsCrypt").toString());
		}else{			
			header.append("0");						
		}
						/* [  1] C is_compressed [압축 여부]
						 **********************************************
						 * ※ 챠트가 들어가면 압축 필요 함 MCI 쪽 협의 필요
						 * 0: 평문
						 * 1: 압축
						 * 2: 평문, 응답시도 평문
						 */
		if(info.get("IsCompressed") != null){
			header.append(info.get("IsCompressed").toString());
		}else{			
			header.append("2");						
		}
						/* [  1] C reply_needed [응답형태]
						 **********************************************
						 * 0: 응답미수신(Async)
						 * 1: 응답수신(Sync)
						 */
		if(info.get("ReplyNeeded") != null){
			header.append(info.get("ReplyNeeded").toString());
		}else{			
			header.append("1");						
		}
						/* [  1] C pkt_chain_value [체인]
						 **********************************************
						 * 'S' : Chain 사용 안함
						 * 'F' : 첫 체인 데이터
						 * 'C' : 중간 체인 데이터
						 * 'E' : 마지막 체인 데이터
						 */
		if(info.get("PktChainValue") != null){
			header.append(info.get("PktChainValue").toString());
		}else{			
			header.append("S");												
		}
						/* [  8] C tr_org_size [TR상세정보 헤더 패킷 시작부터 끝까지 크기]
						 **********************************************
						 * Chainning 이나 압축/암호화 이전의 원본 크기 [채널,MCI] 채널과 MCI 구간 사이에 전문의 chaining/압축/암호화시 전문 길이 연산에 사용
						 */
		if(info.get("TrOrgSize") != null){
			header.append(info.get("TrOrgSize").toString());
		}else{			
			header.append("        ");				
		}
						/* [  8] C tr_real_size [TR상세정보 헤더 패킷 시작부터 끝까지 크기]
						 **********************************************
						 * 압축되거나 암호화 된 TR의 Length [채널,MCI] 채널과 MCI 구간 사이에 전문의 chaining/압축/암호화시 전문 길이 연산에 사용
						 */
		if(info.get("TrRealSize") != null){
			header.append(info.get("TrRealSize").toString());
		}else{			
			header.append("        ");				
		}
						/* [ 10] C fix_common_prepared [예비영역]  Exture Plus Mac Addr 10자리 사용 2013.10.30*/
		if (info.get("FixCommonPrepared")!= null) {
			if (info.get("FixCommonPrepared").toString().length()==10) // 10자리 일 경우만 입력
				header.append(info.get("FixCommonPrepared").toString());
			else 
				header.append("0000000000");
				//header.append("          ");
		} else //header.append("          ");
			header.append("0000000000");
//		if (ExturePlusValues.MAC_ADDR_FRONT != null) header.append(ExturePlusValues.MAC_ADDR_FRONT);
//		else header.append("          ");
//				header.append("          ");
				
						/* [ 32] C UUId [Unique한 ID] */
		header.append(UUID.randomUUID().toString().replace("-", ""));		
						/* [ 15] C tr_code [호출 서비스명] */		
		header.append(BTUtil.rpad(trno, 15));					
						/* [  5] C main_scr_no [화면번호] **/
		header.append("     ");		
						/* [ 10] C tr_base_prepared [예비영역]  Exture Plus Mac Addr 2자리 사용 2013.10.30  뒤에 두자리만 사용해야 함.*/ 
		if (info.get("TrBasePrepared")!= null) {
			if (info.get("TrBasePrepared").toString().length()==10) // 10자리 일 경우만 입력
				header.append(info.get("TrBasePrepared").toString());
			else 
				header.append("        00");
		} else header.append("        00");
//		if (ExturePlusValues.MAC_ADDR_RARE != null) header.append(ExturePlusValues.MAC_ADDR_RARE);
//		else header.append("          ");				
//		header.append("          ");		
		
						/* [  1] C is_cont_tx [연속거래구분] 
						 **********************************************
						 * L : 마지막데이터
						 * B : 이전 데이터 조회
						 * N : 다음 데이터 조회
						 */
		if(info.get("NextYn") != null){
			if(info.get("NextYn").equals("Y")){			
				header.append("N");
			}
		}else{			
			header.append("F");
		}
						/* [  1] C pkt_proc_gb [이벤트구분] 
						 **********************************************
						 * S:조회
						 * I:등록
						 * U:수정
						 * D:삭제
						 * A:처리
						 */
		if(info.get("PktProcGb") != null){
			header.append(info.get("PktProcGb").toString());
		}else{			
			header.append("S");						
		}
						/* [  9] C mca_req_time [단말기(MCI) 요청일시] 
						 **********************************************
						 * HHMISSmmm(미리 초 까지)
						 */
		header.append("         ");				
						/* [  9] C req_ystem_send_time [요청시스템 송신일시] 
						 **********************************************
						 *  HHMISSmmm(미리 초 까지)
						 */
		header.append("         ");				
						/* [ 10] C tr_prepared [예비영역]   Exture Plus Mac Addr Type 0:공인인증서 추출, 1:Flash추출 1자리 사용 2013.10.30  */
		if (info.get("TrPrepared")!= null) {
			if (info.get("TrPrepared").toString().length()==10) // 10자리 일 경우만 입력
				header.append(info.get("TrPrepared").toString());
			else 
//				header.append("          ");
				header.append("          ");
		} else  
			header.append("          ");
//			header.append("          ");
//		if (ExturePlusValues.MAC_ADDR_TYPE != null) header.append(ExturePlusValues.MAC_ADDR_TYPE);
//		else header.append("          ");		
//		header.append("          ");			
		
						/* [ 20] C user_id [사용자 ID] */
		if(info.get("UserId") != null){
			header.append(BTUtil.rpad(info.get("UserId").toString(), 20));
		}else{			
			header.append("                    ");					
		}
						/* [  3] C chnl_media_vrfy [채널매체구분] 
						010 : 창구(내방)
						011 : 유선(업무사원)
						020 : HTS
						060 : WTS/인터넷
						041 : 스마트폰
						150 : 아이폰
						 * */
		if(info.get("ChnlMediaVrfy") != null){
			header.append(info.get("ChnlMediaVrfy").toString());
		}else{			
			header.append("060");						
		}
		 
		
		//			/* [ 39] C client_pcip_addr [사설 IP] */
		if(info.get("BTUtilPrivIp") != null && info.get("BTUtilPrivIp")!="") {
			header.append(BTUtil.rpad(info.get("BTUtilPrivIp").toString(), 39));
			System.out.println("BldTRCall PrivIp1i:" + info.get("BTUtilPrivIp").toString());
		} else {
			header.append(BTUtil.rpad(CommonProperties.getLognUserPrivIp(), 39));
			System.out.println("BldTRCall PrivIp1c:" + CommonProperties.getLognUserPrivIp());
		}
		
		//			/* [ 39] C client_ip_addr [공인 IP] */
		if(info.get("BTUtilPubIp") != null && info.get("BTUtilPubIp") != ""){
			header.append(BTUtil.rpad(info.get("BTUtilPubIp").toString(), 39));													
		} else {
			header.append(BTUtil.rpad(CommonProperties.getLognUserIp(), 39));
		}
		
		
//		if(info.get("BTUtilPrivIp") != null){
//			/* [ 39] C client_pcip_addr [사설 IP] */
//			header.append(BTUtil.rpad(info.get("BTUtilPrivIp").toString(), 39));													
////		header.append(BTUtil.rpad(CommonProperties.getLognUserPrivIp(), 39));													
////		header.append(BTUtil.rpad(CommonProperties.getLognUserIp(), 39));													
////		header.append(BTUtil.rpad("010.200.201.149", 39));
//		} else {
//			header.append(BTUtil.rpad("000.000.000.000", 39));
//			
//			/* [ 39] C client_ip_addr [공인 IP] */
//			if(info.get("BTUtilPubIp") != null){
//				header.append(BTUtil.rpad(info.get("BTUtilPubIp").toString(), 39));													
////		header.append(BTUtil.rpad(CommonProperties.getLognUserIp(), 39));													
////		header.append(BTUtil.rpad("010.200.201.149", 39));													
////		header.append(BTUtil.rpad(CommonProperties.getLognUserIp(), 39));
//			} else {
//				header.append(BTUtil.rpad("000.000.000.000", 39));
//			}
//			
//		}
		
						/* [  5] C br_code [지점코드] */
		if(info.get("BrCode") != null){
			header.append(BTUtil.rpad(info.get("BrCode").toString(),5));
		}else{			
			header.append("     ");					
		}
						/* [ 10] C user_prepared [예비영역] */
		header.append("          ");			
						/* [  3] C chnl_user_len [채널 데이터 길이] */
		header.append("050");					
						/* [  3] C authorized_len [서명데이터 길이] */
		header.append("000");					
						/* [  3] C next_key_len [Next Key 길이] */
		if(info.get("NextYn") != null){
			header.append(BTUtil.lpad(info.get("NextLength").toString(), 3));
		}else{			
			header.append("000");					
		}							
						/* [  8] C req_window_handle [Wnd Handle]
						 **********************************************
						 * 클라이언트 내부에서 사용될 화면 Handle(Windoww Handle)
						 * [채널]F/W 서비스 응답시 해당 화면으로 응답처리에 사용
						 */
		header.append("00000048");				
						/* [  1] C usr_media_vrfy [입력매체구분] 
						 **********************************************
						 * 0->무사용
						 * 1->개별카드
						 * 2->종합통장
						 * 3->종합카드
						 * 4->지점책임자
						 * 5->감사실책임자
						 */		
		header.append("0");						
						/* [  8] C session_key [Session Key] 
						 **********************************************
						 * 로그인 시 생성된 Packet의 Session Key
						 * [MCI]로그인 Tr에서 생성되어지며 Async 트랜잭션 처리시 응답을 받을 session key로 사용
						 */
		header.append("        ");				
						/* [ 32] C mci_session_key [MCI 세션키]
						 **********************************************
						 * 사용자 로그인시 생성되는 키 [MCI] MCI 에서 키 생성
						 * 로그인시 받은 내용 올림
						 */
		header.append(BTUtil.rpad(CommonProperties.getLognSessionKey(), 32)); 		
						/* [ 32] C role_cls [처리자 업무역할 구분자]
						 **********************************************
						 * 로그인시 받은 내용 올림
						 * 1;업무직원
						 * 2;영업직원
						 * 3;업무팀장
						 * 4;지점장/부서장
						 * 5;본부장
						 * A;고객지원센터 팀원
						 * B;고객지원센터 관리자
						 * C;고객지원센터장
						 */		
		header.append(" ");	
		
		return header;
	}

	/**
	 * connect
	 * 설명 : 소켓 통신 접속 요청
	 **/
	public BldConnector connect() throws UnknownHostException{
		BldConnector trc = null;
		String ip = CommonProperties.getServerIp();
		String port= CommonProperties.getServerPort();

		trc = new BldConnector();
		trc.setIp(ip);
		trc.setPort(Integer.parseInt(port));
		rc = trc.Connect();
		System.out.println("Start connect ["+ ip +":"+ port +"] [rc : "+rc+"]");

		if(rc == 1){
			return trc;
		}else{
			trc.stopConnector("con");
			return null;
		}
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public int getRc() {
		return rc;
	}
}
