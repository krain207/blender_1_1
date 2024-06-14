/**
 * 
 */
package com.imeritz.blender.comm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.filters.ExpiresFilter.XHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.imeritz.blender.comm.beans.BlenderKakaSendBean;
import com.imeritz.blender.comm.beans.RequestTestDto;
import com.imeritz.blender.comm.service.BlenderRestService;
import com.imeritz.blender.comm.beans.BlenderBean;
import com.imeritz.blender.comm.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import reactor.core.publisher.Mono;

/**
 * Artemis Test 
 */
@RestController
public class BlenderRestController {
	private static final Logger logger = LoggerFactory.getLogger(BlenderRestController.class);
	
	private BlenderRestService blenderrestsvc;
	
	@Autowired
	public void setBlenderRestService( BlenderRestService blenderrestsvc) {
		this.blenderrestsvc = blenderrestsvc;
	}
	
	
    private WebClient customWebClient;

    @Autowired
    public void setCustomWebClient(WebClient customWebClient) {
        this.customWebClient = customWebClient;
    }
	
	// Success  ****************************************
	@SuppressWarnings({ "unused", "rawtypes" })
	@PostMapping("/SpringTest3.do")
	private ResponseEntity<HashMap> springTest3 (Model model
			, @ModelAttribute("requestTestDto") RequestTestDto requestTestDto
			, HttpServletRequest request
			, HttpServletResponse response) throws IOException 
	{
		System.out.println(requestTestDto.getCsno());
		
		
		String msg = "";
		HashMap returnMap = new HashMap();
		HashMap bean = new HashMap();
		
		try {
			bean.put("header", "S");
			bean.put("UserId", "imeritz");
			bean.put("ChnlMediaVrfy", "060");
			
			bean.put("Csno", requestTestDto.getCsno());
			
			System.out.println("[ArtmTestRestController]...");
			System.out.println("[ArtmTestRestController bean]..." + bean.toString());
			
			
			returnMap = this.blenderrestsvc.testArrList(bean);
			
		} catch (Exception e) {
			System.out.println("[ArtmTestRestController Errer]" + e.getMessage());
		}
		
		return ResponseEntity.ok(returnMap);
	}
	
	
	// Success  ***** 전문
	@SuppressWarnings({ "unused", "rawtypes" })
	@PostMapping("/kaKaoTest000.do")
	private ResponseEntity<HashMap> kaKaoTest1 (Model model
			, @ModelAttribute("artmKakaSendBean") BlenderKakaSendBean artmKakaSendBean
			, HttpServletRequest request
			, HttpServletResponse response) throws IOException 
	{
		System.out.println("ProsClsCode:" + artmKakaSendBean.getProsClsCode());
		System.out.println("Acno:" + artmKakaSendBean.getAcno());
		
		
		String msg = "";
		HashMap returnMap = new HashMap();
		HashMap bean = new HashMap();
		
		try {
			bean.put("header", "S");
			bean.put("UserId", "imeritz");
			bean.put("ChnlMediaVrfy", "060");
			
			bean.put("ProsClsCode", artmKakaSendBean.getProsClsCode());
			bean.put("Acno", artmKakaSendBean.getAcno());
			bean.put("Asno", "");
			bean.put("CodeVal", "");
			bean.put("CodeValdVal", "");
			
			System.out.println("[kaKaoTest1]...");
			System.out.println("[kaKaoTest1 bean]..." + bean.toString());
			
			
			returnMap = this.blenderrestsvc.testKakaArrList(bean);
			
		} catch (Exception e) {
			System.out.println("[kaKaoTest1 Errer]" + e.getMessage());
		}
		
		return ResponseEntity.ok(returnMap);
	}
	
	
	// Success  ***** WebClient Test *****************************************************************
	@SuppressWarnings({ "unused", "rawtypes" })
	@PostMapping("/wc001.do")
	private ResponseEntity<HashMap> wcTest01 (Model model
			, @ModelAttribute("blenderKakaSendBean") BlenderKakaSendBean blenderKakaSendBean
			, HttpServletRequest request
			, HttpServletResponse response) throws IOException 
	{
		System.out.println("ProsClsCode:" + blenderKakaSendBean.getProsClsCode());
		System.out.println("Acno:" + blenderKakaSendBean.getAcno());
		
		
		String msg = "";
		HashMap returnMap = new HashMap();
		HashMap bean = new HashMap();
		
		try {
			bean.put("header", "S");
			bean.put("UserId", "imeritz");
			bean.put("ChnlMediaVrfy", "060");
			
//			bean.put("ProsClsCode", artmKakaSendBean.getProsClsCode());
//			bean.put("Acno", artmKakaSendBean.getAcno());
			bean.put("Asno", "");
			bean.put("CodeVal", "");
			bean.put("CodeValdVal", "");
			
			System.out.println("[wc001]...");
			System.out.println("[wc001 bean]..." + bean.toString());
			
			// HTTP Client Test
			String rsltMonoStr = getPost(0).toString();
			
			System.out.println("[rsltMonoStr]:" + rsltMonoStr);
			
//			returnMap = this.blenderrestsvc.testKakaArrList(bean);
			
		} catch (Exception e) {
			System.out.println("[kaKaoTest1 Errer]" + e.getMessage());
		}
		
		return ResponseEntity.ok(returnMap);
	}
	
	
	public Mono<String> getPost (int postId) {
		return this.customWebClient.post()
		.uri("/kaKaoTest00"+postId+".do")
		.retrieve()
		.bodyToMono(String.class);
	}
}
