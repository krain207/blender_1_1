package com.imeritz.blender.comm.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imeritz.blender.comm.beans.BlenderBean;
import com.imeritz.blender.comm.dao.BlenderRestDao;


@Service("blenderRestService")
public class BlenderRestServiceImpl implements BlenderRestService {

//	public ArtmTestRestServiceImpl() {
//		// TODO Auto-generated constructor stub
//	}
	
	protected static Logger logger = LoggerFactory.getLogger(BlenderRestServiceImpl.class);
	
	private BlenderRestDao blenderRestDao ;
	
	@Autowired
	public void setArtmTestRestDao (BlenderRestDao blenderRestDao) {
		this.blenderRestDao = blenderRestDao;
	}
	
//	@SuppressWarnings("unused")
//	private AdminLoginChkDao adminLogindao;

//	@Autowired
//	public void setLoginDao(AdminLoginChkDao adminLogindao) {
//		this.adminLogindao = adminLogindao;
//	}
	
	
	/**
	* 사용자 정보 확인
	* @author 이재화
	* @since  2024.03.18
	* @version 1.0 
	*/
	@Override
	public HashMap testArrList(HashMap bean) throws Exception {
		return blenderRestDao.testArrList(bean);
	}

	/**
	 * 카카오 알리미 연계 전문 확인
	 * @author 이재화
	 * @since  2024.03.21
	 * @version 1.0 
	 */
	@Override
	public HashMap testKakaArrList(HashMap bean) throws Exception {
		return blenderRestDao.testKakaoArrList(bean);
	}
	
	

}
