package com.imeritz.blender.tran;

public class BldConnMgr {
	private boolean busy = false;								/* TRConnector 상태 플래그 	*/
//	private int receiveTime = 300;								/* 응답 대기 횟수 				*/
//	private int s_time = 1000;									/* 응답 대기 회수당 ms 			*/	
//	static private BldConnMgr cmgr;
	private int pollingCnt = 30;								/* 폴링 연속 횟수가 30번이상이면 장애처리 */
	
	public  BldConnMgr(){}

	/** 현재 TRConnector의 상태를 free로 설정. **/
	private void freeBusy(){
		busy = false;
	}
	
	public byte [] useConnector(BldConnector trc, byte[] msg){
//		receiveTime = 300; //초기화				
		setIsBusy();		
		trc.setMsg(msg);
		
//		System.out.println("Send to gw");
		trc.sendMsg();

		while (!trc.isMsg()) {
//			sleepTime();
//			if (receiveTime == 0){
//				break;
//			}
		}			
		freeBusy();
//		System.out.println("Receive OK");
		return trc.getReceiveMsg();
	}

	/** 응답대기 사간만큼 Thread sleep **/
//	private void sleepTime(){
//		try {
//			receiveTime--;
//			Thread.sleep(s_time);
//			System.out.print("[" + receiveTime + "]"); 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 현재 TRConnector의 상태를 돌려준다.
	 * True : 사용가능
	 * False : 사용불가
	 * @return  true or false
	 */
	public boolean getIsBusy(){
		return busy;
	}
	
	/** 현재 TRConnector의 상태를 busy로 설정 **/
	private void setIsBusy(){
		busy = true;
	}
}
