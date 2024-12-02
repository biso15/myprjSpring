package com.myprj.myapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myprj.myapp.domain.CalendarVo;
import com.myprj.myapp.service.CalendarService;
import com.myprj.myapp.util.UserIp;

@Controller  // Controller ��ü�� �������
@RequestMapping(value="/calendar")  // �ߺ��� �ּҴ� ���ʿ��� �ѹ��� ó��
public class CalendarController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
			
	@Autowired(required=false)
	private CalendarService calendarService;
		
	@Autowired(required=false)
	private UserIp userip;
	
	@ResponseBody
	@RequestMapping(value="/{bidx}/travelReservationWriteAction.do", method=RequestMethod.POST)
	public JSONObject boardWriteAction(
			CalendarVo cv,
			HttpServletRequest request
			) throws Exception {

		logger.info("boardReservationWriteAction����");
		
//		// end day ���
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	    Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(sdf.parse(cv.getStartday()));
//	    calendar.add(Calendar.DAY_OF_MONTH, bv.getPeriod());
//
//	    // ���� ��¥�� �ٽ� request�� ����
//	    String calculatedDate = sdf.format(calendar.getTime());
//	    cv.setEndday(calculatedDate);
		
		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession�� HttpServletRequest �ȿ� ����
		int midx_int = Integer.parseInt(midx);
		cv.setMidx(midx_int);
		
		String ip = userip.getUserIp(request);
		cv.setIp(ip);
		
		int value = calendarService.calendarInsert(cv);
		
		JSONObject js = new JSONObject();

		js.put("value", value);

		return js;
	}
	
}
