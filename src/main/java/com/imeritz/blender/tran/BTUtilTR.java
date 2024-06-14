package com.imeritz.blender.tran;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imeritz.blender.comm.beans.LognMangBean;
import com.imeritz.blender.comm.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

//import com.imeritz.comm.logn.beans.LognMangBean;
//import com.imeritz.comm.util.CommUtil;

public class BTUtilTR {
	protected static Logger logger = LoggerFactory.getLogger(BTUtilTR.class);
	
	BTConvert convert = new BTConvert();
	BTUtil util = new BTUtil();
    
    /** 
     * 2012.01.16 신규추가 
     * 수정자   : 남웅주
     * 수정내용 : 별도 - 신규 
     * MCI인경우 repeat가 occurs 인경우
     * **/
    @SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	public int convTR2Occurs(Hashtable h, List outList, String outBlockName, ArrayList<HashMap> bldOutBlockParamList, int startPos, HashMap systemMsg) throws Exception {
    	ArrayList<HashMap> outBlockDataList = null;	
    	
    	HashMap map = null;    	
    	byte[] strPData = null;
    	byte[] occursData = null;    
    	int rowCount = 0;    	
    	byte[] ob01 = null;
    	if(h.containsKey(outBlockName)){
    		if(!h.get(outBlockName).equals("")) ob01 = (byte[])h.get(outBlockName);
    	}
    	String rMessage = (String)h.get("msg");
    	String subMessage = (String)h.get("submsg");
		int fieldSizeSum = 0;
    	
    	if(ob01 != null && ob01.length > startPos){
    		outBlockDataList = new ArrayList<HashMap>();	    		
			strPData = util.getAttrValue(ob01, startPos, ob01.length-startPos);
			h.put("rowCnt", util.getBytePData(strPData, 0, 4));
			rowCount = convert.toInt(h.get("rowCnt").toString());
    		occursData = util.getAttrValue(strPData, 4, strPData.length);
    		if (rowCount > 0) {
    			int fieldSize = 0;
    			String fieldType = null;
    			byte[] fieldData = null;
    			
	    		for (int i=0; i<rowCount; i++) {
    	    		map = new HashMap();
	    			for (int j=0; j<bldOutBlockParamList.size(); j++) {
	    	    		fieldSize = convert.toInt(bldOutBlockParamList.get(j).get("size"));  	//out 블럭에 속한 하위 field 바이트수
	    	    		fieldType = convert.toString(bldOutBlockParamList.get(j).get("type")); 	//out 블럭에 속한 하위 field 데이터 타입

	    	    		fieldData = util.getAttrValue(occursData, fieldSizeSum, fieldSize);
	    	    		if(fieldType.equals("numstring")){
	    	    			map.put(bldOutBlockParamList.get(j).get("name"), util.numberSignProc(util.getBytePData(fieldData, 0, fieldData.length)).trim());
	    	    		}else{
	    	    			map.put(bldOutBlockParamList.get(j).get("name"), util.getBytePData(fieldData, 0, fieldData.length).trim());
	    	    		}
	    				fieldSizeSum += convert.toInt(bldOutBlockParamList.get(j).get("size")); //out 블럭 한 레코드에 속한 필드의 길이누적
	    				
//	    				System.out.println("------------------------------------------------------------------------");
//	    				System.out.println(" fieldSize :: [" + Integer.toString(fieldSize) + "]" );
//	    				System.out.println(" fieldType :: [" + fieldType + "]" );
//	    				System.out.println(" name :: [" + bldOutBlockParamList.get(j).get("name") + "]" );
//	    				System.out.println(" fieldData :: [" + util.getBytePData(fieldData, 0, fieldData.length) + "]" );
	    			} 
    	    		outBlockDataList.add(map);
	    		}
    		}
    		startPos += fieldSizeSum + 4;
    		outList.add(outBlockDataList);
    	}	 	
		//시스템메시지 맵 설정
    	systemMsg.put("msg",     rMessage.trim());
    	systemMsg.put("submsg",  subMessage.trim());
    	systemMsg.put("count",   rowCount);
    	systemMsg.put("currPos", startPos);
    	
    	return startPos;
    }
    
