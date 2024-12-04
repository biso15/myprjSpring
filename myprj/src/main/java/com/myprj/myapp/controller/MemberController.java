package com.myprj.myapp.controller;

import javax.servlet.http.HttpServletRequest;
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

import com.myprj.myapp.domain.MemberVo;
import com.myprj.myapp.service.MemberService;
import com.myprj.myapp.util.UserIp;

@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/member")  // 중복된 주소는 위쪽에서 한번에 처리
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired(required=false)
	private UserIp userip;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(value="memberJoin.do", method=RequestMethod.GET)
	public String memberJoin() {
		
		logger.info("memberJoin들어옴");
		
		return "WEB-INF/member/memberJoin";
	}
	
	@RequestMapping(value="/memberJoinAction.do", method=RequestMethod.POST)
	public String memberJoinAction(
			MemberVo mv,
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {

		logger.info("memberJoinAction들어옴");

		// 비밀번호 암호화
		String memberpwd_enc = bCryptPasswordEncoder.encode(mv.getPassword());
		mv.setPassword(memberpwd_enc);
		
		String ip = userip.getUserIp(request);
		mv.setIp(ip);
		
		int value = memberService.memberInsert(mv);
		
		String path = "";
		if(value == 1) {
			rttr.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
			path = "redirect:/";
		} else {
			rttr.addFlashAttribute("msg", "회원가입이 실패했습니다.");
			path = "redirect:/member/memberJoin.do";			
		}
		
		return path;
	}	
	
	@RequestMapping(value="memberLogin.do", method=RequestMethod.GET)
	public String memberLogin() {

		logger.info("memberLogin들어옴");
		
		return "WEB-INF/member/memberLogin";
	}
	
	@RequestMapping(value="memberLoginAction.do", method=RequestMethod.POST)
	public String memberLoginAction(
			MemberVo inputMv,
			HttpSession session,
			RedirectAttributes rttr
		) {

		logger.info("memberLoginAction들어옴");
		
		MemberVo mv = memberService.memberSelect(inputMv.getId());

		String path = "";
		
		if(mv != null) {  // id 있음
			
			// 저장된 비밀번호를 가져온다
			String password = mv.getPassword();			
			
			if(bCryptPasswordEncoder.matches(inputMv.getPassword(), password)) {  // 인코딩 된 부분이 2번째 파라미터
				logger.info("비밀번호 일치");
				
				rttr.addAttribute("midx", mv.getMidx());
				rttr.addAttribute("id", mv.getId());
				rttr.addAttribute("adminyn", mv.getAdminyn());				

				rttr.addFlashAttribute("msg", mv.getName() + "님 환영합니다🎉");
				
				if(session.getAttribute("saveUrl") != null) {  // 이동할 위치 확인 -> interceptor
				System.out.println("로그인컨트롤러에 있는 주소 : " + session.getAttribute("saveUrl"));
					path = "redirect:" + session.getAttribute("saveUrl").toString();
				} else {
					path = "redirect:/";
				}
				
				return path;
			}

			logger.info("비밀번호 불일치");
		}
		
		rttr.addAttribute("midx", "");
		rttr.addAttribute("id", "");
		rttr.addAttribute("adminyn", "");
		
		rttr.addFlashAttribute("msg", "아이디/비밀번호를 확인해주세요");  // addAttribute와 다르게 1회성임. 새로고침시 사라진다
					
		return "redirect:/member/memberLogin.do";
		
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
	public String memberLogout(
			HttpSession session,
			RedirectAttributes rttr) {

		logger.info("memberLogout들어옴");
		
		session.removeAttribute("midx");
		session.removeAttribute("memberId");
		session.removeAttribute("memberName");
		session.removeAttribute("adminyn");		

		rttr.addFlashAttribute("msg", "로그아웃이 완료되었습니다.");
		return "redirect:/";
		
	}
	
	@RequestMapping(value="memberMypage.do", method=RequestMethod.GET)
	public String memberMypage(HttpServletRequest request, Model model) {

		logger.info("memberMypage들어옴");

		String id = request.getSession().getAttribute("id").toString();
		MemberVo mv = memberService.memberSelect(id);
		
		model.addAttribute("mv", mv);
		
		return "WEB-INF/member/memberMypage";
	}

	@RequestMapping(value="memberMypageAction.do", method=RequestMethod.POST)
	public String memberMypage(
			MemberVo mv, 
			HttpServletRequest request, 
			RedirectAttributes rttr
			) throws Exception {

		logger.info("memberMypageAction들어옴");
		
		String path = "";		

		// 저장된 midx를 가져온다
		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx);
		mv.setMidx(midx_int);

		// 비밀번호 암호화
		String passwordEnc = bCryptPasswordEncoder.encode(mv.getPassword());
		mv.setPassword(passwordEnc);

		String ip = userip.getUserIp(request);
		mv.setIp(ip);
		
		int value = memberService.memberUpdate(mv);
		
		if(value == 1) {
			rttr.addFlashAttribute("msg", "회원정보 수정이 완료되었습니다.");
		} else {
			rttr.addFlashAttribute("msg", "회원정보 수정이 실패했습니다.");
		}			
		
		return "redirect:/member/memberMypage.do";
	}
	
	@RequestMapping(value="/memberDeleteAction.do")
	public String memberCancelAction(
			MemberVo mv, 
			HttpServletRequest request, 
			RedirectAttributes rttr
			) throws Exception {

		logger.info("memberDeleteAction들어옴");
		
		String path = "";

		// 저장된 midx를 가져온다
		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx);
		mv.setMidx(midx_int);

		String ip = userip.getUserIp(request);
		mv.setIp(ip);
		
		int value = memberService.memberDelete(mv);
		
		if(value == 1) {
			rttr.addFlashAttribute("msg", "회원탈퇴가 완료되었습니다.");
		} else {
			rttr.addFlashAttribute("msg", "회원탈퇴가 실패했습니다.");
		}
		
		return "redirect:/member/memberLogout.do";
	}	
	
}
