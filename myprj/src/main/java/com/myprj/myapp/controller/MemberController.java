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

import com.myprj.myapp.domain.MemberVo;
import com.myprj.myapp.service.MemberService;

@Controller  // Controller ��ü�� �������
@RequestMapping(value="/member/")  // �ߺ��� �ּҴ� ���ʿ��� �ѹ��� ó��
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);  // ������ڵ�ó�� ����ϸ�, java�� System.out.println();�� ��������� ���ҽ��� �� �Һ��Ѵ�.
	
	// @Autowired  // ������ ��û
		
	@Autowired
	private MemberService memberService;  // Interface���� ���Թ޾ƾ� implements �� class���� ����Ǿ �������� �ʾƵ� ��	
	
	@Autowired  // @Autowired�� ���� �ٿ���� ��
	private BCryptPasswordEncoder bCryptPasswordEncoder;  //  ��й�ȣ ��ȣȭ
	
	@RequestMapping(value="memberJoin.aws", method=RequestMethod.GET)  // ";" ���� ����
	public String memberJoin() {
		
		logger.info("memberJoin����");  // INFO : com.myprj.myapp.controller.MemberController - memberJoin����
		
		// logger.info("tt���� " + tt.test());
		
		return "WEB-INF/member/memberJoin";
	}
	
	@RequestMapping(value="memberJoinAction.aws", method=RequestMethod.POST)
	public String memberJoinAction(MemberVo mv) {  // <jsp:useBean id = "mv" class="Vo.MemberVo" scope = "page" /> �̰Ͱ� ����. form �±� ������ name�� class�� �������� ������ getParameter ��ü ����

		logger.info("memberJoinAction����");

		// ��й�ȣ ��ȣȭ
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
	
	
	@RequestMapping(value="memberLogin.aws", method=RequestMethod.GET)
	public String memberLogin() {
		
		return "WEB-INF/member/memberLogin";
	}
	
	@RequestMapping(value="memberLoginAction.aws", method=RequestMethod.POST)
	public String memberLoginAction(
			@RequestParam("memberid") String memberId, 
			@RequestParam("memberpwd") String memberPwd,
			RedirectAttributes rttr,  // request.setAttribute("serverTime", formattedDate); �� ���
			HttpSession session
		) {

		logger.info("memberLoginAction����");
		
		MemberVo mv = memberService.memberLoginCheck(memberId);
		
		String path = "";
		
		if(mv != null) {  // ��ü���� ������
			
			// ����� ��й�ȣ�� �����´�
			String reservedPwd = mv.getMemberpwd();
			
			if(bCryptPasswordEncoder.matches(memberPwd, reservedPwd)) {  // ��й�ȣ ��ġ
				logger.info("��й�ȣ ��ġ");
				rttr.addAttribute("midx", mv.getMidx());
				rttr.addAttribute("memberId", mv.getMemberid());
				rttr.addAttribute("memberName", mv.getMembername());
				
				if(session.getAttribute("saveUrl") != null) {
					path = "redirect:" + session.getAttribute("saveUrl").toString();
				} else {					
					path = "redirect:/";
				}
				
			} else {  // ��й�ȣ ����ġ
				
				rttr.addFlashAttribute("msg", "���̵�/��й�ȣ�� Ȯ�����ּ���");  // addAttribute�� �ٸ��� 1ȸ����. ���ΰ�ħ�� �������
				
				path = "redirect:/member/memberLogin.aws";
			}			
			
		} else {  // ��ü���� ������

			rttr.addFlashAttribute("msg", "�ش��ϴ� ���̵� �����ϴ�.");
			
			path = "redirect:/member/memberLogin.aws";
		}
		
		return path;
		
		// model.addAttribute("serverTime", formattedDate);
		
	}
	
	@ResponseBody
	@RequestMapping(value="memberIdCheck.aws", method=RequestMethod.POST)
	public JSONObject memberIdCheck(@RequestParam("memberId") String memberId) {  // �ڹ��� String memberId = request.getParameter("memberId"); ��ü

		logger.info("memberIdCheck����");
		
		// MemberDao md = new MemberDao();  // Spring������ Javaó�� POJO ��ĵ� ���������� service�� ����Ұ���
		// int cnt = md.memberIdCheck(memberId);
		
		int cnt = memberService.memberIdCheck(memberId);

		// PrintWriter out = response.getWriter();  // ���̺귯�� ����ؼ� json ���� ���� �������
		// out.println("{\"cnt\":\""+cnt+"\"}");
		
		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);
		
		return obj;	
		
	}
	
	@RequestMapping(value="memberList.aws", method=RequestMethod.GET)
	public String memberList(Model model) {  // model�� �ڵ����� �����Ǵ� ��ü�̴�

		logger.info("memberList����");
		
		ArrayList<MemberVo> alist = memberService.memberSelectAll();
		model.addAttribute("alist", alist);
		
		return "WEB-INF/member/memberList";
		
	}

	
	@RequestMapping(value="memberLogout.aws", method=RequestMethod.GET)
	public String memberLogout(HttpSession session) {  // model�� �ڵ����� �����Ǵ� ��ü�̴�

		logger.info("memberLogout����");
		
		session.removeAttribute("midx");
		session.removeAttribute("membername");
		session.removeAttribute("memberId");
		session.invalidate();
		
		return "redirect:/";
		
	}
	
	
}
