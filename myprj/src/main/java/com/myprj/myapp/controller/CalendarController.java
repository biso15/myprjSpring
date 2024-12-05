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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.CalendarVo;
import com.myprj.myapp.service.BoardService;
import com.myprj.myapp.service.CalendarService;
import com.myprj.myapp.util.UserIp;

@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/calendar")  // 중복된 주소는 위쪽에서 한번에 처리
public class CalendarController {
	
	private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);
	
	@Autowired(required=false)  // @Autowired : 타입이 같은 객체를 찾아서 주입. required=false : 만약 주입 못받을 경우, null로 지정
	private BoardService boardService;
	
	@Autowired(required=false)
	private CalendarService calendarService;
	
	@Autowired(required=false)
	private UserIp userip;
	
	@RequestMapping(value="/{bidx}/calendarWrite.do")
	public String calendarWrite(
			@PathVariable("bidx") int bidx,
			Model model) {
		
		logger.info("calendarWrite들어옴");
		
		BoardVo bv = boardService.boardSelectOne(bidx);  // 해당되는 bidx의 게시물 데이터 가져옴
		ArrayList<CalendarVo> clist = calendarService.calendarSelectAll(bidx);

		String menu = "";
		if(bv.getPeriod() == 1) {
			menu = "당일치기";
		} else if(bv.getPeriod() == 2) {
			menu = "1박2일";
		} else if(bv.getPeriod() == 3) {
			menu = "2박3일";
		} else if(bv.getPeriod() == 4) {
			menu = "3박4일";
		}
		
		model.addAttribute("bv", bv);
		model.addAttribute("menu", menu);
		model.addAttribute("clist", clist);

		return "WEB-INF/board/calendarWrite";
	}

	@ResponseBody
	@RequestMapping(value="/{bidx}/calendarWriteAction.do", method=RequestMethod.POST)
		public JSONObject calendarWriteAction(
			CalendarVo cv,
			HttpServletRequest request
			) throws Exception {

		logger.info("calendarWriteAction들어옴");
		
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
		
		Integer value = calendarService.calendarFindIdx(cv.getBidx(), cv.getStartday());  // int는 null값인 경우 오류 발생. Integer는 null값 가능
		
		System.out.println(value);		
		
		if(value == null) {  // value가 null인 경우 NullPointerException을 발생시킬 가능성이 있으므로, null을 먼저 비교한다.
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
