package com.myprj.myapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	// �α����Ŀ� ȸ�������� ���ǿ� ��´�
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// ����ä�� �ϱ� ���� ó���ϴ� �޼ҵ�
		HttpSession session = request.getSession();
		
		if(session.getAttribute("midx") != null) {
			session.removeAttribute("midx");
			session.removeAttribute("id");
			session.removeAttribute("adminyn");
		}
		
		return true;
	}
	
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		
		// RedirectAttributes�� Model ��ü�� ���� ���� ������
		// loginAction.do���� �α��� ������ rttr.addAttribute�� ���� ���� �����ͼ� ����
		String midx = modelAndView.getModel().get("midx").toString();
		String id = modelAndView.getModel().get("id").toString();
		String adminyn = modelAndView.getModel().get("adminyn").toString();
		
		modelAndView.getModel().clear();  // (url�� �Ķ���ͷ� ���� �Ѿ�� �ʰ� �ϱ� ����)�Ķ���� model ���� �����.
		
		HttpSession session = request.getSession();
		if(midx != null) {
			session.setAttribute("midx", midx);
			session.setAttribute("id", id);
			session.setAttribute("adminyn", adminyn);
		}
	}
}
