package com.imeritz.blender.tran;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
*/
import java.util.Base64.Encoder;
import java.util.Base64;
import java.util.Base64.Decoder;
public class BTUtil {

	/**
	 * 오른쪽 공백 채움
	 * @param rpad("ASDF", "10")
	 * @param String s, int num
	 * @return "ASDF      "
	 */
    public static String rpad(String s, int num) {
    	StringBuffer buf = new StringBuffer(s);
    	
    	for (int i=getByte(s); i<num; i++) {
    		buf.append(" ");
    	}
    	
    	return buf.toString();
    }
	/**
	 * 왼쪽 0채움
	 * @param lpad("ASDF", "10")
	 * @param String s, int num
	 * @return "000000ASDF"
	 */
    public static String lpad(String s, int num) {
    	StringBuffer buf = new StringBuffer("");
    	
    	for (int i=getByte(s); i<num; i++) {
    		buf.append("0");
    	}
    	buf.append(s);
    	
    	return buf.toString();
    }
    
	/**
	 * BYTE 짤라서 값을 리턴 String
	 * @param getBytePData([Byte Data], 0, 10)
	 * @return "String으로 변환해서 보내준다."
	 */
	public static String getBytePData(byte[] msg, int startPos,int len) throws UnsupportedEncodingException{
		byte[] msgBuffer = msg;
		byte[] pS = new byte[len];

		int insertI = 0;
		String endS;
		for(int j = startPos; j < (startPos+len) ; j++ ){
			try{
				pS[insertI++] = (byte)msgBuffer[j];
			}catch(Exception e){
				e.printStackTrace();
				break;
			}
		}
		endS = new String(pS, "MS949"); //new String(pS);
		return endS;
	}
	
	/**
	 * BYTE 짤라서 값을 리턴 BYTE
	 * @param getByte([Byte Data], 0, 10)
	 * @return "30,30,30,30,30....."
	 */
	public static byte [] getByte(byte[] msg, int startPos,int len) throws UnsupportedEncodingException{
		byte[] msgBuffer = msg;
		byte[] pS = new byte[len];

		int insertI = 0;
		String endS;
		for(int j = startPos; j < (startPos+len) ; j++ ){
			try{
				pS[insertI++] = (byte)msgBuffer[j];
			}catch(Exception e){
				break;
			}
		}
		return pS;
	}

	public static int ArrayCopyLen(byte[] target, int tpos, byte[] src, int spos, int len) {
		for(int	i =	0; i < len;	i++) {
			target[tpos++]	= src[spos++];
		}return len;
	}

	public static int ArrayCopy(byte[] target, int tpos, byte[] src, int spos, int len) {
		for(int	i =	spos; i < len;	i++) {
			target[tpos++]	= src[i];
		}
		return tpos;
	}

