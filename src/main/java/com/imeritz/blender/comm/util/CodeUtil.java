package com.imeritz.blender.comm.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdom2.JDOMException;

import com.imeritz.blender.tran.TranUtil;

public class CodeUtil {
	/**
	 * 코드값조회 obcpp_code_034r
	 * @author 황남수
	 * @param  String 코드도메인영문명, String 코드값, String 사용자ID
	 *         ex) "ISSU_DETL_DVCD", "01", "ulmbada0"
	 * @return String 코드명
	 * @since  2012.04.13
	 * @version 1.0 
	 */	
	/* 사용예 Controller 에서 사용
	 * 
	System.out.println("returnMap1############>>"+returnMap.toString());
	ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
	
	codeName = codeUtil.getCodeName("ISSU_DETL_DVCD", rs.get(0).get(0).get("IssuDetlDvcd").toString(), loginInfo.getUserId());		
	rs.get(0).get(0).put("IssuDetlDvcdName", codeName);
	
	returnMap.remove("resultList");
	returnMap.put("resultList", rs);			

	System.out.println("returnMap2############>>"+returnMap.toString());
	 */
	public String getCodeName(String termPhscName, String codeVal, String userId){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();

		System.out.println("코드조회 INPUT >>> "+ termPhscName + ", " + codeVal);
		//TR를 호출한다. 파라메터 : 입력Bean, 호출TR명
		try {
			bean.put("header", "S");
			bean.put("UserId", userId);
			
			bean.put("CodeVal", codeVal);
			bean.put("MetaTermPhscName", termPhscName);	
			
			returnMap = TranUtil.sendToGateWay(bean, "obcpp_code_034r" ,header);
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		if( rs.get(0).size() > 0){

			System.out.println("CodeValName %%%%%%%% " + rs.get(0).get(0).get("CodeValName").toString());
			return rs.get(0).get(0).get("CodeValName").toString();
		}else{
			System.out.println("코드조회실패");
			return "";
		}
		
	}	
	
	
	
	/**
	 * 코드 값 모두 조회 obcpp_code_002r
	 * @author 김재성
	 * @param  String 메타용어물리명 (한글 코드명) , String 사용자ID
	 *         ex) "국가코드", "ulmbada0"
	 * @return ArrayList<HashMap>(코드,코드값,코드유효값설명)
	 * @since  2012.04.13
	 * @version 1.0 
	 */	
	/* 사용예 Controller 에서 사용
	 * 
	System.out.println("returnMap1############>>"+returnMap.toString());
	ArrayList<HashMap> codeMap = codeUtil.getCodeList("국가코드", loginInfo.getUserId());
	
	 */
	public ArrayList<HashMap> getCodeList(String codeKorName, String userId){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();
		HashMap resultNext = new HashMap();
		HashMap next = new HashMap();

		//TR를 호출한다. 파라메터 : 입력Bean, 호출TR명
		try {
			bean.put("header", "S");
			bean.put("UserId", userId);
			
			bean.put("IqryClsCode", "1");
			bean.put("MetaTermPhscName", codeKorName);
			bean.put("CodeValdValKorName","");
			bean.put("CodeValdVal","");
			
			returnMap = TranUtil.sendToGateWay(bean, "obcpp_code_002r" ,header);
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		ArrayList<HashMap> rs2 = rs.get(0);
		
		//nextblock이 있을경우 없을까지 조회
		while(returnMap.get("sysNextYn").equals("Y"))
		{
			try {
				resultNext = (HashMap)returnMap.get("resultNext");
				next.put("CodeValdValSortSeq", resultNext.get("CodeValdValSortSeq"));
				next.put("CodeValdVal", resultNext.get("CodeValdVal"));
				header.put("NextYn", "Y");
				header.put("NextBlockData", next);
				
				returnMap = TranUtil.sendToGateWay(bean, "obcpp_code_002r" ,header);
				
				//새로 조회해온 resultList를 다시 rs에 넣어줌
				rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
				
				//새로 조회해온 resultList의 코드값을 기존 rs2에다 추가
				for(int i=0; i<rs.get(0).size(); i++)
				{
					rs2.add(rs.get(0).get(i));
				}
				
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if( rs2.size() > 0 ){
			return rs2;
		}else{
			System.out.println("##################################코드조회실패########################################");
			return new ArrayList<HashMap>();
		}
	}	
	
	
	/**
	 * 값을 List로 받아 왔는데 각 값마다 코드값을 찾아줘야할때
	 * 코드의 내용을 다 가져와서 비교해서 대입하는 메소드
	 * @author 김재성
	 * @param  String 메타용어물리명 (한글 코드명) , String 영문 코드명 
	 * 				, HashMap returnMap , String 아웃블럭 번호 ,  String 사용자ID
	 *         ex) "집합투자증권펀드유형코드" , "IiscFundTypeCode" , returnMap , "1,2" , loginId
	 *         아웃블럭 번호는 tr과 동일하게 써준다 (1부터시작)
	 * @return HashMap
	 * @since  2012.04.13
	 * @version 1.0 
	 */	
	/* 사용예 Controller 에서 사용
	 * 
		bean.put("UserId", loginInfo.getUserId());
		bean.put("header", "S");
			
		returnMap = this.rcmtfnprsvc.getRcmtFundSrch(bean);
		codeUtil.getChangeCodeList("집합투자증권펀드유형코드","IiscFundTypeCode",returnMap,"1",loginInfo.getUserId());	

		System.out.println("returnMap2############>>"+returnMap.toString());
	 */
	public void getChangeCodeList(String codeKorName, String codeEngName 
			, HashMap returnMap, String outNum , String userId){
		ArrayList<HashMap> rs = getCodeList(codeKorName,userId);
		String[] outNums = outNum.split(",");
		int[] num = new int[outNums.length];
		
		for(int i=0; i<outNums.length; i++)
		{
			num[i] = Integer.parseInt(outNums[i])-1;
		}

		ArrayList<ArrayList<HashMap>> resultList = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		ArrayList<HashMap> list = null;
		HashMap bean = null;
		for(int i=0; i<num.length; i++)
		{
			list = (ArrayList<HashMap>)resultList.get(num[i]);
			for(int j=0; j<list.size(); j++)
			{
				bean = list.get(j);
				for(int k=0; k<rs.size(); k++)
				{
					if(bean.get(codeEngName) !=null && bean.get(codeEngName).equals(((HashMap)rs.get(k)).get("CodeValdVal")))
					{
						bean.put(codeEngName,((HashMap)rs.get(k)).get("CodeValdValKorName"));
						break;
					}
				}
			}
		}
	}
	
	/** getChangeCodeList 메소드와 동일하나, 기존 코드컬럼은 그대로 두고 코드값으로 리턴할 컬럼을 사용자가 추가할 수 있음
	 * 값을 List로 받아 왔는데 각 값마다 코드값을 찾아줘야할때
	 * 코드의 내용을 다 가져와서 비교해서 대입하는 메소드
	 * @author 황남수
	 * @param  String 메타용어물리명 (한글 코드명) , String 영문 코드명, String returnMap 에 추가할 한글코드명 컬럼명
	 * 				, HashMap returnMap , String 아웃블럭 번호 ,  String 사용자ID
	 *         ex) "집합투자증권펀드유형코드" , "IiscFundTypeCode" , "IiscFundTypeName" ,  returnMap , "1,2" , loginId
	 *         아웃블럭 번호는 tr과 동일하게 써준다 (1부터시작)
	 * @return HashMap
	 * @since  2012.04.13
	 * @version 1.0 
	 */	
	public void getChangeCodeListAddCol(String codeKorName, String codeEngName, String codeName 
			, HashMap returnMap, String outNum , String userId){
		ArrayList<HashMap> rs = getCodeList(codeKorName,userId);
		String[] outNums = outNum.split(",");
		int[] num = new int[outNums.length];
		
		for(int i=0; i<outNums.length; i++)
		{
			num[i] = Integer.parseInt(outNums[i])-1;
		}

		ArrayList<ArrayList<HashMap>> resultList = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		ArrayList<HashMap> list = null;
		HashMap bean = null;
		for(int i=0; i<num.length; i++)
		{
			list = (ArrayList<HashMap>)resultList.get(num[i]);
			for(int j=0; j<list.size(); j++)
			{
				bean = list.get(j);
				for(int k=0; k<rs.size(); k++)
				{
					if(bean.get(codeEngName) !=null && bean.get(codeEngName).equals(((HashMap)rs.get(k)).get("CodeValdVal")))
					{
						bean.put(codeName,((HashMap)rs.get(k)).get("CodeValdValKorName"));
						break;
					}
				}
			}
		}
	}
	

	/**
	 * 코드값조회 펀드코드->협회펀드코드 obsim_idmn_018r
	 * @author 황남수
	 * @param  String 펀드코드
	 * @return String 협회펀드코드
	 * @since  2012.04.17
	 * @version 1.0 
	 */	
	public String getSctyStndCodeFromIscd(String iscd, String userId){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();

		System.out.println("코드조회 INPUT >>> "+ iscd );
		//TR를 호출한다. 파라메터 : 입력Bean, 호출TR명
		try {

			bean.put("Iscd",  iscd);
			bean.put("ProsClsCode",  "");
			
			bean.put("header", "S");
			bean.put("UserId", userId);
			
			returnMap = TranUtil.sendToGateWay(bean, "obsim_idmn_018r" ,header);
	
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		if( rs.get(0).size() > 0){
			System.out.println("SctyStndCode %%%%%%%% " + rs.get(1).get(0).get("SctyStndCode").toString());
			return rs.get(1).get(0).get("SctyStndCode").toString();
		}else{		
			System.out.println("코드조회실패");
			return "";	
		}
	}	
	/**
	 * 코드값조회 펀드코드->협회펀드코드 obsim_idmn_018r
	 * @author 이혁규
	 * @param  String 펀드코드
	 * @return String 협회펀드코드
	 * @since  2012.04.17
	 * @version 1.0 
	 */	
	public String getSctyStndCodeFromIscdGradCode(String iscd, String userId){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();

		System.out.println("코드조회 INPUT >>> "+ iscd );
		//TR를 호출한다. 파라메터 : 입력Bean, 호출TR명
		try {

			bean.put("Iscd",  iscd);
			bean.put("ProsClsCode",  "");
			
			bean.put("header", "S");
			bean.put("UserId", userId);
			
			returnMap = TranUtil.sendToGateWay(bean, "obsim_idmn_018r" ,header);
	
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		if( rs.get(0).size() > 0){
			System.out.println("SctyStndCode %%%%%%%% " + rs.get(1).get(0).get("SctyStndCode").toString());
			return rs.get(1).get(0).get("DvpdGradCode").toString();
		}else{		
			System.out.println("코드조회실패");
			return "";	
		}
	}	
	
	
	/**
	 * 코드값조회 협회펀드코드->펀드코드 obsim_idmn_017r
	 * @author 황남수
	 * @param  String 협회펀드코드
	 * @return String 펀드코드
	 * @since  2012.04.17
	 * @version 1.0 
	 */	
	public String getIscdFromSctyStndCode(String sctyStndCode, String userId){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();

		System.out.println("코드조회 INPUT >>> "+ sctyStndCode );
		//TR를 호출한다. 파라메터 : 입력Bean, 호출TR명
		try {

			bean.put("SctyStndCode",  sctyStndCode);
			
			bean.put("header", "S");
			bean.put("UserId", userId);
			
			returnMap = TranUtil.sendToGateWay(bean, "obsim_idmn_017r" ,header);
	
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		if( rs.get(0).size() > 0){
			System.out.println("Iscd %%%%%%%% " + rs.get(0).get(0).get("Iscd").toString());
			return rs.get(0).get(0).get("Iscd").toString();
		}else{		
			System.out.println("코드조회실패");
			return "";	
		}
	}	
	
	/**
	 * 코드값조회 협회펀드코드->펀드풀네임 obsim_idmn_017r
	 * @author 황남수
	 * @param  String 협회펀드코드
	 * @return String 펀드코드
	 * @since  2012.04.17
	 * @version 1.0 
	 */	
	public String getIscdFromSctyStndName(String sctyStndCode, String userId){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();

		try {

			bean.put("SctyStndCode",  sctyStndCode);
			
			bean.put("header", "S");
			bean.put("UserId", userId);
			
			returnMap = TranUtil.sendToGateWay(bean, "obsim_idmn_017r" ,header);
	
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		if( rs.get(0).size() > 0){
			return rs.get(0).get(0).get("KorIsnm").toString();
		}else{		
			return "";	
		}
	}		
	
	/**
	 * 펀드저축유형조회 obsim_idmn_018r
	 * @author 황남수
	 * @param  String 펀드코드
	 * @return String 협회펀드코드
	 * @since  2012.04.17
	 * @version 1.0 
	 */	
	public String getIiscSvngTypeCode(String iscd, String userId){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();

		try {

			bean.put("Iscd",  iscd);
			bean.put("ProsClsCode",  "");
			
			bean.put("header", "S");
			bean.put("UserId", userId);
			
			returnMap = TranUtil.sendToGateWay(bean, "obsim_idmn_018r" ,header);
	
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String vRtn = "";
		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
		if( rs.get(9).size() > 0){
			ArrayList aList = (ArrayList) rs.get(9);
			for(int i=0; i < aList.size(); i++) {
				if ( rs.get(9).get(i).get("SplmPaymAbleYn").toString().equals("Y") ) {
					vRtn += rs.get(9).get(i).get("IiscSvngTypeCode").toString() + "|";
				}
			}
			System.out.println(vRtn);
//			return vRtn;
		}else{		
			vRtn = "";
//			return "";	
		}
		
		if( rs.get(4).size() > 0){         
			ArrayList aList2 = (ArrayList) rs.get(4);
			vRtn += "$@$" +  rs.get(4).get(0).get("VntrEtprIvtrYn").toString();
			System.out.println("[getIiscSvngVentureYN] VentureYN:" + vRtn);
		}else{		
			vRtn += "$@$" + "N";	
//			return "N";	
		}
		
		return vRtn;
	}
	
//	/**
//	 * 펀드저축유형조회 obsim_idmn_018r
//	 * @author 이재화
//	 * @param  String 펀드코드
//	 * @return String 벤처펀드 유무
//	 * @since  2018.04.23
//	 * @version 1.0 
//	 */	
//	public String getIiscSvngVentureYN(String iscd, String userId){
//		HashMap bean = new HashMap();
//		HashMap header = new HashMap();
//		HashMap returnMap = new HashMap();
//		
//		try {
//			
//			bean.put("Iscd",  iscd);
//			bean.put("ProsClsCode",  "");
//			
//			bean.put("header", "S");
//			bean.put("UserId", userId);
//			
//			returnMap = TranUtil.sendToGateWay(bean, "obsim_idmn_018r" ,header);
//			
//		} catch (JDOMException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		String vRtn = "";
//		ArrayList<ArrayList<HashMap>> rs = (ArrayList<ArrayList<HashMap>>) returnMap.get("resultList");
//		if( rs.get(4).size() > 0){
//			ArrayList aList = (ArrayList) rs.get(4);
//			for(int i=0; i < aList.size(); i++) {
//				vRtn += rs.get(4).get(i).get("vntrEtprIvtrYn").toString();
//			}
//			System.out.println("[getIiscSvngVentureYN] VentureYN:" + vRtn);
//			return vRtn;
//		}else{		
//			return "N";	
//		}
//	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