    /** 
     * 2012.01.16 신규추가 
     * 수정자   : 남웅주
     * 수정내용 : 별도 - 신규 
     * MCI인경우 repeat가 1 인경우
     * **/
    @SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public int convTR2One(Hashtable h, List outList, String outBlockName, ArrayList<HashMap> bldOutBlockParamList, int startPos, HashMap systemMsg) throws Exception {
    	ArrayList<HashMap> outBlockDataList = null;	
    	
    	HashMap map = null;    	
    	byte[] strPData = null;
    	int rowCount = 1;    	
    	byte[] ob01 = null;
    	if(h.containsKey(outBlockName)){
    		if(!h.get(outBlockName).equals("")) ob01 = (byte[])h.get(outBlockName);
    	}
    	String rMessage = (String)h.get("msg");
    	String subMessage = (String)h.get("submsg");
    	
		int fieldSizeSum = 0;
    	
    	if(ob01 != null && ob01.length > startPos){
    		outBlockDataList = new ArrayList<HashMap>();	
    		
			strPData = util.getAttrValue(ob01, startPos, ob01.length-startPos);
    		if (rowCount > 0) {
    			int fieldSize = 0;
    			String fieldType = null;
    			byte[] fieldData = null;
    			
	    		map = new HashMap();
    			for (int j=0; j<bldOutBlockParamList.size(); j++) {
    	    		fieldSize = convert.toInt(bldOutBlockParamList.get(j).get("size"));  	//out 블럭에 속한 하위 field 바이트수
    	    		fieldType = convert.toString(bldOutBlockParamList.get(j).get("type")); 	//out 블럭에 속한 하위 field 데이터 타입

    	    		fieldData = util.getAttrValue(strPData, fieldSizeSum, fieldSize);
    	    		if(fieldType.equals("numstring")){
    	    			map.put(bldOutBlockParamList.get(j).get("name"), util.numberSignProc(util.getBytePData(fieldData, 0, fieldData.length)).trim());
    	    		}else{
    	    			map.put(bldOutBlockParamList.get(j).get("name"), util.getBytePData(fieldData, 0, fieldData.length).trim());
    	    		}
    	    		fieldSizeSum += convert.toInt(bldOutBlockParamList.get(j).get("size")); //out 블럭 한 레코드에 속한 필드의 길이누적
    			} 
	    		outBlockDataList.add(map);
    		}
    		startPos += fieldSizeSum;
    		outList.add(outBlockDataList);
    	}	 	
		//시스템메시지 맵 설정
    	systemMsg.put("msg",     rMessage.trim());
    	systemMsg.put("submsg",  subMessage.trim());
    	systemMsg.put("count",   rowCount);
    	systemMsg.put("currPos", startPos);
    		
    	return startPos;
    }    
    
    /** 
     * 2012.02.22 신규추가 
     * 수정자   : 남웅주
     * 수정내용 : 별도 - 신규 
     * MCI인경우 NEXT 블럭에 값이 있는 경우 처리
     * **/
    @SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public void convTR2Next(Hashtable h, HashMap outNext, String nextBlockName, ArrayList<HashMap> bldNextBlockParamList) throws Exception {   	
    	byte[] strPData = null;
    	byte[] ob01 = null;
    	if(h.containsKey("OUT_DATA_NEXT")){
    		if(!h.get("OUT_DATA_NEXT").equals("")) ob01 = (byte[])h.get("OUT_DATA_NEXT");
    	}
		int fieldSizeSum = 0;
    	
    	if(ob01 != null && ob01.length > 0){
			strPData = util.getAttrValue(ob01, 0, ob01.length);
			int fieldSize = 0;
			String fieldType = null;
			byte[] fieldData = null;
			
			for (int j=0; j<bldNextBlockParamList.size(); j++) {
	    		fieldSize = convert.toInt(bldNextBlockParamList.get(j).get("size"));  		//next 블럭에 속한 하위 field 바이트수
	    		fieldType = convert.toString(bldNextBlockParamList.get(j).get("type")); 	//next 블럭에 속한 하위 field 데이터 타입

	    		fieldData = util.getAttrValue(strPData, fieldSizeSum, fieldSize);
	    		if(fieldType.equals("numstring")){
	    			outNext.put(bldNextBlockParamList.get(j).get("name"), util.numberSignProc(util.getBytePData(fieldData, 0, fieldData.length)).trim());
	    		}else{
	    			outNext.put(bldNextBlockParamList.get(j).get("name"), util.getBytePData(fieldData, 0, fieldData.length).trim());
	    		}
	    		fieldSizeSum += convert.toInt(bldNextBlockParamList.get(j).get("size")); //next 블럭 한 레코드에 속한 필드의 길이누적
			} 
    	}	 	
    }    
    
