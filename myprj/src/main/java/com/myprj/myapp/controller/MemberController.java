package com.myprj.myapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.MemberVo;
import com.myprj.myapp.service.MemberService;

@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/member")  // 중복된 주소는 위쪽에서 한번에 처리
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(value="memberJoin.do", method=RequestMethod.GET)
	public String memberJoin() {
		
		logger.info("memberJoin들어옴");
		
		return "WEB-INF/member/memberJoin";
	}
	
	@RequestMapping(value="/memberJoinAction.aws", method=RequestMethod.POST)
	public String memberJoinAction(MemberVo mv) {  // <jsp:useBean id = "mv" class="Vo.MemberVo" scope = "page" /> 이것과 같다. form 태그 내부의 name과 class의 변수명이 같으면 getParameter 대체 가능

		logger.info("memberJoinAction들어옴");

		// 비밀번호 암호화
		String memberpwd_enc = bCryptPasswordEncoder.encode(mv.getMemberpwd());
		mv.setMemberpwd(memberpwd_enc);
		
		int value = memberService.memberInsert(mv);
		
		String path = "";
		if(value == 1) {
			path = "redirect:/";
		} else {
			path = "redirect:/member/memberJoin.aws";			
		}
		
		return path;
	}	
	
	
	@RequestMapping(value="memberLogin.do", method=RequestMethod.GET)
	public String memberLogin() {
		
		return "WEB-INF/member/memberLogin";
	}
	
	@RequestMapping(value="memberLoginAction.do", method=RequestMethod.POST)
	public String memberLoginAction(
			MemberVo inputMv,
			HttpSession session,
			RedirectAttributes rttr
		) {

		logger.info("memberLoginAction들어옴");
		
		MemberVo mv = memberService.memberLogin(inputMv.getId());

		String path = "";
		
		if(mv != null) {  // id 있음
			logger.info("mv != null 이야 ");
			
			// 저장된 비밀번호를 가져온다
			String password = mv.getPassword();
			
			if(password.equals(inputMv.getPassword())) {
				
			// if(bCryptPasswordEncoder.matches(password, inputMv.getId())) {  // 비밀번호 일치
				logger.info("비밀번호 일치");
				
				rttr.addAttribute("midx", mv.getMidx());
				rttr.addAttribute("memberId", mv.getId());
				rttr.addAttribute("memberName", mv.getName());
				rttr.addAttribute("adminyn", mv.getAdmin());
				
				rttr.addFlashAttribute("msg", "님 환영합니다🎉");
				
				if(session.getAttribute("saveUrl") != null) {  // 이동할 위치 확인 -> interceptor
					path = "redirect:" + session.getAttribute("saveUrl").toString();
				} else {
					path = "redirect:/";
				}
				
			} else {  // 비밀번호 불일치
				
				rttr.addFlashAttribute("msg", "아이디/비밀번호를 확인해주세요");  // addAttribute와 다르게 1회성임. 새로고침시 사라진다
				
				path = "redirect:/member/memberLogin.do";
			}			
			
		} else {  // 객체값이 없으면

			rttr.addFlashAttribute("msg", "해당하는 아이디가 없습니다.");
			
			path = "redirect:/member/memberLogin.do";
		}
		
		return path;
		
		  
	
//				rttr.addFlashAttribute("msg", "님 환영합니다🎉");
//				return path;
//			}
//
//			logger.info("비밀번호 불일치");
//		}
//		
//		rttr.addFlashAttribute("msg", "아이디/비밀번호를 확인해주세요");  // addAttribute와 다르게 1회성임. 새로고침시 사라진다
//					
//		return "redirect:/member/memberLogin.do";
		
	}
	
	@ResponseBody
	@RequestMapping(value="memberIdCheck.do", method=RequestMethod.POST)
	public JSONObject memberIdCheck(@RequestParam("id") String id) {  // 자바의 String memberId = request.getParameter("memberId"); 대체

		logger.info("memberIdCheck들어옴");
				
		int cnt = memberService.memberIdCheck(id);
		
		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);
		
		return obj;	
		
	}
	
	@RequestMapping(value="memberLogout.do", method=RequestMethod.GET)
	public String memberLogout(HttpSession session) {

		logger.info("memberLogout들어옴");
		
		session.removeAttribute("midx");
		session.removeAttribute("memberId");
		session.removeAttribute("memberName");
		session.removeAttribute("adminyn");		
		
		return "redirect:/";
		
	}	
	
}
