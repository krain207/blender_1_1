package com.imeritz.blender.comm.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//import net.sf.json.JSONObject;


import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.imeritz.blender.tran.BTUtil;
import com.imeritz.blender.tran.TranUtil;




public class CommUtil {

	
	public String getCustEmail(String paramId, String paramCsno){
		HashMap bean = new HashMap();
		HashMap header = new HashMap();
		HashMap returnMap = new HashMap();
		
		String rtnEmail = "";
		CommUtil util = new CommUtil();
		
		try {
			header.put("PktProcGb", "S");
			header.put("UserId", paramId);
			bean.put("Csno", paramCsno);
			returnMap = TranUtil.sendToGateWay(bean, "ooopc_cuma_023r" ,header);
			if (returnMap.get("sysCode").toString().equals("0")) {
				rtnEmail = ((HashMap)((ArrayList)((ArrayList)returnMap.get("resultList")).get(0)).get(0)).get("EmailAddr").toString();
			}else {
				rtnEmail = "";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{

			return rtnEmail;
		}
	}	
	
	
	//남길것
		public static String gtnFormatIpAddr(String ipaddr) {
			if (ipaddr==null || ipaddr.equals("")) return "000.000.000.000";
			
			String rsltIp = "000.000.000.000";
			
			String[] arrIp = ipaddr.split("\\.");
			
			try {
				for (int i=0; i< arrIp.length; i++) {
					if (arrIp[i].toString().length()==3) ;
					else if (arrIp[i].toString().length()==2)  arrIp[i] = "0" + arrIp[i].toString();
					else if (arrIp[i].toString().length()==1)  arrIp[i] = "00" + arrIp[i].toString();
				}
				
				rsltIp = arrIp[0].toString() + "." + arrIp[1].toString() + "." + arrIp[2].toString() + "." + arrIp[3].toString();
			}catch (Exception e) {
				rsltIp  = "000.000.000.000";
			}
			
			return rsltIp;
		}		


	
}
