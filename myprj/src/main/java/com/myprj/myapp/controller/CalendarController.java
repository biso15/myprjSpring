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

@Controller  // Controller ��ü�� �������
@RequestMapping(value="/calendar")  // �ߺ��� �ּҴ� ���ʿ��� �ѹ��� ó��
public class CalendarController {
	
	private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);
	
	@Autowired(required=false)  // @Autowired : Ÿ���� ���� ��ü�� ã�Ƽ� ����. required=false : ���� ���� ������ ���, null�� ����
	private BoardService boardService;
	
	@Autowired(required=false)
	private CalendarService calendarService;
	
	@Autowired(required=false)
	private UserIp userip;
	
	@RequestMapping(value="/{bidx}/calendarWrite.do")
	public String calendarWrite(
			@PathVariable("bidx") int bidx,
			Model model) {
		
		logger.info("calendarWrite����");
		
		BoardVo bv = boardService.boardSelectOne(bidx);  // �ش�Ǵ� bidx�� �Խù� ������ ������
		ArrayList<CalendarVo> clist = calendarService.calendarSelectAll(bidx);

		String menu = "";
		if(bv.getPeriod() == 1) {
			menu = "����ġ��";
		} else if(bv.getPeriod() == 2) {
			menu = "1��2��";
		} else if(bv.getPeriod() == 3) {
			menu = "2��3��";
		} else if(bv.getPeriod() == 4) {
			menu = "3��4��";
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

		logger.info("calendarWriteAction����");
		
//		// end day ���
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	    Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(sdf.parse(cv.getStartday()));
//	    calendar.add(Calendar.DAY_OF_MONTH, bv.getPeriod());
//
//	    // ���� ��¥�� �ٽ� request�� ����
//	    String calculatedDate = sdf.format(calendar.getTime());
//	    cv.setEndday(calculatedDate);
		
		String ip = userip.getUserIp(request);
		cv.setIp(ip);
		
		Integer value = calendarService.calendarFindIdx(cv.getBidx(), cv.getStartday());  // int�� null���� ��� ���� �߻�. Integer�� null�� ����
		
		System.out.println(value);		
		
		if(value == null) {  // value�� null�� ��� NullPointerException�� �߻���ų ���ɼ��� �����Ƿ�, null�� ���� ���Ѵ�.
			System.out.println("�μ�Ʈ");
			value = calendarService.calendarInsert(cv);
		} else {
			System.out.println("������Ʈ");
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

		logger.info("getCalendarAll����");
		
		ArrayList<CalendarVo> clist = calendarService.calendarSelectAll(bidx);
		
		ArrayList<Map<String, Object>> events = new ArrayList<>();

        // ������ �߰�
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
