package com.myprj.myapp.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.service.BoardService;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired(required=false)  // @Autowired : 타입이 같은 객체를 찾아서 주입. required=false : 만약 주입 못받을 경우, null로 지정
	private BoardService boardService;
		
	//메인 페이지 이동
	@RequestMapping(value = "/main.do")
	public String mainPage(Model model) {
		
		logger.info("main들어옴");
		
		ArrayList<BoardVo> tlist = boardService.boardSelect("travel", 10);
		ArrayList<BoardVo> nlist = boardService.boardSelect("notice", 4);
		model.addAttribute("tlist", tlist);
		model.addAttribute("nlist", nlist);
		
		return "WEB-INF/main";
	}	
	
}