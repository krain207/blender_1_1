package com.imeritz.blender.comm.service;

import java.util.HashMap;

public interface BlenderRestService {

	/**
	* 사용자 확인
	* @author 이재화
	* @since  2024.03.18
	* @version 1.0 
	*/
	public HashMap testArrList(HashMap bean) throws Exception;
	/**
	 * 카카오 알리미 연계 전문 확인
	 * @author 이재화
	 * @since  2024.03.21
	 * @version 1.0 
	 */
	public HashMap testKakaArrList(HashMap bean) throws Exception;

}
