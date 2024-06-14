package com.imeritz.blender.tran;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException; 
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;  

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//public class BldConnector extends Thread {
public class BldConnector{
	protected static Logger logger = LoggerFactory.getLogger(BldConnector.class);

	private static final byte AS_END_DATA		=	0x53;	/* 'S' CHAIN FLAG */
	private static final byte AS_FIRST_DATA		=	0x46;	/* 'F' CHAIN FLAG */
	private static final byte AS_MID_DATA		=	0x43;	/* 'C' CHAIN FLAG */
	private static final byte AS_LAST_DATA		=	0x45;	/* 'E' CHAIN FLAG */
	private static final byte AS_POLLING_DATA	=	0x4F;	/* 'O' CHAIN FLAG */
	
//	private Thread thd;											/*	스레드 객체				*/
//	private boolean thd_flag = true;							/*	스레드 생성관리 플래그		*/
	private boolean chain_flag = true;
	private boolean receive_flag = false;
	private Socket sock;										/*	TR 서버에 연결할 Socket 	*/
	private String ip;											
	private int port;							
	private boolean polling_flag = false;
	
	private static final int FULL_PACKET_SIZE	= 8;			/*	첫8자리 데이터 len			*/
	private static final int MAX_PACKET_SIZE	= 102400;			/*	읽어올 데이터 수			*/
	
	/** Socket 데이터를 송수신할 Stream **/
	private DataInputStream din;
	private DataOutputStream dout;
	
//	/** Thread 반복실행 감시자 **/
//	private int cnt = 0;
//	private int cntMax = 5;
	
	private byte[] msg;											/*	송신할 TR MSG				*/
	private byte[] r_msg = new byte[MAX_PACKET_SIZE];					/*	수신할 TR MSG				*/	
	private byte[] r_msg_packet	= new byte[MAX_PACKET_SIZE];				/*	임시 byte					*/	
	private int r_msg_len	= 0;								/*	수신할 TR MSG length		*/	
	boolean m_connect =	false;
	private boolean chain = false;								/*	chain 여부				*/
	private int chainCnt = 0;									/*	chain 갯수				*/
	BTUtil btutil = new BTUtil();
	
	/**
	 * Connect
	 * 설명 : 소켓 통신 접속 요청
	 **/
	public int Connect(){
		try	{ 
			sock = new Socket();
			sock.connect(new InetSocketAddress(getIp(),getPort()), 300000);
		}catch(UnknownHostException e) {
			logger.info("[" + sock.hashCode() + "] Host " + ip	+ "	not	found");
			return -1;
		}catch(IOException e) {
			logger.info("[" + sock.hashCode() + "] Failed to connect to host "+ip);
			return -2;
		//포트바꿔서 한번만 재시도
		}catch (Exception e)	{
			logger.info("[" + sock.hashCode() + "] Security violation on socket for host "+ip);
			return -3;
		//포트바꿔서 한번만 재시도
		}try	{ 
			din = new DataInputStream(sock.getInputStream()); 
			dout =	new	DataOutputStream(sock.getOutputStream());	
		}catch(IOException e){
			logger.info("[" + sock.hashCode() + "] Failed to get stream from socket");
			return -4;
		}
		m_connect =	true;
		return 1;
	}

	/**
	 * setMsg
	 * 설명 : 헤더+INPUT 전솔할 데이터 세팅
	 **/
	public void setMsg(byte[] msg){
		this.msg = msg;
	}
	
