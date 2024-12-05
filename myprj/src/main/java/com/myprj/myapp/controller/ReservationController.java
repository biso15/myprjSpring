package com.myprj.myapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.CalendarVo;
import com.myprj.myapp.domain.MemberVo;
import com.myprj.myapp.domain.PageMaker;
import com.myprj.myapp.domain.ReservationDto;
import com.myprj.myapp.domain.ReservationVo;
import com.myprj.myapp.domain.SearchCriteria;
import com.myprj.myapp.service.BoardService;
import com.myprj.myapp.service.CalendarService;
import com.myprj.myapp.service.MemberService;
import com.myprj.myapp.service.ReservationService;
import com.myprj.myapp.util.UserIp;

@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/reservation")  // 중복된 주소는 위쪽에서 한번에 처리
public class ReservationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
	
	@Autowired(required=false)
	private UserIp userip;

	@Autowired(required=false)
	private MemberService memberService;
	
	@Autowired(required=false)
	private BoardService boardService;
	
	@Autowired(required=false)
	private CalendarService calendarService;

	@Autowired(required=false)
	private ReservationService reservationService;

	@Autowired(required=false)
	private PageMaker pm;  // @Component 어노테이션 사용 안할 경우, private PageMaker pm = new PageMaker(); 이렇게 사용하면 되는듯
	
	
	@RequestMapping(value="/{bidx}/reservationWrite.do")
	public String reservationWrite(
			HttpServletRequest request,
			@PathVariable("bidx") int bidx,
			Model model) {

		logger.info("reservationWrite들어옴");
		
		BoardVo bv = boardService.boardSelectOne(bidx);  // 해당되는 bidx의 게시물 데이터 가져옴
		ArrayList<CalendarVo> clist = calendarService.calendarSelectAll(bidx);
		
		String id = request.getSession().getAttribute("id").toString();
		MemberVo mv = memberService.memberSelect(id);
		
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
		model.addAttribute("clist", clist);	
		model.addAttribute("mv", mv);
		model.addAttribute("menu", menu);
		
		return "WEB-INF/board/reservationWrite";
	}	
	
	@RequestMapping(value="/{bidx}/reservationWriteAction.do")
	public String reservationWriteAction(
			@PathVariable("bidx") int bidx,
			@RequestParam("startday") String startday,
			HttpServletRequest request,
			ReservationVo rv,
			RedirectAttributes rttr) throws Exception {

		logger.info("reservationWriteAction들어옴");

		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession은 HttpServletRequest 안에 있음
		int midx_int = Integer.parseInt(midx);
		rv.setMidx(midx_int);
		
		String ip = userip.getUserIp(request);
		rv.setIp(ip);
		
		rv.setBidx(bidx);
		
		int cidx = calendarService.calendarFindIdx(rv.getBidx(), startday);
		rv.setCidx(cidx);
		
		int value = reservationService.reservationInsert(rv);
		String path = "";
		if(value == 1) {
			rttr.addFlashAttribute("msg", " 예약이 완료되었습니다.");
			path = "redirect:/reservation/reservationList.do";
		} else {
			rttr.addFlashAttribute("msg", "예약이 실패하였습니다.");
			path = "redirect:/reservation/{bidx}/reservationWrite.do";
		}
		
		return path;
	}
	
	@RequestMapping(value="/reservationList.do")
	public String reservationList(
			HttpServletRequest request,
			HttpSession session,
			SearchCriteria scri,
			Model model) {

		logger.info("reservationList들어옴");

		String adminyn = session.getAttribute("adminyn").toString();
		
		pm.setScri(scri);  // <-- PageMaker에 SearhCriteria 담아서 가지고 다닌다
		
		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession은 HttpServletRequest 안에 있음
		int midx_int = Integer.parseInt(midx);
		
		// 페이징 처리하기 위한 전체 데이터 갯수 가져오기
		int cnt = reservationService.reservationTotalCount(scri, midx_int, adminyn);
		
		pm.setTotalCount(cnt);  // <-- PageMaker에 전체게시물수를 담아서 페이지 계산
		
		String menu = "예약확인";
		
		ArrayList<ReservationDto> rlist = reservationService.reservationSelectAll(scri, midx_int, adminyn);

		model.addAttribute("rlist", rlist);	 // 화면까지 가지고 가기위해 model 객체에 담는다(redirect 사용 안하므로 Modele을 사용)
		model.addAttribute("pm", pm);  // forward 방식으로 넘기기 때문에 공유가 가능하다
		model.addAttribute("menu", menu);
		
		return "WEB-INF/board/reservationList";
	}
	
	@RequestMapping(value="/{ridx}/{cidx}/{bidx}/reservationContents.do")
	public String reservationContents(
			@PathVariable("ridx") int ridx,
			@PathVariable("cidx") int cidx,
			@PathVariable("bidx") int bidx,
			HttpServletRequest request,
			HttpSession session,
			Model model) {

		logger.info("reservationContents 들어옴");

		String adminyn = session.getAttribute("adminyn").toString();
		
		ReservationDto rd = reservationService.reservationSelectOne(ridx, cidx, bidx);
		
		String menu = "예약확인";
		String path = "";
		if(adminyn.equals("Y")) {
			path = "WEB-INF/board/reservationContentsAdmin";
		} else {
			path = "WEB-INF/board/reservationContents";
		}
		
		model.addAttribute("rd", rd);
		model.addAttribute("menu", menu);
		
		return path;
	}

	@RequestMapping(value="/{ridx}/{cidx}/{bidx}/reservationModify.do")
		public String reservationModify(
				@PathVariable("ridx") int ridx,
				@PathVariable("cidx") int cidx,
				@PathVariable("bidx") int bidx,
				HttpServletRequest request,
				HttpSession session,
				Model model) {

		logger.info("reservationModify들어옴");
		
		ReservationDto rd = reservationService.reservationSelectOne(ridx, cidx, bidx);

		String menu = "예약확인";
		
		model.addAttribute("rd", rd);
		model.addAttribute("menu", menu);
		
		return "WEB-INF/board/reservationModify";
	}
}
