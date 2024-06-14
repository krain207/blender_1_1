package com.imeritz.blender.comm.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.JDOMException;
import org.springframework.stereotype.Repository;

import com.imeritz.blender.comm.beans.BlenderBean;
import com.imeritz.blender.tran.TranUtil;

@Repository("BlenderRestDao")
public class BlenderRestDaoImpl implements BlenderRestDao {

	@Override
	public HashMap testArrList(HashMap bean) {
//		List<ArtmTestBean> alist = new ArrayList<ArtmTestBean>();
		HashMap returnMap = new HashMap();
		HashMap header = new HashMap();

		System.out.println("[BlenderRestDaoImpl]*****************************");
		
		try {
			header.put("PktProcGb", bean.get("header"));
			header.put("UserId", bean.get("UserId"));
			
			returnMap = TranUtil.sendToGateWay(bean, "ooopc_cuma_023r", header);
			
		} catch (JDOMException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {	
			System.out.println(e.getMessage());
		}
		
		return returnMap;
	}
	
	
	@Override
	public HashMap testKakaoArrList(HashMap bean) {
//		List<ArtmTestBean> alist = new ArrayList<ArtmTestBean>();
		HashMap returnMap = new HashMap();
		HashMap header = new HashMap();
		
		System.out.println("[BlenderRestDaoImpl.testKakaoArrList]*****************************");
		
		try {
			header.put("PktProcGb", bean.get("header"));
			header.put("UserId", bean.get("UserId"));
			
			returnMap = TranUtil.sendToGateWay(bean, "obwat_move_021r", header);
			
		} catch (JDOMException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {	
			System.out.println(e.getMessage());
		}
		
		return returnMap;
	}

}
