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
	
	@Autowired(required=false)  // @Autowired : Ÿ���� ���� ��ü�� ã�Ƽ� ����. required=false : ���� ���� ������ ���, null�� ����
	private BoardService boardService;
		
	//���� ������ �̵�
	@RequestMapping(value = "/main.do")
	public String mainPage(Model model) {
		
		logger.info("main����");
		
		ArrayList<BoardVo> tlist = boardService.boardSelect("travel", 10);
		ArrayList<BoardVo> nlist = boardService.boardSelect("notice", 4);
		model.addAttribute("tlist", tlist);
		model.addAttribute("nlist", nlist);
		
		return "WEB-INF/main";
	}	
	
}