package com.myprj.myapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 가상경로에 해당 메서드 접근 전에 가로채기
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("midx") == null) {
			
			// 로그인이 안되어있으면 이동하려고 하는 주소를 보관하고
			// 로그인 페이지로 보낸다
			saveUrl(request);  // 이동할 경로를 저장한다
			
			response.sendRedirect(request.getContextPath() + "/member/memberLogin.do");
			
			return false;
			
		} else {
			
			return true;
		}
		
	}

	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}
	
	public void saveUrl(HttpServletRequest request) {
		
		// String uri = request.getRequestURI();  // 전체경로주소
		
	    // 전체 URL 가져오기		
	    String fullUrl = request.getRequestURL().toString();
	    
	    // 컨텍스트 경로 가져오기 (프로젝트 이름 포함)
	    String contextPath = request.getContextPath();
	    
	    // 컨텍스트 경로 제외한 URL
	    String cleanUrl = fullUrl.replaceFirst(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath, "");
	    	    
		String param = request.getQueryString();  // 파라미터를 가져온다
		
		if(param == null || param.equals("null") || param.equals("")) {
			param = "";
		} else {
			param = "?" + param;
		}
		
		// 이동할 페이지
		// String locationUrl = uri + param;
		String locationUrl = cleanUrl + param;
		
		HttpSession session = request.getSession();
		if(request.getMethod().equals("GET")) {  // 대문자 GET
			session.setAttribute("saveUrl", locationUrl);
		}
	}
}
