package com.myprj.myapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myprj.myapp.domain.CalendarVo;
import com.myprj.myapp.service.CalendarService;
import com.myprj.myapp.util.UserIp;

@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/calendar")  // 중복된 주소는 위쪽에서 한번에 처리
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

		logger.info("boardReservationWriteAction들어옴");
		
//		// end day 계산
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	    Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(sdf.parse(cv.getStartday()));
//	    calendar.add(Calendar.DAY_OF_MONTH, bv.getPeriod());
//
//	    // 계산된 날짜를 다시 request에 저장
//	    String calculatedDate = sdf.format(calendar.getTime());
//	    cv.setEndday(calculatedDate);
		
		String ip = userip.getUserIp(request);
		cv.setIp(ip);
		
		System.out.println("cv.getCidx : " + cv.getCidx());
		int value = 0;
		if(cv.getCidx() == 0) {
			System.out.println("인서트");
			value = calendarService.calendarInsert(cv);
		} else {
			System.out.println("업데이트");
			value = calendarService.calendarUpdate(cv);
		}
		
		JSONObject js = new JSONObject();

		js.put("value", value);

		return js;
	}
	
	@ResponseBody
	@RequestMapping(value="/{bidx}/getCalendarAll.do")
	public ArrayList<Map<String, Object>> getCalendarAll(
			@PathVariable("bidx") int bidx
			) throws Exception {

		logger.info("getCalendarAll들어옴");
		
		ArrayList<CalendarVo> clist = calendarService.calendarSelectAll(bidx);
		
		ArrayList<Map<String, Object>> events = new ArrayList<>();

        // 데이터 추가
		for(CalendarVo cv : clist ) {
	        Map<String, Object> extendedProps = new HashMap<>();
	        extendedProps.put("fromTo", cv.getStartday() + " ~ " + cv.getEndday());
	        extendedProps.put("adultprice", cv.getAdultprice());
	        extendedProps.put("childprice", cv.getChildprice());
	        
	        Map<String, Object> event = new HashMap<>();
	        event.put("start", cv.getStartday());
	        event.put("display", "list-item");
	        event.put("backgroundColor", "#0d6efd");
	        event.put("extendedProps", extendedProps);
	        events.add(event);
		}
		
		System.out.println(events);
        return events;
	}
	
}
