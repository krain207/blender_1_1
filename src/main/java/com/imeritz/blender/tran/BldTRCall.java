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
	private static final int HEADER_SIZE = 385;				/* ������ ������ ��� ������ */
	private static final int HEADER_CHAIN_SIZE = 94;		/* chain ���� �����ΰ�� 94byte�� �����ؾ� �� data �� */
	private static final int HEADER_MSG_CODE_POS = 251;		/* MSG CODE ���� ��ġ 4byte*/
	private static final int HEADER_MSG_MSG_POS = 255;		/* MSG ������ġ 80byte */
	private static final int HEADER_MSG_SUB_MSG_POS = 335;	/* ���� MSG ������ġ 40byte */	
	private static final int HEADER_MSG_ERROR_POS = 248;	/* MSG ERROR ���� ��ġ 1byte */
	/**
	 * callTR
	 * ���� : ��������� ��û�� ����� �޾ƿ´�.
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable callTR(String trno, String[] outBlocks, byte[] HeaderIn, HashMap info
			){
		int packetLen = 0;									//�Ǿ� 8�ڸ� ��ü ���̸� ������ ���� ��ü ����
		byte[] Header = null;
		byte[] HeaderBuff = new byte[102400];
		byte[] Output  = new byte[102400];
		byte[] OutputNext  = new byte[1024];
		byte[] InputNext  = new byte[1024];
		
		int inputNextSize  = 0;
		
		Hashtable h = new Hashtable();
		
		try {
			Header = getHeader(trno, info).toString().getBytes();    // �α��� IP�� ���ǿ��� ���� ���� ���� req  �߰� 2018.11.22
			if(info.get("NextYn") != null){				
				if(info.get("NextYn").equals("Y")){				
					InputNext = info.get("NextBlockString").toString().getBytes();
					inputNextSize = Integer.parseInt(info.get("NextLength").toString());
				}
			}	
			String HeaderLen = BTUtil.lpad(Integer.toString(Header.length+HeaderIn.length+inputNextSize),IO_SIZE);		
			/* HeaderBuff = "����" */
			HeaderBuff = BTUtil.ArrayCopyRtn(HeaderBuff, 0, HeaderLen.getBytes(), 0, IO_SIZE);
			/* HeaderBuff = "����+���" */
			HeaderBuff = BTUtil.ArrayCopyRtn(HeaderBuff, IO_SIZE, Header, 0, Header.length);
			if(inputNextSize > 0){				
				/* HeaderBuff = "����+���+������ȸŰ" */
				HeaderBuff = BTUtil.ArrayCopyRtn(HeaderBuff, IO_SIZE+Header.length, InputNext, 0, inputNextSize);
			}				
			/* HeaderBuff = "����+���+������ȸŰ+����� INPUT " */
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
//				rcvPacket = "00001486S9001S00000215        6C2B59CBCC23366CB43FA74A6990EBB95FDF0695FBooopc_cuma_023r     ME11    FFLS082034605         1                             010172.017.121.109                        172.017.121.109                                       00A5820                                                                                                                                  050000000000000120010A00002024031907571327078611202561BBMQ 1000487485��ȫ��                                                                                              1055-973-****                            02-6098-****   010-4244-****       21986 ��ȣ �ڿ��� ���ϸ����06�ڴ� 87 790�� 208�� (�ۿϿ�,�ۻ翵�ڵ���Ȳ���Ĺں���)                                                                                                                         07326 ���� â����ȣ ���γ�㱤 77 JBH (�̽ɺ���)                                                                                                                                                                                                                                                                                                                                                                            vnzql207@gmail.com                                                                                                                                                                                      01�����ּ�                                YYYY".getBytes("MS949");
//				rcvPacket = "00001486S9001S00000215        6C2B59CBCC23366CB43FA74A6990EBB95FDF0695FBooopc_cuma_023r     ME11    FFLS082034605         1                             010172.017.121.109                        172.017.121.109                                       00A5820                                                                                                                                  050000000000000120010A00002024031907571327078611202561BBMQ 1000487485��ȫ��                                                                                              1055-973-****                            02-6098-****   010-4244-****       21986 ��ȣ �ڿ��� ���ϸ����06�ڴ� 87 790�� 208�� (�ۿϿ�,�ۻ翵�ڵ���Ȳ���Ĺں���)                                                                                                                         07326 ���� â����ȣ ���γ�㱤 77 JBH (�̽ɺ���)                                                                                                                                                                                                                                                                                                                                                                            vnzql207@gmail.com                                                                                                                                                                                      01�����ּ�                                YYYY".getBytes("EUC-KR");
//				rcvPacket = cm.useConnector(trc, BTUtil.getByte(HeaderBuff, 0, Integer.parseInt(HeaderLen)+IO_SIZE));
//				if(trc.isChain()){
//					// chain ���� log��
//					System.out.println("[OUT Chain  ][true]");
//				}
			}

			logger.info("**********************************************************************************************************");
			logger.info("Send To WEB["+trno+"]"+"["+BTUtil.getBytePData(rcvPacket, 0, rcvPacket.length)+"]");
			logger.info("**********************************************************************************************************");
						
			if(rcvPacket != null){
				/**
				 * chnl_user_len ä�� ������ ����
				 * authorized_len �������� ����
				 * next_key_len Next Key ����
				 **/
				int chnl_user_len = Integer.parseInt(BTUtil.getBytePData(rcvPacket, 385, 3));
				int authorized_len = Integer.parseInt(BTUtil.getBytePData(rcvPacket, 388, 3));
				int next_key_len = Integer.parseInt(BTUtil.getBytePData(rcvPacket, 391, 3));
				
				int cutPos = 0, cutPoe = 0;
				int inlen = 0;

				if(info.get("FID").equals("Y")){
					packetLen = Integer.parseInt(BTUtil.getBytePData(rcvPacket ,cutPoe ,IO_SIZE));
					cutPoe += packetLen;
					int packetHeader = cutPos+HEADER_SIZE+(chnl_user_len+authorized_len+next_key_len)+9;	// 9 = chnl_user_len+chnl_user_len+next_key_len 3+3+3, 15 = �迭 ������ �ڵ�(��$��)+�����ͱ���	�䱸 ���ڵ� ����/���䰳��(RowCount)+	�׼� ����(ActionKey)+���±���(Status)
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
				//����� ������ �ǵ����͸� �ִ´�.
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
				h.put("msg", BTUtil.getBytePData(rcvPacket, HEADER_MSG_MSG_POS, 80));			//255 msg ��ġ
				h.put("submsg", BTUtil.getBytePData(rcvPacket, HEADER_MSG_SUB_MSG_POS, 40));	//335 sub msg ��ġ
				
//				System.out.println("[OUT MSG    ]["+trno+"]"+"["+String.valueOf(h.get("msg")).trim()+"] \n");
			}else{
				h.put("nextyn","N");
				h.put("error", "9");
				h.put("msg", "error");
				h.put("submsg", "�˼����� ����");
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
	 * ���� : MCI ��� ����
	 **/
	@SuppressWarnings("rawtypes")
	public StringBuffer getHeader(String trno, HashMap info
			){

		StringBuffer header = new StringBuffer();
						/* [  1] C header_type [�������]
						 **********************************************
						 	R   : TRAN ��û
							H   : ��ȣȭ Handshaking Init
							U   : ��ȣȭ Handshaking Update
							F   : ��ȣȭ Handshaking Final
							0(N): Subscribe Set
							1(N): Subscribe Reset
							O(A): Polling
							I(A): MCI Init
							G   : Ŭ���̾�Ʈ ���
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
						/* [  1] C svc_dest_vrfy [������ ����]
						 **********************************************
							1 : �ü�FID
							9 : ����
							C : �ü�TRAN
							3 : Local MCI
							M : MCI MASTER
							Y : �ڵ����� SET/RESET
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
						/* [  1] N is_crypt [��ȣȭ����]
						 **********************************************
						 * 0: ��
						 * 1: ��ȣȭ
						 */
		if(info.get("IsCrypt") != null){
			header.append(info.get("IsCrypt").toString());
		}else{			
			header.append("0");						
		}
						/* [  1] C is_compressed [���� ����]
						 **********************************************
						 * �� íƮ�� ���� ���� �ʿ� �� MCI �� ���� �ʿ�
						 * 0: ��
						 * 1: ����
						 * 2: ��, ����õ� ��
						 */
		if(info.get("IsCompressed") != null){
			header.append(info.get("IsCompressed").toString());
		}else{			
			header.append("2");						
		}
						/* [  1] C reply_needed [��������]
						 **********************************************
						 * 0: ����̼���(Async)
						 * 1: �������(Sync)
						 */
		if(info.get("ReplyNeeded") != null){
			header.append(info.get("ReplyNeeded").toString());
		}else{			
			header.append("1");						
		}
						/* [  1] C pkt_chain_value [ü��]
						 **********************************************
						 * 'S' : Chain ��� ����
						 * 'F' : ù ü�� ������
						 * 'C' : �߰� ü�� ������
						 * 'E' : ������ ü�� ������
						 */
		if(info.get("PktChainValue") != null){
			header.append(info.get("PktChainValue").toString());
		}else{			
			header.append("S");												
		}
						/* [  8] C tr_org_size [TR������ ��� ��Ŷ ���ۺ��� ������ ũ��]
						 **********************************************
						 * Chainning �̳� ����/��ȣȭ ������ ���� ũ�� [ä��,MCI] ä�ΰ� MCI ���� ���̿� ������ chaining/����/��ȣȭ�� ���� ���� ���꿡 ���
						 */
		if(info.get("TrOrgSize") != null){
			header.append(info.get("TrOrgSize").toString());
		}else{			
			header.append("        ");				
		}
						/* [  8] C tr_real_size [TR������ ��� ��Ŷ ���ۺ��� ������ ũ��]
						 **********************************************
						 * ����ǰų� ��ȣȭ �� TR�� Length [ä��,MCI] ä�ΰ� MCI ���� ���̿� ������ chaining/����/��ȣȭ�� ���� ���� ���꿡 ���
						 */
		if(info.get("TrRealSize") != null){
			header.append(info.get("TrRealSize").toString());
		}else{			
			header.append("        ");				
		}
						/* [ 10] C fix_common_prepared [���񿵿�]  Exture Plus Mac Addr 10�ڸ� ��� 2013.10.30*/
		if (info.get("FixCommonPrepared")!= null) {
			if (info.get("FixCommonPrepared").toString().length()==10) // 10�ڸ� �� ��츸 �Է�
				header.append(info.get("FixCommonPrepared").toString());
			else 
				header.append("0000000000");
				//header.append("          ");
		} else //header.append("          ");
			header.append("0000000000");
//		if (ExturePlusValues.MAC_ADDR_FRONT != null) header.append(ExturePlusValues.MAC_ADDR_FRONT);
//		else header.append("          ");
//				header.append("          ");
				
						/* [ 32] C UUId [Unique�� ID] */
		header.append(UUID.randomUUID().toString().replace("-", ""));		
						/* [ 15] C tr_code [ȣ�� ���񽺸�] */		
		header.append(BTUtil.rpad(trno, 15));					
						/* [  5] C main_scr_no [ȭ���ȣ] **/
		header.append("     ");		
						/* [ 10] C tr_base_prepared [���񿵿�]  Exture Plus Mac Addr 2�ڸ� ��� 2013.10.30  �ڿ� ���ڸ��� ����ؾ� ��.*/ 
		if (info.get("TrBasePrepared")!= null) {
			if (info.get("TrBasePrepared").toString().length()==10) // 10�ڸ� �� ��츸 �Է�
				header.append(info.get("TrBasePrepared").toString());
			else 
				header.append("        00");
		} else header.append("        00");
//		if (ExturePlusValues.MAC_ADDR_RARE != null) header.append(ExturePlusValues.MAC_ADDR_RARE);
//		else header.append("          ");				
//		header.append("          ");		
		
						/* [  1] C is_cont_tx [���Ӱŷ�����] 
						 **********************************************
						 * L : ������������
						 * B : ���� ������ ��ȸ
						 * N : ���� ������ ��ȸ
						 */
		if(info.get("NextYn") != null){
			if(info.get("NextYn").equals("Y")){			
				header.append("N");
			}
		}else{			
			header.append("F");
		}
						/* [  1] C pkt_proc_gb [�̺�Ʈ����] 
						 **********************************************
						 * S:��ȸ
						 * I:���
						 * U:����
						 * D:����
						 * A:ó��
						 */
		if(info.get("PktProcGb") != null){
			header.append(info.get("PktProcGb").toString());
		}else{			
			header.append("S");						
		}
						/* [  9] C mca_req_time [�ܸ���(MCI) ��û�Ͻ�] 
						 **********************************************
						 * HHMISSmmm(�̸� �� ����)
						 */
		header.append("         ");				
						/* [  9] C req_ystem_send_time [��û�ý��� �۽��Ͻ�] 
						 **********************************************
						 *  HHMISSmmm(�̸� �� ����)
						 */
		header.append("         ");				
						/* [ 10] C tr_prepared [���񿵿�]   Exture Plus Mac Addr Type 0:���������� ����, 1:Flash���� 1�ڸ� ��� 2013.10.30  */
		if (info.get("TrPrepared")!= null) {
			if (info.get("TrPrepared").toString().length()==10) // 10�ڸ� �� ��츸 �Է�
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
		
						/* [ 20] C user_id [����� ID] */
		if(info.get("UserId") != null){
			header.append(BTUtil.rpad(info.get("UserId").toString(), 20));
		}else{			
			header.append("                    ");					
		}
						/* [  3] C chnl_media_vrfy [ä�θ�ü����] 
						010 : â��(����)
						011 : ����(�������)
						020 : HTS
						060 : WTS/���ͳ�
						041 : ����Ʈ��
						150 : ������
						 * */
		if(info.get("ChnlMediaVrfy") != null){
			header.append(info.get("ChnlMediaVrfy").toString());
		}else{			
			header.append("060");						
		}
		 
		
		//			/* [ 39] C client_pcip_addr [�缳 IP] */
		if(info.get("BTUtilPrivIp") != null && info.get("BTUtilPrivIp")!="") {
			header.append(BTUtil.rpad(info.get("BTUtilPrivIp").toString(), 39));
			System.out.println("BldTRCall PrivIp1i:" + info.get("BTUtilPrivIp").toString());
		} else {
			header.append(BTUtil.rpad(CommonProperties.getLognUserPrivIp(), 39));
			System.out.println("BldTRCall PrivIp1c:" + CommonProperties.getLognUserPrivIp());
		}
		
		//			/* [ 39] C client_ip_addr [���� IP] */
		if(info.get("BTUtilPubIp") != null && info.get("BTUtilPubIp") != ""){
			header.append(BTUtil.rpad(info.get("BTUtilPubIp").toString(), 39));													
		} else {
			header.append(BTUtil.rpad(CommonProperties.getLognUserIp(), 39));
		}
		
		
//		if(info.get("BTUtilPrivIp") != null){
//			/* [ 39] C client_pcip_addr [�缳 IP] */
//			header.append(BTUtil.rpad(info.get("BTUtilPrivIp").toString(), 39));													
////		header.append(BTUtil.rpad(CommonProperties.getLognUserPrivIp(), 39));													
////		header.append(BTUtil.rpad(CommonProperties.getLognUserIp(), 39));													
////		header.append(BTUtil.rpad("010.200.201.149", 39));
//		} else {
//			header.append(BTUtil.rpad("000.000.000.000", 39));
//			
//			/* [ 39] C client_ip_addr [���� IP] */
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
		
						/* [  5] C br_code [�����ڵ�] */
		if(info.get("BrCode") != null){
			header.append(BTUtil.rpad(info.get("BrCode").toString(),5));
		}else{			
			header.append("     ");					
		}
						/* [ 10] C user_prepared [���񿵿�] */
		header.append("          ");			
						/* [  3] C chnl_user_len [ä�� ������ ����] */
		header.append("050");					
						/* [  3] C authorized_len [�������� ����] */
		header.append("000");					
						/* [  3] C next_key_len [Next Key ����] */
		if(info.get("NextYn") != null){
			header.append(BTUtil.lpad(info.get("NextLength").toString(), 3));
		}else{			
			header.append("000");					
		}							
						/* [  8] C req_window_handle [Wnd Handle]
						 **********************************************
						 * Ŭ���̾�Ʈ ���ο��� ���� ȭ�� Handle(Windoww Handle)
						 * [ä��]F/W ���� ����� �ش� ȭ������ ����ó���� ���
						 */
		header.append("00000048");				
						/* [  1] C usr_media_vrfy [�Է¸�ü����] 
						 **********************************************
						 * 0->�����
						 * 1->����ī��
						 * 2->��������
						 * 3->����ī��
						 * 4->����å����
						 * 5->�����å����
						 */		
		header.append("0");						
						/* [  8] C session_key [Session Key] 
						 **********************************************
						 * �α��� �� ������ Packet�� Session Key
						 * [MCI]�α��� Tr���� �����Ǿ����� Async Ʈ����� ó���� ������ ���� session key�� ���
						 */
		header.append("        ");				
						/* [ 32] C mci_session_key [MCI ����Ű]
						 **********************************************
						 * ����� �α��ν� �����Ǵ� Ű [MCI] MCI ���� Ű ����
						 * �α��ν� ���� ���� �ø�
						 */
		header.append(BTUtil.rpad(CommonProperties.getLognSessionKey(), 32)); 		
						/* [ 32] C role_cls [ó���� �������� ������]
						 **********************************************
						 * �α��ν� ���� ���� �ø�
						 * 1;��������
						 * 2;��������
						 * 3;��������
						 * 4;������/�μ���
						 * 5;������
						 * A;���������� ����
						 * B;���������� ������
						 * C;������������
						 */		
		header.append(" ");	
		
		return header;
	}

	/**
	 * connect
	 * ���� : ���� ��� ���� ��û
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
