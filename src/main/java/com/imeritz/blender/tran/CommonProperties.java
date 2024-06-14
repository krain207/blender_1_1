package com.imeritz.blender.tran;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommonProperties {
	//서버에 올릴때는 반드시 defaultPropertiesPath 라인을 변경해 주세요.	
	
	private static String defaultPropertiesPath = "/home/imeritz/htapps/WEB-INF/config/meritz.properties";
//	private static String defaultPropertiesPath = "C:\\workspace\\meritz-home\\WebContent\\WEB-INF"+File.separatorChar+"config"+File.separatorChar+"meritz.properties";
	//private static String defaultBldPath = "/home/imeritz/htapps/WEB-INF/bld/";
//	private static String serverIp = "127.0.0.1";
//	private static String defaultBldPath = "/demo/src/main/webapp/bld/";
//	private static String serverIp = "172.17.171.65";
	
//	private static String defaultBldPath = "/home/meritz/";
	private static String defaultBldPath = "C:\\workspace\\blender_1_\\src\\main\\webapp\\bld\\";
	private static String serverIp = "127.17.172.146";
	
//	private static String defaultBldPath = "C:\\workspace\\artemis_1_5\\src\\main\\webapp\\bld\\";
//	private static String serverIp = "127.0.0.1";
	private static String serverPort = "19998";
//	private static String serverPort = "7795";
	private static String lognSessionKey = "";
	private static String lognUserIp = "";
	private static String lognUserPrivIp = "";   // 2018.10.29
	
	public static String getServerIp() {
		return serverIp;
	}
	
	public static void setServerIp(String serverIp) {
		CommonProperties.serverIp = serverIp;
	}

	public static String getServerPort() {
		return serverPort;
	}
	
	public static void setServerPort(String serverPort) {
		CommonProperties.serverPort = serverPort;
	}
	
	public static String getDefaultBldPath() {
		return defaultBldPath;
	}

	public static void setDefaultBldPath(String defaultBldPath) {
		CommonProperties.defaultBldPath = defaultBldPath;
	}

	public static String getDefaultPropertiesPath() {
		return defaultPropertiesPath;
	}

	public static void setDefaultPropertiesPath(String defaultPropertiesPath) {
		CommonProperties.defaultPropertiesPath = defaultPropertiesPath;
	}

	public static String getKey(String key){
		String value = null;
		InputStream is = null;
		Properties p = null;
		try {
			is = new FileInputStream(defaultPropertiesPath);
			p = new Properties();
			p.load(is);
			value = p.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {is.close();} catch (IOException e) {}
		}
		return value;
	}

	/**
	 * 사용중인 서버별 조회하려는 키의 정보를 다르게 하여 가져온다. 
	 * @param key
	 * @return
	 */
	public static String getKeyGbn(String key){
		String value = null;
		InputStream is = null;
		Properties p = null;
		try {
			is = new FileInputStream(defaultPropertiesPath);
			p = new Properties();
			p.load(is);
			
			InetAddress inet = InetAddress.getLocalHost();
			String keyName = "";
			value = p.getProperty(keyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {is.close();} catch (IOException e) {}
		}
		return value;
	}

	/**
	 * 프로퍼티 파일에 사용자 값을 넣는다.
	 */
	public static void putPropertie(Map<String, String> paramMap)
			throws FileNotFoundException, IOException {
		String propertiesKey = "properties.file.path";
		Properties proper = null;
		FileOutputStream output = null;
		try {
			String comment = paramMap.get("properties.comment");
			if (!paramMap.containsKey(propertiesKey)) {
				paramMap.put(propertiesKey, defaultPropertiesPath);
			}
			output = new FileOutputStream(paramMap.get(propertiesKey));
			proper = new Properties();
			proper.putAll(paramMap);
			proper.store(output, comment);
		} catch (FileNotFoundException fnfe) {
			throw new FileNotFoundException("properties 파일을 찾을수 없습니다.");
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> paramMap = new HashMap<String, String>();
		CommonProperties.setDefaultPropertiesPath(paramMap.get("properties.file.path"));

	}

	public static String getLognSessionKey() {
		return lognSessionKey;
	}

	public static void setLognSessionKey(String lognSessionKey) {
		CommonProperties.lognSessionKey = lognSessionKey;
	}

	public static String getLognUserIp() {
		return lognUserIp;
	}

	public static void setLognUserIp(String lognUserIp) { 
		CommonProperties.lognUserIp = lognUserIp;
	}
	
	public static String getLognUserPrivIp() {
		return lognUserPrivIp;
	}
	
	public static void setLognUserPrivIp(String lognUserPrivIp) {
		CommonProperties.lognUserPrivIp = lognUserPrivIp;
	}

	
	
}
