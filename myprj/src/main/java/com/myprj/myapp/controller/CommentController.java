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

@RestController  // @ResponseBody�� ���ԵǾ� �ִ� Controller
@RequestMapping(value="/comment")
public class CommentController {

	@Autowired(required=false)
	CommentService commentService;

	@Autowired(required=false)
	private UserIp userip;
	
	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
	
	@RequestMapping(value="/{bidx}/{block}/commentList.aws")  // RestFul ���
	public JSONObject commentList(
			@PathVariable("bidx") int bidx,
			@PathVariable("block") int block
			) {

		logger.info("commentList ����");
		
		String moreView = "";
		int nextBlock = 0;
		int cnt = commentService.commentTotalCnt(bidx);
		if(cnt > block * 15) {
			moreView = "Y";
			nextBlock = block + 1;
		} else {
			moreView = "N";
			nextBlock = block;
		}

		ArrayList<CommentVo> clist = commentService.commentSelectAll(bidx, block);
		
		JSONObject js = new JSONObject();

		js.put("clist", clist);
		js.put("moreView", moreView);
		js.put("nextBlock", nextBlock);
		
		return js;
	}
	
	@RequestMapping(value="/commentWriteAction.aws", method=RequestMethod.POST)
	public JSONObject commentWriteAction(
			CommentVo cv,
			HttpServletRequest request
			) throws Exception {

		logger.info("commentWriteAction ����");
		
		cv.setCip(userip.getUserIp(request));

		int value = commentService.commentInsert(cv);
		
		JSONObject js = new JSONObject();
		js.put("value", value);
		
		return js;
	}
	
	@RequestMapping(value="/{cidx}/commentDeleteAction.aws")
	public JSONObject commentDeleteAction(
			@PathVariable("cidx") int cidx,
			HttpServletRequest request,
			CommentVo cv) throws Exception {

		logger.info("commentDeleteAction ����");
		
		int midx = Integer.parseInt(request.getSession().getAttribute("midx").toString());
		cv.setMidx(midx);
		cv.setCidx(cidx);
		cv.setCip(userip.getUserIp(request));
		
		int value = commentService.commentDelete(cv);
		
		JSONObject js = new JSONObject();
		js.put("value", value);
		
		return js;
	}
	
}
