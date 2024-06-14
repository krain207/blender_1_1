package com.imeritz.blender.tran;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FidUtil {
	protected static Logger logger = LoggerFactory.getLogger(FidUtil.class);
	//FID 통신모듈 send ~ receive
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static HashMap sendToGateWayFID(ArrayList<HashMap>fidArrayInfoList, Object objBean, String bldname, HashMap inHeader) throws JDOMException, IOException{
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
		
		byte[] bFid = new byte[4096];		
		byte[] sendByte = null;
		int iposition = 0;
		
		//결과를 저장하기 위한 자료구조들...
		HashMap outMap = new HashMap();
		ArrayList<ArrayList<HashMap>> outList = new ArrayList<ArrayList<HashMap>>();
		
		//xml정보 파싱
		xmlInfoParse(bldname, xmlInfoMap, bldInBlockNameList, bldOutBlockNameList, bldNextBlockNameList,
					                      bldInBlockParamList, bldOutBlockParamList, bldNextBlockParamList);		
		logger.info("<<-------------------------[sendToGateWayFID]--------------------------->>");		
		String strByte = "";
		try {
			HashMap inBlockInfo = null;
			HashMap outBlockInfo = null;
			ArrayList inParamArray = null;
			HashMap inParamMap = null;
			if(bldInBlockNameList.size() > 1){			
				for(int i=0;i<bldInBlockNameList.size();i++){
					inBlockInfo = bldInBlockNameList.get(i);
					ArrayList<HashMap> aryInParamList = bldInBlockParamList.get(i);
					if("occurs".equals(inBlockInfo.get("repeat"))){					
						inParamArray = (ArrayList)((ArrayList)objBean).get(i);						
						iposition = xmlFidParserInBlock(bFid, iposition, TranConstant.INBLOCK_TYPE_LIST, inBlockInfo, aryInParamList, inParamArray);
					}else{
						inParamMap = (HashMap)((ArrayList)objBean).get(i);
						iposition = xmlFidParserInBlock(bFid, iposition, TranConstant.INBLOCK_TYPE_MAP, inBlockInfo, aryInParamList, inParamMap);
					}
					outBlockInfo = bldOutBlockNameList.get(i);
					ArrayList<HashMap> aryOutParamList = bldOutBlockParamList.get(i);
	    			iposition = xmlFidParserOutBlock(bFid, iposition, outBlockInfo, aryOutParamList, fidArrayInfoList.get(i));
				}
			}else{
				inBlockInfo = bldInBlockNameList.get(0);
				ArrayList<HashMap> aryInParamList = bldInBlockParamList.get(0);				
				if("occurs".equals(inBlockInfo.get("repeat"))){					
					inParamArray = (ArrayList)objBean;	
					iposition = xmlFidParserInBlock(bFid, iposition, TranConstant.INBLOCK_TYPE_LIST, inBlockInfo, aryInParamList, inParamArray);
				}else{
					inParamMap = (HashMap)objBean;
					iposition = xmlFidParserInBlock(bFid, iposition, TranConstant.INBLOCK_TYPE_MAP, inBlockInfo, aryInParamList, inParamMap);
				}
				outBlockInfo = bldOutBlockNameList.get(0);
				ArrayList<HashMap> aryOutParamList = bldOutBlockParamList.get(0);
				iposition = xmlFidParserOutBlock(bFid, iposition, outBlockInfo, aryOutParamList, fidArrayInfoList.get(0));
			}
			sendByte = BTUtil.getByte(bFid, 0, iposition);			  
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}		
		HashMap outBlockInfo = null;
		
		if(inHeader == null){
			inHeader = new HashMap();
		}
		inHeader.put("FID", "Y");
		try {
	    	//통신 요청
			Hashtable rcvMap = null;
	    	BldTRCall wstr = new BldTRCall();
	    	
	    	String [] outblocks = new String[bldOutBlockNameList.size()];
	    	for(int i=0;i<outblocks.length;i++){
	    		outblocks[i] = (String)bldOutBlockNameList.get(i).get("name");
	    	}
	    	rcvMap = wstr.callTR(bldname, outblocks, sendByte, inHeader);
	    	//데이터 파싱
	    	if(rcvMap.get("error").equals("0")){
	    		if(!rcvMap.get("msg").equals("error")){
    				BTUtiltr.convFID2List(rcvMap, outList, bldOutBlockNameList, bldOutBlockParamList);				    				
		    	}
	    	}	    		    	
//	    	System.out.println(" MESSAGE :: [ERROR] [" + (String)rcvMap.get("error") + "]");
//	    	System.out.println(" MESSAGE :: [MSG] [" + (String)rcvMap.get("msg") + "]");
//	    	System.out.println(" MESSAGE :: [SUBMSG] [" + (String)rcvMap.get("submsg") + "]");
//	    	System.out.println(" MESSAGE :: [MSGCODE] [" + (String)rcvMap.get("msgcode") + "]");
	    	
			outMap.put("sysMsg", (String)rcvMap.get("msg"));
			outMap.put("sysSubMsg", (String)rcvMap.get("submsg"));
			outMap.put("sysCode", (String)rcvMap.get("error"));
			outMap.put("sysMsgCode", (String)rcvMap.get("msgcode"));
	    	
//			메시지만 저장된 outMap에 실자료의 리스트를 저장한다.
			outMap.put("resultList", outList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String outputString = outMap.toString().replace("[", "[\n");
		outputString = outputString.replace("]", "\n]");
		outputString = outputString.replace("},", "},\n");
				
//		System.out.println(outputString);
		
		return outMap;
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int xmlFidParserInBlock(byte[] bFid, int iposition, int inBlockType, HashMap inBlockInfo, ArrayList<HashMap> aryInParamList, Object inParam) throws UnsupportedEncodingException{
		HashMap inParamMap = null;
		ArrayList<HashMap> inParamArray = null;
		byte[] tmpByte = null;
		bFid[iposition++] = TranConstant.FD_GS;
		
		
		switch(inBlockType){
		case TranConstant.INBLOCK_TYPE_LIST : //블럭에 다중레코드
			inParamArray = (ArrayList)inParam;	
			for(int i=0;i<aryInParamList.size();i++){
    			tmpByte = BTUtil.lpad(aryInParamList.get(i).get("fid").toString(), 4).getBytes();
    			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
    			iposition += tmpByte.length;    		
    			bFid[iposition++] = TranConstant.FD_IS;
    			for(int j=0;j<inParamArray.size();j++ ){
	    			tmpByte = inParamArray.get(j).get(aryInParamList.get(i).get("name")).toString().getBytes();
	    			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
	    			iposition += tmpByte.length;    		
	    			bFid[iposition++] = TranConstant.FD_IS;
				}
    			bFid[iposition-1] = TranConstant.FD_FS; //마지막 FD_IS 을 덮어씌운다.
			}
			break;
		case TranConstant.INBLOCK_TYPE_MAP : //블럭에 단일레코드
			inParamMap = (HashMap)inParam;
			for(int j=0;j<aryInParamList.size();j++){
    			tmpByte = BTUtil.lpad(aryInParamList.get(j).get("fid").toString(), 4).getBytes();
    			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
    			iposition += tmpByte.length;    		
    			bFid[iposition++] = TranConstant.FD_IS;
    			tmpByte = inParamMap.get(aryInParamList.get(j).get("name")).toString().getBytes();
    			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
    			iposition += tmpByte.length;    		
    			bFid[iposition++] = TranConstant.FD_FS;
			}
			break;
		}
		//resultByte = BTUtil.getByte(bFid, 0, iposition); 
		return iposition;
	}
	
	private static int xmlFidParserOutBlock(byte[] bFid, int iposition, HashMap outBlockInfo, ArrayList<HashMap> aryOutParamList, HashMap fidArrayInfo) throws UnsupportedEncodingException{
		byte[] tmpByte = null;
		int fidSize = 0;
		int fidLenPosition = 0;
		String rowCount = TranConstant.DEFAULT_ROW_COUNT;
		byte actionKey = TranConstant.ACTION_KEY_FIRST;
		byte[] saveBufLen = TranConstant.SAVE_BUFFER_LEN_DEFAULT;
		byte status = TranConstant.FID_STATUS_DEFAULT;
		byte[] saveBuf = null;
		
		if(!"".equals(fidArrayInfo.get("rowCount").toString().trim())){
			rowCount = fidArrayInfo.get("rowCount").toString().trim();
		}
		if(!"".equals(fidArrayInfo.get("actionKey").toString().trim())){
			actionKey = fidArrayInfo.get("actionKey").toString().trim().getBytes()[0];
			
			switch(actionKey){
			case TranConstant.ACTION_KEY_FIRST:
				status = TranConstant.FID_STATUS_DEFAULT;
				break;
			case TranConstant.ACTION_KEY_PRIOR:
				status = TranConstant.FID_STATUS_PRIOR;
				
				break;
			case TranConstant.ACTION_KEY_NEXT:
				status = TranConstant.FID_STATUS_NEXT;				
				break;
			default :
				//public static final byte FID_STATUS_OR = 0x01|0x02; //이전/다음 동시에 존재하는 경우	
				status = TranConstant.FID_STATUS_DEFAULT;
			}
		}
		if(!"".equals(fidArrayInfo.get("saveBufLen").toString().trim())){
			saveBufLen = fidArrayInfo.get("saveBufLen").toString().getBytes();
		}
		if(!"".equals(fidArrayInfo.get("saveBuf").toString().trim())){
			saveBuf = fidArrayInfo.get("saveBuf").toString().getBytes();			
		}
		
		tmpByte = "GID".getBytes();
		bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
		iposition += tmpByte.length;    		
		bFid[iposition++] = TranConstant.FD_IS;
		tmpByte = BTUtil.rpad(outBlockInfo.get("gid").toString(), 4).getBytes();
		bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
		iposition += tmpByte.length;    		
		bFid[iposition++] = TranConstant.FD_FS;
		//----------- print point -------------------------------------------
		if(outBlockInfo.get("repeat").equals("occurs")){
			//배열정보 셋팅
			bFid[iposition++] = TranConstant.FID_ARRAY_CODE;
			fidSize = iposition;
			tmpByte = "        ".getBytes();
			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
			fidLenPosition = iposition;
			iposition += tmpByte.length;    
			tmpByte = BTUtil.lpad(rowCount, 4).getBytes();
			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
			iposition += tmpByte.length;    		
			bFid[iposition++] = actionKey;				
			bFid[iposition++] = status;				
			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, saveBufLen, 0, saveBufLen.length);
			iposition += saveBufLen.length;    
			
			if(saveBuf != null){
				bFid = BTUtil.ArrayCopyRtn(bFid, iposition, saveBuf, 0, saveBuf.length);
				iposition += saveBuf.length;    
			}			
			bFid[iposition++] = TranConstant.FD_FS;
		}			
		for(int i=0;i<aryOutParamList.size();i++){
			bFid[iposition++] = '*';
//			if(aryOutParamList.get(i).get("type").equals("numstring")){
//				bFid[iposition++] = '*';					
//			}
			tmpByte = BTUtil.lpad(aryOutParamList.get(i).get("fid").toString(), 4).getBytes();
			bFid = BTUtil.ArrayCopyRtn(bFid, iposition, tmpByte, 0, tmpByte.length);
			iposition += tmpByte.length;    		
			bFid[iposition++] = TranConstant.FD_FS;
		}
		bFid[iposition-1] = TranConstant.FD_GS;

		if(outBlockInfo.get("repeat").equals("occurs")){		
			tmpByte = BTUtil.lpad(String.valueOf(iposition), 8).getBytes();
			bFid = BTUtil.ArrayCopyRtn(bFid, fidLenPosition, tmpByte, 0, tmpByte.length);
		}		
		// 20230313 System.out.println(" OUT 파라메터 바이트 :: [" + BTUtil.getBytePData(bFid, 0, iposition)  + "]");
		//BTUtil.getByte(bFid, 0, iposition)
		return iposition;
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
				blockInfoMap.put("gid", eblock.getAttributeValue("gid"));	
				
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
					fieldInfoMap.put("fid", e.getAttributeValue("fid"));
					//fieldInfoMap.put("gid", e.getAttributeValue("gid"));
					
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
				blockInfoMap.put("gid", eblock.getAttributeValue("gid"));
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
					fieldInfoMap.put("fid", e.getAttributeValue("fid"));
					//fieldInfoMap.put("gid", e.getAttributeValue("gid"));
					
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
				blockInfoMap.put("gid", eblock.getAttributeValue("gid"));
				
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
					fieldInfoMap.put("fid", e.getAttributeValue("fid"));
					//fieldInfoMap.put("gid", e.getAttributeValue("gid"));
					
					nextParamList.add(fieldInfoMap);				//OUT 블록안 파라메터 정보(대문자) 	 
				}
				bldNextBlockParamList.add(nextParamList);
			}			
		}		
	}
}
