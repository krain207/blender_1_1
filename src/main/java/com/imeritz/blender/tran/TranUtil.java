package com.imeritz.blender.tran;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

//import javax.servlet.http.HttpServletRequest;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class TranUtil {
	protected static Logger logger = LoggerFactory.getLogger(TranUtil.class);
	//CommonProperties 정보를 읽어서 통신에 필요한 정보를 설정한다.
	public static boolean initConnectInfo(HttpServletRequest request){
		return BTUtilTR.initConnectInfo(request);
	}
	public static boolean initConnectInfo(HttpServletRequest request, int serverFlag){
		return BTUtilTR.initConnectInfo(request, serverFlag);
	}
	
	//TR통신모듈 send ~ receive
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static HashMap sendToGateWay(Object objBean, String bldname, HashMap inHeader) throws JDOMException, IOException{
		BTUtilTR BTUtiltr = new BTUtilTR();		
		String bldInBlockName  = null;
		String bldOutBlockName = null;	
		HashMap blockInfoMap = null;
		HashMap fieldInfoMap = null;		
		HashMap xmlInfoMap = new HashMap();
		ArrayList<HashMap> bldInBlockNameList 	= new ArrayList<HashMap>(); //블록명 배열
		ArrayList<HashMap> bldOutBlockNameList 	= new ArrayList<HashMap>();
		ArrayList<HashMap> bldNextBlockNameList = new ArrayList<HashMap>();				
		ArrayList<ArrayList<HashMap>> bldInBlockParamList   = new ArrayList<ArrayList<HashMap>>(); //블록단위 파라메터 정보들 
		ArrayList<ArrayList<HashMap>> bldOutBlockParamList  = new ArrayList<ArrayList<HashMap>>();
		ArrayList<ArrayList<HashMap>> bldNextBlockParamList = new ArrayList<ArrayList<HashMap>>();
		ArrayList<HashMap> inParamList   = null;	//IN 블록안 파라메터 정보	
		ArrayList<HashMap> outParamList  = null;	//OUT 블록안 파라메터 정보 
		ArrayList<HashMap> nextParamList = null;	//NEXT 블록안 파라메터 정보	 
		
		//결과를 저장하기 위한 자료구조들...
		HashMap outMap = new HashMap();
		HashMap systemMsg = new HashMap();
		ArrayList<ArrayList<HashMap>> outList = new ArrayList<ArrayList<HashMap>>();
		HashMap outNext = new HashMap();
		
		//xml정보 파싱
		xmlInfoParse(bldname, xmlInfoMap, bldInBlockNameList, bldOutBlockNameList, bldNextBlockNameList,
					                      bldInBlockParamList, bldOutBlockParamList, bldNextBlockParamList);		
		
		logger.info("<<-------------------------[sendToGateWay]--------------------------->>");

		String strByte = makeInputBlock(objBean, bldInBlockNameList, bldInBlockParamList);
		
		HashMap outBlockInfo = null;
				
		try {
			if(inHeader == null){
				inHeader = new HashMap();
			}
			inHeader.put("FID", "N");
			if(inHeader.get("NextYn") != null){
				if(inHeader.get("NextYn").equals("Y")){					
					setNextBlockInfo(bldNextBlockParamList.get(0), inHeader);
				}
			}
			
	    	//통신 요청
			Hashtable rcvMap = null;
	    	BldTRCall wstr = new BldTRCall();
	    	String [] outblocks = new String[bldOutBlockNameList.size()];
	    	for(int i=0;i<outblocks.length;i++){
	    		outblocks[i] = (String)bldOutBlockNameList.get(i).get("name");
	    	}
	    	rcvMap = wstr.callTR(bldname, outblocks, strByte.getBytes(), inHeader);
	    	
	    	//데이터 파싱
	    	if(rcvMap.get("error").equals("0")){
	    		if(!rcvMap.get("msg").equals("error")){
			    	int currPos = 0;
			    	try {
				    	for(int i=0; i < bldOutBlockNameList.size(); i++){
				    		bldOutBlockName = (String)bldOutBlockNameList.get(i).get("name");
				    		if(bldOutBlockNameList.get(i).get("repeat").equals("occurs")){
			    				currPos = BTUtiltr.convTR2Occurs(rcvMap, outList, bldOutBlockName, bldOutBlockParamList.get(i), currPos, systemMsg);				    				
				    		}else{
			    				currPos = BTUtiltr.convTR2One(rcvMap, outList, bldOutBlockName, bldOutBlockParamList.get(i), currPos, systemMsg);				    				
				    		}
				    	}
				    	if(bldNextBlockNameList.size() > 0){   
				    		BTUtiltr.convTR2Next(rcvMap, outNext, bldNextBlockNameList.get(0).toString(), bldNextBlockParamList.get(0));
				    	}
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
	    	}	    	
	    	
//	    	System.out.println(" MESSAGE :: [ERROR] [" + (String)rcvMap.get("error") + "]");
//	    	System.out.println(" MESSAGE :: [MSG] [" + (String)rcvMap.get("msg") + "]");
//	    	System.out.println(" MESSAGE :: [SUBMSG] [" + (String)rcvMap.get("submsg") + "]");
//	    	System.out.println(" MESSAGE :: [MSGCODE] [" + (String)rcvMap.get("msgcode") + "]");
//	    	System.out.println(" MESSAGE :: [NEXTYN] [" + (String)rcvMap.get("nextyn") + "]");
	    	
			outMap.put("sysMsg", (String)rcvMap.get("msg"));
			outMap.put("sysSubMsg", (String)rcvMap.get("submsg"));
			outMap.put("sysCode", (String)rcvMap.get("error"));
			outMap.put("sysMsgCode", (String)rcvMap.get("msgcode"));
			outMap.put("sysNextYn", (String)rcvMap.get("nextyn"));
	    	
//			메시지만 저장된 outMap에 실자료의 리스트를 저장한다.
			outMap.put("resultList", outList);
			outMap.put("resultNext", outNext);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String outputString = outMap.toString().replace("[", "[\n");
		outputString = outputString.replace("]", "\n]");
		outputString = outputString.replace("},", "},\n");
				
//		System.out.println(outputString);
		
		return outMap;
	}
	
	//TR통신모듈 send ~ receive
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static HashMap sendToGateWayRD(Object objBean, String bldname, HashMap inHeader) throws JDOMException, IOException{
		BTUtilTR BTUtiltr = new BTUtilTR();		
		String bldInBlockName  = null;
		String bldOutBlockName = null;	
		HashMap blockInfoMap = null;
		HashMap fieldInfoMap = null;		
		HashMap xmlInfoMap = new HashMap();
		ArrayList<HashMap> bldInBlockNameList 	= new ArrayList<HashMap>(); //블록명 배열
		ArrayList<HashMap> bldOutBlockNameList 	= new ArrayList<HashMap>();
		ArrayList<HashMap> bldNextBlockNameList = new ArrayList<HashMap>();				
		ArrayList<ArrayList<HashMap>> bldInBlockParamList   = new ArrayList<ArrayList<HashMap>>(); //블록단위 파라메터 정보들 
		ArrayList<ArrayList<HashMap>> bldOutBlockParamList  = new ArrayList<ArrayList<HashMap>>();
		ArrayList<ArrayList<HashMap>> bldNextBlockParamList = new ArrayList<ArrayList<HashMap>>();
		ArrayList<HashMap> inParamList   = null;	//IN 블록안 파라메터 정보	
		ArrayList<HashMap> outParamList  = null;	//OUT 블록안 파라메터 정보 
		ArrayList<HashMap> nextParamList = null;	//NEXT 블록안 파라메터 정보	 
		
		//결과를 저장하기 위한 자료구조들...
		HashMap outMap = new HashMap();
		HashMap systemMsg = new HashMap();
		ArrayList<ArrayList<HashMap>> outList = new ArrayList<ArrayList<HashMap>>();
		//  RD 연속조회 오류 수정 2013.01.24  start
		HashMap outNext = new HashMap();
		//  RD 연속조회 오류 수정 end
		
		//xml정보 파싱
		xmlInfoParse(bldname, xmlInfoMap, bldInBlockNameList, bldOutBlockNameList, bldNextBlockNameList,
					                      bldInBlockParamList, bldOutBlockParamList, bldNextBlockParamList);		
		
		logger.info("<<-------------------------[sendToGateWayRD]--------------------------->>");
		
		String strByte = makeInputBlock(objBean, bldInBlockNameList, bldInBlockParamList);
		
		HashMap outBlockInfo = null;
				
		try {
			if(inHeader == null){
				inHeader = new HashMap();
			}
			inHeader.put("FID", "N");
			//  RD 연속조회 오류 수정 2013.01.24  start
			if(inHeader.get("NextYn") != null){
				if(inHeader.get("NextYn").equals("Y")){					
					setNextBlockInfo(bldNextBlockParamList.get(0), inHeader);
				}
			}
			// RD 연속조회 오류 수정 end
			
	    	//통신 요청
			Hashtable rcvMap = null;
	    	BldTRCall wstr = new BldTRCall();
	    	String [] outblocks = new String[bldOutBlockNameList.size()];
	    	for(int i=0;i<outblocks.length;i++){
	    		outblocks[i] = (String)bldOutBlockNameList.get(i).get("name");
	    	}
	    	rcvMap = wstr.callTR(bldname, outblocks, strByte.getBytes(), inHeader);
	    	
	    	byte[] realData = (byte[])rcvMap.get("OUT_DATA");
	    	
	    	int dataParamSize = 0;
	    	int dataPosition = 0;
	    	int dataArySize = 0;
	    	
	    	if(rcvMap.get("error").equals("0")){
	    		if(!rcvMap.get("msg").equals("error")){
			    	int currPos = 0;
			    	
			    	try {
				    	for(int i=0; i < bldOutBlockNameList.size(); i++){
				    		bldOutBlockName = (String)bldOutBlockNameList.get(i).get("name");
				    		if(bldOutBlockNameList.get(i).get("repeat").equals("occurs")){
				    			dataArySize = Integer.parseInt(BTUtil.getBytePData(realData, dataPosition, 4));
				    			if(dataArySize > 0){
					    			dataPosition += 4; 
					    			dataParamSize = 0;
					    			for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
					    				dataParamSize += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	
					    			}				    			
					    			dataPosition += (dataParamSize * dataArySize);
				    			}else{
				    				dataPosition += 4;
				    			}
			    			}else{
			    				dataParamSize = 0;
				    			for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
				    				dataParamSize += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());
				    			}
				    			dataPosition += dataParamSize;
				    		}
//			    			System.out.println(" 블럭사이즈 : [" + dataPosition + "]");				    		
				    	}
				    	//  RD 연속조회 오류 수정 2013.01.24  start
				    	if(bldNextBlockNameList.size() > 0){   
				    		BTUtiltr.convTR2Next(rcvMap, outNext, bldNextBlockNameList.get(0).toString(), bldNextBlockParamList.get(0));
				    	}
				    	//  RD 연속조회 오류 수정 end
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
	    	}	    	
//	    	System.out.println(" (Byte[])rcvMap.get(\"OUT_DATA\") ::  [" + BTUtil.getBytePData(realData, 0, dataPosition) + "]");
//	    	System.out.println(" realData.length ::  [" + realData.length + "]");
//	    	System.out.println(" dataPosition ::  [" + dataPosition + "]");
//	    	
//	    	System.out.println(" MESSAGE :: [ERROR] [" + (String)rcvMap.get("error") + "]");
//	    	System.out.println(" MESSAGE :: [MSG] [" + (String)rcvMap.get("msg") + "]");
//	    	System.out.println(" MESSAGE :: [SUBMSG] [" + (String)rcvMap.get("submsg") + "]");
//	    	System.out.println(" MESSAGE :: [MSGCODE] [" + (String)rcvMap.get("msgcode") + "]");
	    	
			outMap.put("sysMsg", (String)rcvMap.get("msg"));
			outMap.put("sysSubMsg", (String)rcvMap.get("submsg"));
			outMap.put("sysCode", (String)rcvMap.get("error"));
			outMap.put("sysMsgCode", (String)rcvMap.get("msgcode"));
			//  RD 연속조회 오류 수정 2013.01.24  start
			outMap.put("sysNextYn", (String)rcvMap.get("nextyn"));
			outMap.put("resultNext", outNext);
			//  RD 연속조회 오류 수정 end
			outMap.put("reportData", bldname + BTUtil.lpad(Integer.toString(dataPosition), 10) + BTUtil.getBytePData(realData, 0, dataPosition));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return outMap;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })	
	private static void xmlInfoParse(
		String bldname,
		HashMap xmlInfoMap,
		ArrayList<HashMap> bldInBlockNameList,
		ArrayList<HashMap> bldOutBlockNameList,
		ArrayList<HashMap> bldNextBlockNameList,					
		ArrayList<ArrayList<HashMap>> bldInBlockParamList, 
		ArrayList<ArrayList<HashMap>> bldOutBlockParamList,
		ArrayList<ArrayList<HashMap>> bldNextBlockParamList) throws JDOMException, IOException{
		
		File file = new File(new String(CommonProperties.getDefaultBldPath().getBytes("ISO-8859-1"), "euc-kr")+bldname+".xml");
		FileInputStream inputStream = new FileInputStream(file);		

		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(inputStream);
		Element root 		= doc.getRootElement();
		Element info 		= root.getChild("info");
		Element input 		= root.getChild("input");		
		Element output 		= root.getChild("output");
		Element outputNext 	= root.getChild("output_next");

		HashMap blockInfoMap = null;
		HashMap fieldInfoMap = null;
		ArrayList<HashMap> inParamList   = null;	//IN 블록안 파라메터 정보	
		ArrayList<HashMap> outParamList  = null;	//OUT 블록안 파라메터 정보 
		ArrayList<HashMap> nextParamList = null;	//NEXT 블록안 파라메터 정보	 
		
		// INFO 블럭
		if(info != null){
			xmlInfoMap.put("name", 		 (String)info.getChild("name").getText());
			xmlInfoMap.put("description",(String)info.getChild("description").getText());
			xmlInfoMap.put("tuxcode",	 (String)info.getChild("tuxcode").getText());
			xmlInfoMap.put("base21", 	 (String)info.getChild("base21").getText());
			xmlInfoMap.put("attr", 		 (String)info.getChild("attr").getText());
			xmlInfoMap.put("block", 	 (String)info.getChild("block").getText());				
			xmlInfoMap.put("headtype", 	 (String)info.getChild("headtype").getText());
			xmlInfoMap.put("author", 	 (String)info.getChild("author").getText());
			xmlInfoMap.put("version", 	 (String)info.getChild("version").getText());
		}
		// INPUT 블럭
		if(input != null){
			List inputList = input.getChildren();
			for(int i=0;i<inputList.size();i++){
				Element eblock = (Element)inputList.get(i);
				blockInfoMap = new HashMap();
				blockInfoMap.put("name", eblock.getAttributeValue("name"));
				blockInfoMap.put("repeat", eblock.getAttributeValue("repeat"));				
				bldInBlockNameList.add(blockInfoMap);
				List blockList = eblock.getChildren();
				inParamList = new ArrayList<HashMap>();
				for(int j=0;j<blockList.size();j++){
					Element e = (Element)blockList.get(j);	
					fieldInfoMap = new HashMap();
					fieldInfoMap.put("label", e.getAttributeValue("label"));
					fieldInfoMap.put("name", BTUtil.upperToLower(e.getAttributeValue("name")));
					fieldInfoMap.put("uppername", e.getAttributeValue("name"));					
					fieldInfoMap.put("type", e.getAttributeValue("type"));
					fieldInfoMap.put("size", e.getAttributeValue("size"));
					
					//인블럭파라메터에 추가
					inParamList.add(fieldInfoMap);
				}
				//블록단위 파라메터 정보들 
				bldInBlockParamList.add(inParamList); 
			}
		}		
		// OUTPUT 블럭
		if(output != null){
			List outputList = output.getChildren();
			for(int i=0;i<outputList.size();i++){
				Element eblock = (Element)outputList.get(i);
				blockInfoMap = new HashMap();
				blockInfoMap.put("name", eblock.getAttributeValue("name"));
				blockInfoMap.put("repeat", eblock.getAttributeValue("repeat"));				
				//OUT 블록명 저장
				bldOutBlockNameList.add(blockInfoMap);				
				List blockList = eblock.getChildren();				
				outParamList  = new ArrayList<HashMap>(); //OUT 블록안 파라메터 정보	 
				for(int j=0;j<blockList.size();j++){
					Element e = (Element)blockList.get(j);
					fieldInfoMap = new HashMap();
					fieldInfoMap.put("label", e.getAttributeValue("label"));
					fieldInfoMap.put("name", BTUtil.upperToLower(e.getAttributeValue("name")));
					fieldInfoMap.put("uppername", e.getAttributeValue("name"));					
					fieldInfoMap.put("type", e.getAttributeValue("type"));
					fieldInfoMap.put("size", e.getAttributeValue("size"));
					
					outParamList.add(fieldInfoMap);					//OUT 블록안 파라메터 정보
				}
				bldOutBlockParamList.add(outParamList);
			}
		}
		// OUTPUT_NEXT 블럭
		if(outputNext != null){
			List outputNextList = outputNext.getChildren();

			for(int i=0;i<outputNextList.size();i++){
				Element eblock = (Element)outputNextList.get(i);
				blockInfoMap = new HashMap();
				blockInfoMap.put("name", eblock.getAttributeValue("name"));
				blockInfoMap.put("repeat", eblock.getAttributeValue("repeat"));								
				//NEXT 블록명 저장
				bldNextBlockNameList.add(blockInfoMap);
				List blockList = eblock.getChildren();
				nextParamList = new ArrayList<HashMap>();
				for(int j=0;j<blockList.size();j++){
					Element e = (Element)blockList.get(j);				
					fieldInfoMap = new HashMap();
					fieldInfoMap.put("label", e.getAttributeValue("label")); 
					fieldInfoMap.put("name", BTUtil.upperToLower(e.getAttributeValue("name")));
					fieldInfoMap.put("uppername", e.getAttributeValue("name"));					
					fieldInfoMap.put("type", e.getAttributeValue("type"));
					fieldInfoMap.put("size", e.getAttributeValue("size"));
					
					nextParamList.add(fieldInfoMap);				//OUT 블록안 파라메터 정보(대문자) 	 
				}
				bldNextBlockParamList.add(nextParamList);
			}			
		}
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int setNextBlockInfo(ArrayList<HashMap> bldNextBlockParam, HashMap inHeader){
		int resultCnt = 0;
		StringBuffer sb = new StringBuffer();
		HashMap map = null;
		
//		System.out.println("################################################################");
//		System.out.println("  bldNextBlockParam  >>> " + bldNextBlockParam.toString());
//		System.out.println("################################################################");
//		System.out.println("  inHeader  >>> " + inHeader.toString());
//		System.out.println("################################################################");
		
		for(int i=0;i<bldNextBlockParam.size();i++){
			map = (HashMap)bldNextBlockParam.get(i);				
			resultCnt += Integer.parseInt(map.get("size").toString());
			if(map.get("type").equals("string")){
				sb.append(BTUtil.rpad(((HashMap)inHeader.get("NextBlockData")).get((String)map.get("name")).toString(), Integer.parseInt(map.get("size").toString())));  
			}else if(map.get("type").equals("numstring")){				
				if(((HashMap)inHeader.get("NextBlockData")).get((String)map.get("name")).toString().trim().equals("")){
					sb.append(BTUtil.lpad(((HashMap)inHeader.get("NextBlockData")).get((String)map.get("name")).toString(), Integer.parseInt(map.get("size").toString())));
				}else {
					if(Double.parseDouble(((HashMap)inHeader.get("NextBlockData")).get((String)map.get("name")).toString()) < 0){
						sb.append("-" + BTUtil.lpad(((HashMap)inHeader.get("NextBlockData")).get((String)map.get("name")).toString().substring(1), Integer.parseInt(map.get("size").toString())-1));
					}else{
						sb.append(BTUtil.lpad(((HashMap)inHeader.get("NextBlockData")).get((String)map.get("name")).toString(), Integer.parseInt(map.get("size").toString())));
					}
				}	
			}
		}
		inHeader.put("NextLength", Integer.toString(resultCnt));
		inHeader.put("NextBlockString", sb.toString());

//		System.out.println("################################################################");
//		System.out.println("  Integer.toString(resultCnt)  >>> " + Integer.toString(resultCnt));
//		System.out.println("  sb.toString()  >>> " + sb.toString());		
//		System.out.println("################################################################");
		
		return resultCnt;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static String makeInputBlock(Object objBean, ArrayList<HashMap> bldInBlockNameList, ArrayList<ArrayList<HashMap>> bldInBlockParamList ){
		String strByte = "";
		if(objBean != null){
			try {
				HashMap inBlockInfo = null;
				ArrayList inParamArray = null;
				HashMap inParamMap = null;
				//bldInBlockNameList.size() 랑 bldInBlockParamList.size() 랑 동일하다. 
				if(bldInBlockParamList.size() > 1){			
					for(int i=0;i<bldInBlockParamList.size();i++){
						ArrayList<HashMap> aryInParamList = bldInBlockParamList.get(i);
						inBlockInfo = bldInBlockNameList.get(i);				
						if("occurs".equals(inBlockInfo.get("repeat"))){					
							inParamArray = (ArrayList)((ArrayList)objBean).get(i);						
							strByte += BTUtil.lpad(Integer.toString(inParamArray.size()), 4);
							for(int k=0;k < inParamArray.size();k++){
								inParamMap = (HashMap)inParamArray.get(k);	
								for(int j=0;j<aryInParamList.size();j++){
									if(aryInParamList.get(j).get("type").equals("string")){
										strByte += BTUtil.rpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
									}else if(aryInParamList.get(j).get("type").equals("numstring")){
										if(inParamMap.get(aryInParamList.get(j).get("name")).toString().trim().equals("")){
											strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
										}else {
											if(Double.parseDouble(inParamMap.get(aryInParamList.get(j).get("name")).toString()) < 0){
												strByte += "-" + BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString().substring(1), Integer.parseInt(aryInParamList.get(j).get("size").toString())-1);
											}else{
												strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
											}
										}	
									}
								}
							}
						}else{
							inParamMap = (HashMap)((ArrayList)objBean).get(i);
							for(int j=0;j<aryInParamList.size();j++){
								if(aryInParamList.get(j).get("type").equals("string")){									
									strByte += BTUtil.rpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
								}else if(aryInParamList.get(j).get("type").equals("numstring")){
									if(inParamMap.get(aryInParamList.get(j).get("name")).toString().trim().equals("")){
										strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
									}else {
										if(Double.parseDouble(inParamMap.get(aryInParamList.get(j).get("name")).toString()) < 0){
											strByte += "-" + BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString().substring(1), Integer.parseInt(aryInParamList.get(j).get("size").toString())-1);
										}else{
											strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
										}
									}	
								}
							}					
						}
					}
				}else{
					ArrayList<HashMap> aryInParamList = bldInBlockParamList.get(0);
					inBlockInfo = bldInBlockNameList.get(0);				
					if("occurs".equals(inBlockInfo.get("repeat"))){					
						inParamArray = (ArrayList)objBean;						
						strByte += BTUtil.lpad(Integer.toString(inParamArray.size()), 4);
						for(int k=0;k < inParamArray.size();k++){
							inParamMap = (HashMap)inParamArray.get(k);						
							for(int j=0;j<aryInParamList.size();j++){
								if(aryInParamList.get(j).get("type").equals("string")){
									strByte += BTUtil.rpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
								}else if(aryInParamList.get(j).get("type").equals("numstring")){
									if(inParamMap.get(aryInParamList.get(j).get("name")).toString().trim().equals("")){
										strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
									}else {
										if(Double.parseDouble(inParamMap.get(aryInParamList.get(j).get("name")).toString()) < 0){
											strByte += "-" + BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString().substring(1), Integer.parseInt(aryInParamList.get(j).get("size").toString())-1);
										}else{
											strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
										}
									}	
								}
							}
						}
					}else{
						inParamMap = (HashMap)objBean;
						for(int j=0;j<aryInParamList.size();j++){
							if(aryInParamList.get(j).get("type").equals("string")){
								strByte += BTUtil.rpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
							}else if(aryInParamList.get(j).get("type").equals("numstring")){
								if(inParamMap.get(aryInParamList.get(j).get("name")).toString().trim().equals("")){
									strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
								}else {
									if(Double.parseDouble(inParamMap.get(aryInParamList.get(j).get("name")).toString()) < 0){
										strByte += "-" + BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString().substring(1), Integer.parseInt(aryInParamList.get(j).get("size").toString())-1);
									}else{
										strByte += BTUtil.lpad(inParamMap.get(aryInParamList.get(j).get("name")).toString(), Integer.parseInt(aryInParamList.get(j).get("size").toString()));
									}
								}	
							}
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}	
		}
		return strByte;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })	
	private static void xmlOutBlockParse(
		String bldname,
		ArrayList<HashMap> bldOutBlockNameList,
		ArrayList<ArrayList<HashMap>> bldOutBlockParamList) throws JDOMException, IOException{
		
		File file = new File(new String(CommonProperties.getDefaultBldPath().getBytes("ISO-8859-1"), "euc-kr")+bldname+".xml");
		FileInputStream inputStream = new FileInputStream(file);		

		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(inputStream);
		Element root 		= doc.getRootElement();
		Element output 		= root.getChild("output");

		HashMap blockInfoMap = null;
		HashMap fieldInfoMap = null;
		ArrayList<HashMap> outParamList  = null;	//OUT 블록안 파라메터 정보 
		
		// OUTPUT 블럭
		if(output != null){
			List outputList = output.getChildren();
			for(int i=0;i<outputList.size();i++){
				Element eblock = (Element)outputList.get(i);
				blockInfoMap = new HashMap();
				blockInfoMap.put("name", eblock.getAttributeValue("name"));
				blockInfoMap.put("repeat", eblock.getAttributeValue("repeat"));				
				//OUT 블록명 저장
				bldOutBlockNameList.add(blockInfoMap);				
				List blockList = eblock.getChildren();				
				outParamList  = new ArrayList<HashMap>(); //OUT 블록안 파라메터 정보	 
				for(int j=0;j<blockList.size();j++){
					Element e = (Element)blockList.get(j);
					fieldInfoMap = new HashMap();
					fieldInfoMap.put("label", e.getAttributeValue("label"));
					fieldInfoMap.put("name", BTUtil.upperToLower(e.getAttributeValue("name")));
					fieldInfoMap.put("uppername", e.getAttributeValue("name"));					
					fieldInfoMap.put("type", e.getAttributeValue("type"));
					fieldInfoMap.put("size", e.getAttributeValue("size"));
					
					outParamList.add(fieldInfoMap);					//OUT 블록안 파라메터 정보
				}
				bldOutBlockParamList.add(outParamList);
			}
		}
	}
		
	public static String setOutBlockRD(String TrCode, HashMap bean, String reportData) throws JDOMException, IOException{
		String bldOutBlockName = null;	
		ArrayList<HashMap> bldOutBlockNameList 	= new ArrayList<HashMap>();
		ArrayList<ArrayList<HashMap>> bldOutBlockParamList  = new ArrayList<ArrayList<HashMap>>();
		ArrayList<HashMap> outParamList  = null;	//OUT 블록안 파라메터 정보 
		ArrayList<HashMap> rdOutBlockList = null;
		xmlOutBlockParse(TrCode, bldOutBlockNameList, bldOutBlockParamList);
		
		byte[] reportByte = reportData.getBytes(); 	
		byte[] tempByte = new byte[1024*128];
		byte[] reportTempByte = new byte[1024*128];
		
		int dataPosition = TrCode.length() + 10;
		int dataArySize = 0;
		int dataParamSize = 0;
		int tempPosition = 0;
		int rdDataFullSize =0;
		HashMap outblockMap = null; 
				
		rdDataFullSize = Integer.parseInt(BTUtil.getBytePData(reportByte, TrCode.length(), 10));         
		
    	for(int i=0; i < bldOutBlockNameList.size(); i++){
    		bldOutBlockName = (String)bldOutBlockNameList.get(i).get("name");
    		if(bldOutBlockNameList.get(i).get("repeat").equals("occurs")){
    			rdOutBlockList = (ArrayList)bean.get(bldOutBlockName);   
        		if(rdOutBlockList == null){
            		dataArySize = Integer.parseInt(BTUtil.getBytePData(reportByte, dataPosition, 4));
        			if(dataArySize > 0){
    	    			dataPosition += 4;
    	    			dataParamSize = 0;	
    	    			for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
    	    				dataParamSize += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
	    				}
    	    			dataPosition += (dataParamSize * dataArySize);
    					
        			}else{
        				dataPosition += 4;
        			}
        		}else{
    				System.out.println("oc reportByte : [" + BTUtil.getBytePData(reportByte, 0, reportByte.length) + "]");
        			
            		dataArySize = Integer.parseInt(BTUtil.getBytePData(reportByte, dataPosition, 4));            		
        			if(dataArySize > 0){
        				dataPosition += 4; 
        				for(int k=0;k<rdOutBlockList.size();k++){
        					outblockMap = (HashMap)rdOutBlockList.get(k);	    			
        					for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
        						System.out.println("블럭명비교전 : [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "]");
    	    					if(outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()) != null){
    	    						System.out.println("블럭명L : [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "]");
    	    						System.out.println("블럭값L : [" + outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()) + "]");
    	    						if(bldOutBlockParamList.get(i).get(j).get("type").equals("numstring")){
    	    							BTUtil.ArrayCopyLen(reportByte, dataPosition, BTUtil.lpad(outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString(), Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString())).getBytes(), 0, Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString()));
    	    						}else{
        	    						BTUtil.ArrayCopyLen(reportByte, dataPosition, outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString().getBytes(), 0, outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString().getBytes().length);
    	    						}
    	    					}
    	    					dataPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
    	    				}
    	    			}
        			}else{
        				//초기화
        				for(int m=0;m<tempByte.length;m++) tempByte[m] = 0x00;
        				for(int m=0;m<reportTempByte.length;m++) reportTempByte[m] = 0x00;
        				
        				tempPosition = 0;
        				for(int k=0;k<rdOutBlockList.size();k++){
        					outblockMap = (HashMap)rdOutBlockList.get(k);	    			
        					for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
        						System.out.println("블럭명비교전 : [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "]");
    	    					if(outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()) != null){
    	    						System.out.println("블럭명L : [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "]");
    	    						System.out.println("블럭값L : [" + outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()) + "]");
    	    						if(bldOutBlockParamList.get(i).get(j).get("type").equals("numstring")){
    	    							BTUtil.ArrayCopyLen(tempByte, tempPosition, BTUtil.lpad(outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString(), Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString())).getBytes(), 0, Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString()));
    	    						}else{
    	    							BTUtil.ArrayCopyLen(tempByte, tempPosition, outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString().getBytes(), 0, outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString().getBytes().length);
    	    						}
    	    					}
    	    					tempPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
    	    				}
    	    			}
        				rdDataFullSize += tempPosition;
        				BTUtil.ArrayCopyLen(reportTempByte, 0, reportByte, 0, dataPosition);
        				BTUtil.ArrayCopyLen(reportTempByte, dataPosition, BTUtil.lpad(String.valueOf(rdOutBlockList.size()), 4).getBytes(), 0, 4);
        				dataPosition += 4;
        				BTUtil.ArrayCopyLen(reportTempByte, dataPosition+tempPosition, reportByte, dataPosition, reportByte.length - dataPosition);
        				BTUtil.ArrayCopyLen(reportTempByte, dataPosition, tempByte, 0, tempPosition);
        				dataPosition += tempPosition;
        				
        				BTUtil.ArrayCopyLen(reportTempByte, TrCode.length(), BTUtil.lpad(String.valueOf(rdDataFullSize), 10).getBytes(), 0, 10);

        				reportByte = new byte[TrCode.length()+10+rdDataFullSize];
        				BTUtil.ArrayCopyLen(reportByte, 0, reportTempByte, 0, reportByte.length);        				
        				System.out.println("rdDataFullSize : [" + rdDataFullSize + "]");
        				System.out.println("new reportByte : [" + BTUtil.getBytePData(reportByte, 0, reportByte.length) + "]");
        			}
        		}
			}else{
				outblockMap = (HashMap)bean.get(bldOutBlockName); 
				if(outblockMap == null){
					for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
						dataPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    
					}
				}else{
	    			for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
						System.out.println("블럭명비교전 : [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "]");
						if(outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()) != null){
    						System.out.println("블럭명1 : [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "]");
    						System.out.println("블럭값1 : [" + outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()) + "]");
    						if(bldOutBlockParamList.get(i).get(j).get("type").equals("numstring")){
    							BTUtil.ArrayCopyLen(reportByte, dataPosition, BTUtil.lpad(outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString(), Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString())).getBytes(), 0, Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString()));
    						}else{
        						BTUtil.ArrayCopyLen(reportByte, dataPosition, outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString().getBytes(), 0, outblockMap.get(bldOutBlockParamList.get(i).get(j).get("uppername").toString()).toString().getBytes().length);
    						}
						}
						dataPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
	    			}
				}
    		}
			System.out.println(" 블럭사이즈 : [" + dataPosition + "]");				    		
    	}		
		return new String(reportByte); 
	}

	public static void displayToOutBlockRD(String TrCode, String reportData) throws JDOMException, IOException{
		ArrayList<HashMap> bldOutBlockNameList 	= new ArrayList<HashMap>();
		ArrayList<ArrayList<HashMap>> bldOutBlockParamList  = new ArrayList<ArrayList<HashMap>>();
		xmlOutBlockParse(TrCode, bldOutBlockNameList, bldOutBlockParamList);
		
		byte[] reportByte = reportData.getBytes(); 	
		
		int dataPosition = TrCode.length() + 10;
		int dataArySize = 0;
		
    	for(int i=0; i < bldOutBlockNameList.size(); i++){
    		System.out.println("*********** [" + (String)bldOutBlockNameList.get(i).get("name") + "] *********** ");
    		if(bldOutBlockNameList.get(i).get("repeat").equals("occurs")){
    			dataArySize = Integer.parseInt(BTUtil.getBytePData(reportByte, dataPosition, 4));	
    			System.out.println("[INFO] Type: occurs , Count: " + dataArySize);  
    			dataPosition += 4;
    			for(int k=0;k<dataArySize;k++){
    				System.out.println("[INFO] ------------ Array[: " + k + "] ------------ ");
    				for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
    					System.out.println("[DATA] [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "][" + BTUtil.getBytePData(reportByte, dataPosition, Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString())) + "]");  
    					dataPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
					}
    			}
    		}else{
    			System.out.println("[INFO] Type: 1");  
				for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
					System.out.println("[DATA] [" + bldOutBlockParamList.get(i).get(j).get("uppername").toString() + "][" + BTUtil.getBytePData(reportByte, dataPosition, Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString())) + "]");  
					dataPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
				}
    		}
    	}		
	}	

	public static ArrayList getRDParamToValue(String TrCode, String reportData, String paramName) throws JDOMException, IOException{
		ArrayList<HashMap> bldOutBlockNameList 	= new ArrayList<HashMap>();
		ArrayList<ArrayList<HashMap>> bldOutBlockParamList  = new ArrayList<ArrayList<HashMap>>();
		xmlOutBlockParse(TrCode, bldOutBlockNameList, bldOutBlockParamList);
		ArrayList<String> resultList = new ArrayList<String>();
		
		byte[] reportByte = reportData.getBytes(); 	
		
		int dataPosition = TrCode.length() + 10;
		int dataArySize = 0;
		
    	for(int i=0; i < bldOutBlockNameList.size(); i++){
    		if(bldOutBlockNameList.get(i).get("repeat").equals("occurs")){
    			dataArySize = Integer.parseInt(BTUtil.getBytePData(reportByte, dataPosition, 4));	
    			dataPosition += 4;
    			for(int k=0;k<dataArySize;k++){
    				for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
    					if(bldOutBlockParamList.get(i).get(j).get("uppername").toString().equals(paramName)||bldOutBlockParamList.get(i).get(j).get("name").toString().equals(paramName)){
    						resultList.add(BTUtil.getBytePData(reportByte, dataPosition, Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString())));
    					}
    					dataPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
					}
    			}
    		}else{
				for(int j=0;j<bldOutBlockParamList.get(i).size();j++){
					if(bldOutBlockParamList.get(i).get(j).get("uppername").toString().equals(paramName)||bldOutBlockParamList.get(i).get(j).get("name").toString().equals(paramName)){
						resultList.add(BTUtil.getBytePData(reportByte, dataPosition, Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString())));
					}
					dataPosition += Integer.parseInt(bldOutBlockParamList.get(i).get(j).get("size").toString());	    					
				}
    		}
    	}
    	return resultList;
	}	

}