	public static byte[] ArrayCopyRtn(byte[] target, int tpos, byte[] src, int spos, int len) {
//		try {
//			System.out.println(spos + "," + len + "," + getBytePData(src, 0, src.length));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		for(int	i =	spos; i < len;	i++) {
			target[tpos++]	= src[i];
		}
		return target;
	}

    public static synchronized int getByte(String str) {
        int tLen = str.length();
        int count = 0;  
        char c;
        int s=0;
        for(s=0;s<tLen;s++){
            c = str.charAt(s);
            if(c>127) count +=2;
            else count ++;
        }     
        return count;
    } 
    
	/** 
	 * 소수점이하를 버림
	 * 
	 * 예) in  : NumberUtil.floorString("4597.673")
	 *     out : 4597
	 *     
	 *     in  : NumberUtil.floorString("4,597.673")
	 *     out : 4,597
	 *
	 * 메소드 : floorString
	 * @param num
	 * @return
	 * */
	public static String floorString(String num) {
		num = String.valueOf(num.replace("+", ""));
		if(num.indexOf(".")>0) {
			num = num.substring(0, num.indexOf("."));
		}
		return num;
	}
	
	public static String[][] getArrayPData(byte[] sVal, int rowLen, int[] colLen) {
		try{
			int offset = 0;
			int col_offset = 0;
			String tmp = "";   
			boolean isOver = false;

			byte[] bData = sVal;
			int max = 0;
			if(bData.length >0){
				max = ((bData.length/rowLen)==0)?1:(bData.length/rowLen);
			}			
			if( bData.length > (rowLen*max)){isOver=true; max=1;}
			String[][]  rPVal = new String[max][colLen.length];
			for(int i=0 ;i <max;i++){
				for(int j=0; j<colLen.length; j++){
					if(bData.length < (offset+col_offset+colLen[j])){
						rPVal[i][j] = "";
					}else{
//						rPVal[i][j] =  new String(bData, offset+col_offset, colLen[j]);
						if(isOver){
							rPVal[i][j]  =  new String(bData, offset+col_offset, bData.length, "MS949"); 
						}else{
							rPVal[i][j] =  new String(bData, offset+col_offset, colLen[j], "MS949");
						}
						if(rPVal[i][j].trim().equals("")){
							rPVal[i][j] = "";
						}
					}
					col_offset += colLen[j];
				}
			}
			return rPVal;
		}catch (Exception e){
			System.out.print("========================"+e);
			return null;
		}
	}
	
	/**
	 *  FID 파싱
	 **/
	public static String[][] getArrayPDataFid(byte[] sVal, int rowLen, int[] colLen, String[] colType,int rowCount){
		try{
			byte[] colData = null;
			byte[] tempData = null;
			byte[] bData = sVal;
			int max = rowCount;
			boolean rowFlag = false;
			boolean endFlag = false;
			String[][]  rPVal = new String[rowCount][colLen.length];

			int chkByte = 0;
			int	lowCnt = 0;
				while(chkByte < bData.length && !endFlag){
					for(int j=0; j<colLen.length; j++){
						rowFlag = false;
						colData = new byte [256];
						int dataLength = 0;
						while(true){
							chkByte++;
							if((byte)0x1E == (byte)bData[chkByte] || (byte)0x1D == (byte)bData[chkByte] || (byte)0x1F == (byte)bData[chkByte]){
								tempData = BTUtil.getByte(colData, 0, dataLength);
								if((byte)0x1D == (byte)bData[chkByte]){
									rowFlag = true;
								}								
								if((byte)0x1F == (byte)bData[chkByte]){
									rowFlag = true;
									endFlag = true;
								}								
								break;
							}else{
								colData[dataLength++] = bData[chkByte];
							}
						}
						rPVal[lowCnt][j] =  new String(tempData, "MS949");
						if(rPVal[lowCnt][j].trim().equals("")){
							rPVal[lowCnt][j] = "";
						}						
						if(endFlag){
							lowCnt++;
							break;
						};						
						if(rowFlag){
							lowCnt++;
						};
					}
				}
			return rPVal;
			
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] getAttrValue(byte[] msg, int startPos,int len){
		byte[] msgBuffer = msg;
		byte[] pS = new byte[len];

		int insertI = 0;
		String endS;
		for(int j = startPos; j < (startPos+len) ; j++ ){
			try{
				pS[insertI++] = msgBuffer[j];
			}catch(Exception e){
				break;
			}
		}
		return pS;
	}

	/** 
	 * numstring 형 자료변경
	 * 숫자타입의 자료형중에 불필요한 왼쪽 0 을 삭제하고 부호+데이터로 변경한다.  
	 * 예) in  : BTUtil.numberSignProc("-000000001234")
	 *     out : -1234
	 *     
	 *     in  : BTUtil.numberSignProc("000000001234")
	 *     out : 1234
	 * 
	 * 작성자   : 남웅주 2012.01.10
	 * 메소드   : numberSignProc
	 * @param numstring 형 자료
	 * @return 변경된 자료
	 * */	
	public static String numberSignProc(String originData){
		String signFlag = originData.substring(0, 1);
		String numData  = originData.substring(1, originData.length());
		int i = 0;
		
		while(i<numData.length()){
			if(!numData.substring(i, i+1).equals("0")){
				break;
			}
			i++;
		}
		if(numData.equals("")){
			numData = "0";
		}else{
			numData = numData.substring(i, numData.length());	    						
		}
		if(signFlag.equals("-")){
			numData = signFlag + numData;
		}
		return numData;
	}
	
	/** 
	 * 바이트 배열에서 특정문자열의 위치를 찾는다.
	 * 검색중에 블럭의 끝을 만나면 종료한다.
	 * 예)     : BTUtil.ArrayByteFind(바이트배열, 바이트문자, 검색시작위치)
	 * 작성자   : 남웅주 2012.02.09
	 * 메소드   : ArrayByteFind
	 * @param  바이트배열, 바이트문자, 검색시작위치
	 * @return 해당문자의 위치
	 * */	
	public static int ArrayByteFind(byte[] bArray, byte bChar, int startPos){
		int i = 0;
		for(i=startPos;i<bArray.length;i++){
			if(bArray[i] == bChar || bArray[i] == 0x1F){
				break;
			}
		}
		return i;
	}
		
	/** 
	 * XML의 파라미터 이름을 소문자로 변환한다. 
	 * 예)     : BTUtil.upperToLower(파라미터명)
	 * 작성자   : 남웅주 2012.01.13
	 * 메소드   : upperToLower
	 * @param  파라미터명
	 * @return 변경된 문자열
	 * */	
	public static String upperToLower(String strName){ 	
		String inParamStr = null;
		String inTemp = "";
		String[] strDiv = strName.split("_");
		
		inParamStr = "";
		for(int i=0;i<strDiv.length;i++){
			inTemp = strDiv[i].toLowerCase();					  
			inParamStr += inTemp.substring(0, 1).toUpperCase() + inTemp.substring(1, inTemp.length());
		}
		return inParamStr;
	}	
	
	/** 
	 * 통신모듈 암호화 대상을 암호화 한다. (메리츠 제공소스)
	 * 예)     : BTUtil.sha256Create(파라미터명)
	 * 작성자   : 남웅주 2012.03.07
	 * 메소드   : sha256Create
	 * @param  파라미터명
	 * @return 변경된 문자열
	 * */	
	public static String sha256Create(String str) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return encoder(md.digest(str.getBytes()));		
	}
	
	public static String encoder(byte[] arrByte) throws Exception{
		   
//	       BASE64Encoder encoder = new BASE64Encoder();
//	       return encoder.encode(arrByte);
		
		   return Base64.getEncoder().encodeToString(arrByte);
	   }
	  
	public static String decoder(String str) throws Exception{
//	       BASE64Decoder decoder = new BASE64Decoder();
//	       return new String(decoder.decodeBuffer(str));
		
		   byte[] decodeBytes = Base64.getDecoder().decode(str);
		   return new String(decodeBytes);
	}
	
}
