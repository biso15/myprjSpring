package com.myprj.myapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myprj.myapp.aop.SampleAdvice;
import com.myprj.myapp.domain.CommentVo;
import com.myprj.myapp.service.CommentService;
import com.myprj.myapp.util.UserIp;

@RestController
@RequestMapping(value="/comment")
public class CommentController {

	@Autowired(required=false)
	CommentService commentService;

	@Autowired(required=false)
	private UserIp userip;
	
	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
	
	@RequestMapping(value="/{bidx}/commentList.do")
	public JSONObject commentList(@PathVariable("bidx") int bidx) {

		logger.info("commentList µé¾î¿È");
				
		ArrayList<CommentVo> clist = commentService.commentSelectAll(bidx);
		
		JSONObject js = new JSONObject();

		js.put("clist", clist);
		
		return js;
	}
	
	@RequestMapping(value="/commentWriteAction.do", method=RequestMethod.POST)
	public JSONObject commentWriteAction(
			CommentVo cv,
			HttpServletRequest request
			) throws Exception {

		System.out.println("comment : " + cv.getContents());
		logger.info("commentWriteAction µé¾î¿È");
		
		cv.setIp(userip.getUserIp(request));

		int value = commentService.commentInsert(cv);
		
		JSONObject js = new JSONObject();
		js.put("value", value);
		
		return js;
	}
	
	@RequestMapping(value="/{cidx}/commentDeleteAction.do")
	public JSONObject commentDeleteAction(
			@PathVariable("cidx") int cidx,
			HttpServletRequest request,
			CommentVo cv) throws Exception {

		logger.info("commentDeleteAction µé¾î¿È");
		
		int midx = Integer.parseInt(request.getSession().getAttribute("midx").toString());
		cv.setMidx(midx);
		cv.setCidx(cidx);
		cv.setIp(userip.getUserIp(request));
		
		int value = commentService.commentDelete(cv);
		
		JSONObject js = new JSONObject();
		js.put("value", value);
		
		return js;
	}
	
}