    /** 
     * 2012.02.09 신규추가 
     * 수정자   : 남웅주
     * 수정내용 : 별도 - 신규 
     * MCI인경우 repeat가 occurs 인경우
     * **/
    @SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	public int convFID2List(Hashtable h, ArrayList<ArrayList<HashMap>> outList, ArrayList<HashMap> bldOutBlockNameList, ArrayList<ArrayList<HashMap>> bldOutBlockParamList) throws Exception {
    	ArrayList<HashMap> outBlockDataList = null;	
    	HashMap outBlockMap = null;    	
    	HashMap outBlockHeadMap = null;    	

    	byte[] strPData = null;
    	byte[] ob01 = null;
		
		int startPos = 0;
		int endPos = 0;
		int rowCnt = 0;
		int paramStartPos = 0;
		int paramEndPos = 0;
		int recordEndPos = 0;
		int dataPos = 0;
    	String saveBuffLen = "";
    	if(h.containsKey("OUT_DATA")){
    		if(!h.get("OUT_DATA").equals("")) ob01 = (byte[])h.get("OUT_DATA");
    	}
    	
    	if(ob01 != null){
	    	for(int i=0; i < bldOutBlockNameList.size(); i++){
	    		endPos = util.ArrayByteFind(ob01, TranConstant.FD_GS, startPos);
	    		strPData = util.getAttrValue(ob01, startPos, endPos);
	    		startPos = endPos+1;
    	    	outBlockDataList = new ArrayList<HashMap>();
	    		if(bldOutBlockNameList.get(i).get("repeat").equals("occurs")){
	    			dataPos = 1;
	    			//배열헤더셋팅
	    	    	outBlockHeadMap = new HashMap();    		    			
	    	    	outBlockHeadMap.put("DataLen", util.getBytePData(strPData, dataPos, TranConstant.DATA_LEN_SIZE));
	    	    	dataPos += TranConstant.DATA_LEN_SIZE;
	    	    	outBlockHeadMap.put("RowCount", util.getBytePData(strPData, dataPos, TranConstant.ROW_COUNT_SIZE));
	    	    	dataPos += TranConstant.ROW_COUNT_SIZE;	    	    	
	    	    	outBlockHeadMap.put("ActionKey", util.getBytePData(strPData, dataPos, TranConstant.ACTION_KEY_SIZE));
	    	    	dataPos += TranConstant.ACTION_KEY_SIZE;	    	    		    	    	
	    	    	outBlockHeadMap.put("Status", util.getBytePData(strPData, dataPos, TranConstant.STATUS_SIZE));
	    	    	dataPos += TranConstant.STATUS_SIZE;	    	    	
	    	    	outBlockHeadMap.put("SaveBuffLen", util.getBytePData(strPData, dataPos, TranConstant.SAVE_BUFF_LEN_SIZE));
	    	    	dataPos += TranConstant.SAVE_BUFF_LEN_SIZE;	
	    	    	
	    	    	saveBuffLen = outBlockHeadMap.get("SaveBuffLen").toString().trim();
	    	    	if(!"".equals(saveBuffLen)){
	    	    		outBlockHeadMap.put("SaveBuff", util.getBytePData(strPData, dataPos, Integer.parseInt(saveBuffLen)));	    	    	              
	    	    		dataPos += Integer.parseInt((String)outBlockHeadMap.get("SaveBuffLen"));
	    	    	}
	    	    	outBlockDataList.add(outBlockHeadMap);
	    			//실데이터블럭	    			
	    	    	rowCnt = Integer.parseInt((String)outBlockHeadMap.get("RowCount"));
	    	    	byte[] tmpPData = null;
	    	    	for(int j=0;j<rowCnt;j++){	    	    	
	    	    		recordEndPos = util.ArrayByteFind(strPData, TranConstant.FD_RS, dataPos);	    	    		
	    	    		tmpPData = util.getAttrValue(strPData, dataPos, recordEndPos-dataPos);
	    	    		dataPos = recordEndPos+1;
	    	    		paramStartPos = 0;
	    	    		paramEndPos = 0;
	    	    		outBlockMap = new HashMap();
		    	    	for(int k=0;k<bldOutBlockParamList.get(i).size();k++){		    	    		
		    	    		paramEndPos = util.ArrayByteFind(tmpPData, TranConstant.FD_FS, paramStartPos);		    	    		
		    	    		outBlockMap.put(bldOutBlockParamList.get(i).get(k).get("name"), util.getBytePData(tmpPData, paramStartPos, paramEndPos-paramStartPos));
		    	    		paramStartPos = paramEndPos+1;		    	    		
		    	    	}
		    	    	outBlockDataList.add(outBlockMap);
	    	    	}
	    		}else{
	    			dataPos = 0;
    	    		paramStartPos = 0;
    	    		paramEndPos = 0;
    	    		outBlockMap = new HashMap();
	    	    	for(int k=0;k<bldOutBlockParamList.get(i).size();k++){
	    	    		paramEndPos = util.ArrayByteFind(strPData, TranConstant.FD_FS, paramStartPos);		    	    		
	    	    		outBlockMap.put(bldOutBlockParamList.get(i).get(k).get("name"), util.getBytePData(strPData, paramStartPos, paramEndPos-paramStartPos));
	    	    		paramStartPos = paramEndPos+1;
	    	    	}
	    	    	outBlockDataList.add(outBlockMap);
	    		}
	    		outList.add(outBlockDataList);	    		
	    	}
    	}
    	return startPos;
    }	