	/**
	 * receivePacket
	 * 설명 : MCI단에서 PACKET 받음
	 **/
	public int receivePacket(){
		int recevSize = 0;
		
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buf = new byte[MAX_PACKET_SIZE];
			int n = 0;
			
			if(m_connect){
				for(int i=0;i<MAX_PACKET_SIZE;i++) buf[i] = (byte)0x00;
				
				n = din.read(buf, 0, FULL_PACKET_SIZE);
				String szRecevSize = new String(buf, 0, FULL_PACKET_SIZE);	
				
				logger.info("[" + sock.hashCode() + "][READ N] : [" + n + "]");
				logger.info("[" + sock.hashCode() + "][READ STR] : [" + BTUtil.getBytePData(buf, 0, n) + "]");
				
				bout.write(buf, 0, n);
				recevSize = Integer.parseInt(szRecevSize);
			}
			
			int reRecevSize = recevSize;
			try {
				while(m_connect && reRecevSize > 0){
					n = din.read(buf, 0, reRecevSize);	
					reRecevSize -= n;
					bout.write(buf, 0 , n);
				}
				bout.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			r_msg = bout.toByteArray();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return recevSize;
	}

	/**
	 * receiveMsg
	 * 설명 : MCI단에서 PACKET 받음
	 **/
	@SuppressWarnings("static-access")
	public void receiveMsg(){
		try{
			int recevSize = receivePacket();
			recevSize = recevSize + FULL_PACKET_SIZE;
		
			logger.info("[" + sock.hashCode() + "]     recevSize  :: [" + recevSize + "]");
			logger.info("[" + sock.hashCode() + "]     r_msg.length  :: [" + r_msg.length + "]");

			/** chain
			 * 'S' : Chain 사용 안함
			 * 'F' : 첫 체인 데이터
			 * 'C' : 중간 체인 데이터
			 * 'E' : 마지막 체인 데이터
			 * 'O' : 폴링
			 *  **/			
			if(AS_POLLING_DATA == (byte)r_msg[8]){
				logger.info("[" + sock.hashCode() + "][POLLING ... ] : [" + BTUtil.getBytePData(r_msg, 0, recevSize)+"]");
				polling_flag = true;
				dout.write(r_msg);
				dout.flush();
			}else if(AS_END_DATA == (byte)r_msg[13]){
				r_msg_len += btutil.ArrayCopyLen(r_msg_packet, r_msg_len, r_msg, 0, recevSize);
				logger.info("[" + sock.hashCode() + "][OUT S      ] : [" + BTUtil.getBytePData(r_msg, 0, recevSize)+"]");
				r_msg = r_msg_packet;
				receive_flag = true;
				chain = false;
				chain_flag = false;
			}else{
				if(AS_FIRST_DATA == (byte)r_msg[13]){
					r_msg_len += btutil.ArrayCopyLen(r_msg_packet, r_msg_len, r_msg, 0, recevSize);
					logger.info("[" + sock.hashCode() + "][OUT F		] : [" + BTUtil.getBytePData(r_msg, 0, recevSize) + "]");
					chain_flag = true;
					chainCnt = chainCnt + 1;
				}else if(AS_MID_DATA == (byte)r_msg[13]){
					r_msg_len += btutil.ArrayCopyLen(r_msg_packet, r_msg_len, r_msg, 102, recevSize-102);
					logger.info("[" + sock.hashCode() + "][OUT C		] : [" + BTUtil.getBytePData(r_msg, 0, recevSize) + "]");
					chain_flag = true;
					chainCnt = chainCnt + 1;
				}else if(AS_LAST_DATA == (byte)r_msg[13]){
					r_msg_len += btutil.ArrayCopyLen(r_msg_packet, r_msg_len, r_msg, 102, recevSize-102);
					logger.info("[" + sock.hashCode() + "][OUT E		] : [" + BTUtil.getBytePData(r_msg, 0, recevSize) + "]");
					receive_flag = true;
					chain = true;
					chain_flag = false;
					r_msg = r_msg_packet; 
					chainCnt = chainCnt + 1;
					btutil.ArrayCopyLen(r_msg, 0, btutil.lpad(Integer.toString((r_msg_len - FULL_PACKET_SIZE)),FULL_PACKET_SIZE).getBytes(), 0, FULL_PACKET_SIZE);
				}else{
					//여기까지 들어온것은 이미 아무것도 못 받는다는 의미??
					m_connect = false;
					chain_flag = false;
				}
			}
			logger.info("[" + sock.hashCode() + "][OUT FULL		] : [" + BTUtil.getBytePData(r_msg_packet, 0, r_msg_len) + "]");
			logger.info("[" + sock.hashCode() + "][OUT FULL r_msg_len ] : [" + r_msg_len + "]");
			logger.info("[" + sock.hashCode() + "][OUT FULL chainCnt ] : [" + chainCnt + "]");
			
			if(receive_flag && !polling_flag){
				stopConnector("receiveMsg");
			}
			
		} catch (Exception e) {
			chain_flag = false;
			stopConnector("receiveMsg exec");
			e.printStackTrace();
		}
	}

	/**
	 * getReceiveMsg
	 * 설명 : 메세지가들어왔는지 체크
	 **/
	public byte [] getReceiveMsg(){
		if(receive_flag){
			return r_msg;
		}else{
			return null;
		}
	}
	
	/**
	 * isMsg
	 * 메시지의 수신 상태를 돌려준다.
	 * false == 미수신
	 * true  == 수신
	 */
	public boolean isMsg(){
		return receive_flag;
	}
	
	/**
	 * sendMsg
	 * 메시지 송신
	 */
	public void sendMsg(){
		try {
			if (msg != null) {
				logger.info("[" + sock.hashCode() + "][SEND MSG] : [" + BTUtil.getBytePData(msg, 0, msg.length) + "]");
				
				dout.write(msg);
				dout.flush();
			}
		} catch (Exception e) {
			stopConnector("sendMsg");
			e.printStackTrace();
		}
	}

	/**
	 * stopConnector
	 * 소켓 종료
	 */
	public void stopConnector(String wh){
		try {
			logger.info("[" + sock.hashCode() + "]================ [접속종료] =================");
			chain_flag = false;
			receive_flag = true;
			m_connect = false;
			
			if(sock!=null)sock.close();
			if(dout!=null)dout.close();
			if(din!=null)din.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isReceive_flag() {
		return receive_flag;
	}

	public void setReceive_flag(boolean receive_flag) {
		this.receive_flag = receive_flag;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setChain(boolean chain) {
		this.chain = chain;
	}

	public boolean isChain() {
		return chain;
	}

	public void setChainCnt(int chainCnt) {
		this.chainCnt = chainCnt;
	}

	public int getChainCnt() {
		return chainCnt;
	}
}