	//CommonProperties 정보를 읽어서 통신에 필요한 정보를 설정한다.
	public static boolean initConnectInfo(HttpServletRequest request){
		boolean resultBool = true;
		try{
			//서버에 올릴때는 반드시 아래 2 라인을 주석으로 막으시오.
//			CommonProperties.setDefaultPropertiesPath(request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separatorChar+"config"+File.separatorChar+"meritz.properties");
//			CommonProperties.setDefaultBldPath(request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separatorChar+"bld"+File.separatorChar);		
			
//			System.out.println("CommonProperties.getKey(\"DEFAULT_SERVER\") :: [" + CommonProperties.getKey("DEFAULT_SERVER") + "]");
//			System.out.println("CommonProperties.getKey(\"DEFAULT_PORT\") :: [" + CommonProperties.getKey("DEFAULT_PORT") + "]");
			CommonProperties.setServerIp(CommonProperties.getKey(CommonProperties.getKey("DEFAULT_SERVER")));
			CommonProperties.setServerPort(CommonProperties.getKey(CommonProperties.getKey("DEFAULT_PORT"))); 
			logger.debug("#################################################");	
			logger.debug("## CommonProperties.getDefaultPropertiesPath()  ## : {} ", CommonProperties.getDefaultPropertiesPath());			
			logger.debug("## CommonProperties.getDefaultBldPath           ## : {} ", CommonProperties.getDefaultBldPath());			
			logger.debug("## CommonProperties.getServerIp()               ## : {} ", CommonProperties.getServerIp());			
			logger.debug("## CommonProperties.getServerPort()             ## : {} ", CommonProperties.getServerPort());			
			logger.debug("#################################################");				

			//20190712_01 LKM START
			HttpSession session = request.getSession();			
			if(session.getAttribute("_SESSION_LOGIN_INFO") != null){
				LognMangBean bean = (LognMangBean)session.getAttribute("_SESSION_LOGIN_INFO");
				CommonProperties.setLognSessionKey(bean.getSessionKey());
			}	
		
//				if(bean.getUserIp().trim().equals("")){
////					CommonProperties.setLognUserIp(CommonProperties.getServerIp());
//					if (session.getAttribute("_SESSION_NOLOGIN_IP") != null)
//						CommonProperties.setLognUserIp(session.getAttribute("_SESSION_NOLOGIN_IP").toString());
//					else 	
//						CommonProperties.setLognUserIp(request.getRemoteAddr());
//				}else{
//					CommonProperties.setLognUserIp(bean.getUserIp());
//				}	
			
			//20190712_01 LKM END
			
//			request.setAttribute("BTUtilPubIp", getRealUserIp(request));
//			request.setAttribute("BTUtilPrivIp", getPrivateUserIp(request));
			
			CommonProperties.setLognUserIp(getRealUserIp(request));
			CommonProperties.setLognUserPrivIp(getPrivateUserIp(request)); // 2018.10.31  사설 ip 저장  로그인 할때 세션 저장 필요.
			
			// 20230313 System.out.println("###############initConnectInfo(request################" + getRealUserIp(request));
			// 20230313 System.out.println("###############initConnectInfo(request.PrivIP################" + getPrivateUserIp(request));
			// 20230313 System.out.println("###############setLognUserIp(request################" + CommonProperties.getLognUserIp());
			// 20230313 System.out.println("###############setLognUserPrivIp(request.PrivIP################" + CommonProperties.getLognUserPrivIp());
			
		}catch(Exception e){
			resultBool = false;
			e.printStackTrace();
		}
		return resultBool;
	}

	//CommonProperties 정보를 읽어서 통신에 필요한 정보를 설정한다.
	public static boolean initConnectInfo(HttpServletRequest request, int serverFlag){
		boolean resultBool = true;
		try{
			//서버에 올릴때는 반드시 아래 2 라인을 주석으로 막으시오.			
//			CommonProperties.setDefaultPropertiesPath(request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separatorChar+"config"+File.separatorChar+"meritz.properties");
//			CommonProperties.setDefaultBldPath(request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separatorChar+"bld"+File.separatorChar);		

			switch(serverFlag){
				case TranConstant.DEV_SERVER_A:
					CommonProperties.setServerIp(CommonProperties.getKey("DEV_SERVER_A_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("DEV_SERVER_A_PORT")); 
					break;
				case TranConstant.DEV_SERVER_B:
					CommonProperties.setServerIp(CommonProperties.getKey("DEV_SERVER_B_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("DEV_SERVER_B_PORT")); 
					break;
				case TranConstant.REAL_SERVER_A:
					CommonProperties.setServerIp(CommonProperties.getKey("REAL_SERVER_A_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("REAL_SERVER_A_PORT")); 
					break;
				case TranConstant.REAL_SERVER_B:
					CommonProperties.setServerIp(CommonProperties.getKey("REAL_SERVER_B_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("REAL_SERVER_B_PORT")); 
					break;
				case TranConstant.REAL_SERVER_C:
					CommonProperties.setServerIp(CommonProperties.getKey("REAL_SERVER_C_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("REAL_SERVER_C_PORT")); 
					break;
				case TranConstant.REAL_SERVER_D:
					CommonProperties.setServerIp(CommonProperties.getKey("REAL_SERVER_D_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("REAL_SERVER_D_PORT")); 
					break;
				case TranConstant.TEST_SERVER_A:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_A_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_A_PORT")); 
					break;
				case TranConstant.TEST_SERVER_B:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_B_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_B_PORT")); 
					break;
				case TranConstant.TEST_SERVER_C:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_C_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_C_PORT")); 
					break;
				case TranConstant.TEST_SERVER_D:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_D_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_D_PORT")); 
					break;
				case TranConstant.TEST_SERVER_E:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_E_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_E_PORT")); 
					break;
				case TranConstant.TEST_SERVER_F:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_F_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_F_PORT")); 
					break;
				case TranConstant.TEST_SERVER_G:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_G_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_G_PORT")); 
					break;
				case TranConstant.TEST_SERVER_H:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_H_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_H_PORT")); 
					break;
				case TranConstant.TEST_SERVER_I:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_I_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_I_PORT")); 
					break;
				case TranConstant.TEST_SERVER_J:
					CommonProperties.setServerIp(CommonProperties.getKey("TEST_SERVER_J_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("TEST_SERVER_J_PORT")); 
					break;
				case TranConstant.PBCR_SERVER_A:
					CommonProperties.setServerIp(CommonProperties.getKey("PBCR_SERVER_A_IP"));
					CommonProperties.setServerPort(CommonProperties.getKey("PBCR_SERVER_A_PORT")); 
					break;
			}

			logger.debug("#################################################");	
			logger.debug("## CommonProperties.getDefaultPropertiesPath()  ## : {} ", CommonProperties.getDefaultPropertiesPath());			
			logger.debug("## CommonProperties.getDefaultBldPath           ## : {} ", CommonProperties.getDefaultBldPath());			
			logger.debug("## CommonProperties.getServerIp()               ## : {} ", CommonProperties.getServerIp());			
			logger.debug("## CommonProperties.getServerPort()             ## : {} ", CommonProperties.getServerPort());			
			logger.debug("#################################################");	

			
			//20190712_01 LKM START
			HttpSession session = request.getSession();			
			if(session.getAttribute("_SESSION_LOGIN_INFO") != null){
				LognMangBean bean = (LognMangBean)session.getAttribute("_SESSION_LOGIN_INFO");
				CommonProperties.setLognSessionKey(bean.getSessionKey());
			}

//				if(bean.getUserIp().trim().equals("")){
////					CommonProperties.setLognUserIp(CommonProperties.getServerIp());	
//					if (session.getAttribute("_SESSION_NOLOGIN_IP") != null)
//						CommonProperties.setLognUserIp(session.getAttribute("_SESSION_NOLOGIN_IP").toString());
//					else 						
//						CommonProperties.setLognUserIp(request.getRemoteAddr());					
//				}else{
//					CommonProperties.setLognUserIp(bean.getUserIp());
//				}								
				
			
			//20190712_01 LKM END
			
//			request.setAttribute("BTUtilPubIp", getRealUserIp(request));
//			request.setAttribute("BTUtilPrivIp", getPrivateUserIp(request));
			
			CommonProperties.setLognUserIp(getRealUserIp(request));
			CommonProperties.setLognUserPrivIp(getPrivateUserIp(request)); // 2018.10.31  사설 ip 저장  로그인 할때 세션 저장 필요.
			
			System.out.println("###############initConnectInfo2(request################" + getRealUserIp(request));
			System.out.println("###############initConnectInfo2(request.PrivIP################" + getPrivateUserIp(request));
			System.out.println("###############setLognUserIp2(request################" + CommonProperties.getLognUserIp());
			System.out.println("###############setLognUserPrivIp2(request.PrivIP################" + CommonProperties.getLognUserPrivIp());
			
		}catch(Exception e){
			resultBool = false;
			e.printStackTrace();
		}
		return resultBool;
	}
	
	// 실제 고객 ip 가져 오기
	public static String getRealUserIp (HttpServletRequest request) {
		String userip = "000.000.000.000";
		userip = request.getHeader("X-Forwarded-For");
		
		if (userip==null || userip.length()==0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getHeader("Proxy-Client-IP");
			logger.debug("###############getRealUserIp(Proxy-Client-IP)################" + userip);
		} else {
			logger.debug("###############getRealUserIp(X-Forwarded-For)################" + userip);
			userip = CommUtil.gtnFormatIpAddr(userip);
			return userip;
		}
		
		if (userip==null || userip.length()==0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getHeader("WL-Proxy-Client-IP");
			logger.debug("###############getRealUserIp(WL-Proxy-Client-IP)################" + userip);
		} else {
			userip = CommUtil.gtnFormatIpAddr(userip);
			return userip;
		}
		
		if (userip==null || userip.length()==0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getHeader("HTTP_CLIENT_IP");
			logger.debug("###############getRealUserIp(HTTP_CLIENT_IP)################" + userip);
		} else {
			userip = CommUtil.gtnFormatIpAddr(userip);
			return userip;
		}
		
		if (userip==null || userip.length()==0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getHeader("HTTP_X_FORWARDED_FOR");
			logger.debug("###############getRealUserIp(HTTP_X_FORWARDED_FOR)################" + userip);
		} else {
			userip = CommUtil.gtnFormatIpAddr(userip);
			return userip;
		}
		
		if (userip==null || userip.length()==0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getRemoteAddr();
			logger.debug("###############getRealUserIp(getRemoteAddr)################" + userip);
		}
		
		userip = CommUtil.gtnFormatIpAddr(userip);
		System.out.println("[getRealUserIp] pubIp:" + userip);
		
		return userip;
	}
	
	// 고객 사설 ip 가져 오기  2018.10.31
	public static String getPrivateUserIp (HttpServletRequest request) {
		
		String privIp = "000.000.000.000";
		

		try {
			privIp = CommUtil.gtnFormatIpAddr(request.getRemoteAddr().toString());
			
			HttpSession session = request.getSession();
			if (session.getAttribute("LOGIN_PRIV_IP")!=null && session.getAttribute("LOGIN_PRIV_IP")!="") {
				privIp = CommUtil.gtnFormatIpAddr( session.getAttribute("LOGIN_PRIV_IP").toString());
			}
		} catch (Exception e) {
			System.out.println("[getPrivateUserIp] error:" + e.getMessage());
		}
		
		System.out.println("getPrivateUserIp[PrivIP]:" + privIp);
		
		return privIp;		
	}
	
}
