package com.myprj.myapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.CalendarVo;
import com.myprj.myapp.domain.PageMaker;
import com.myprj.myapp.domain.SearchCriteria;
import com.myprj.myapp.service.BoardService;
import com.myprj.myapp.service.CalendarService;
import com.myprj.myapp.service.MemberService;
import com.myprj.myapp.util.MediaUtils;
import com.myprj.myapp.util.UploadFileUtiles;
import com.myprj.myapp.util.UserIp;



import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/board")  // 중복된 주소는 위쪽에서 한번에 처리
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired(required=false)  // @Autowired : 타입이 같은 객체를 찾아서 주입. required=false : 만약 주입 못받을 경우, null로 지정
	private BoardService boardService;
		
	@Autowired(required=false)
	private CalendarService calendarService;
	
	@Autowired(required=false)
	private PageMaker pm;  // @Component 어노테이션 사용 안할 경우, private PageMaker pm = new PageMaker(); 이렇게 사용하면 되는듯
	
	@Resource(name = "uploadPath")  // @Resource : 이름이 uploadpath인 객체를 찾아서 주입. Bean에 작성한 id를 찾는다.
	private String uploadPath;
	
	@Autowired(required=false)
	private UserIp userip;
	
	@RequestMapping(value="/{boardcode}/{period}/boardList.do")
	public String boardList(
			@PathVariable("boardcode") String boardcode,
			@PathVariable("period") int period,
			SearchCriteria scri,
			Model model) {
		
		logger.info("boardList들어옴");
		
		pm.setScri(scri);  // <-- PageMaker에 SearhCriteria 담아서 가지고 다닌다
		
		// 페이징 처리하기 위한 전체 데이터 갯수 가져오기
		int cnt = boardService.boardTotalCount(scri, boardcode, period);
		
		pm.setTotalCount(cnt);  // <-- PageMaker에 전체게시물수를 담아서 페이지 계산
		
		String menu = "";
		String path = "";
		if(boardcode.equals("travel")) {
			if(period == 1) {
				menu = "당일치기";
			} else if(period == 2) {
				menu = "1박2일";
			} else if(period == 3) {
				menu = "2박3일";
			} else if(period == 4) {
				menu = "3박4일";
			}
			path = "WEB-INF/board/travelList";
		} else if(boardcode.equals("free")) {
			menu = "자유게시판";
			path = "WEB-INF/board/boardList";
		} else if(boardcode.equals("notice")){
			menu = "공지사항";
			path = "WEB-INF/board/boardList";
		}
		
		ArrayList<BoardVo> blist = boardService.boardSelectAll(scri, boardcode, period);
		model.addAttribute("blist", blist);	 // 화면까지 가지고 가기위해 model 객체에 담는다(redirect 사용 안하므로 Modele을 사용)
		model.addAttribute("pm", pm);  // forward 방식으로 넘기기 때문에 공유가 가능하다
		model.addAttribute("menu", menu);
		model.addAttribute("boardcode", boardcode);
		model.addAttribute("period", period);
		
		return path;
	}
	
	@RequestMapping(value="/{boardcode}/{period}/boardWrite.do")
	public String boardWrite(
			@PathVariable("boardcode") String boardcode,
			@PathVariable("period") int period,
			Model model) {
		
		logger.info("boardWrite들어옴");

		String menu = "";
		String path = "";
		if(boardcode.equals("travel")) {
			if(period == 1) {
				menu = "당일치기";
			} else if(period == 2) {
				menu = "1박2일";
			} else if(period == 3) {
				menu = "2박3일";
			} else if(period == 4) {
				menu = "3박4일";
			}
			path = "WEB-INF/board/travelWrite";
		} else if(boardcode.equals("free")) {
			menu = "자유게시판";
			path = "WEB-INF/board/freeWrite";
		} else if(boardcode.equals("notice")){
			menu = "공지사항";
			path = "WEB-INF/board/noticeWrite";
		}

		model.addAttribute("menu", menu);
		model.addAttribute("boardcode", boardcode);
		model.addAttribute("period", period);
		
		return path;
	}

	@RequestMapping(value="/${boardcode}/${period}/boardWriteAction.do", method=RequestMethod.POST)
	public String boardWriteAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,  // input의 name 이름이 BoardVo에 있는 프로퍼티 이름과 동일하면 BoardVo로 값이 넘어가서 @RequestParam으로 받을 수 없으므로, input의 name을 filename이 아닌 attachfile으로 한다.
			HttpServletRequest request,
			RedirectAttributes rttr,
			@RequestPart(name = "posterImages", required = false) MultipartFile uploadPosterImages
			) throws Exception {
		
		logger.info("boardWriteAction들어옴");
		
		// 파일첨부
		MultipartFile file = filename;
		String uploadedFileName = "";
		
		if(!file.getOriginalFilename().equals("")) {			
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
		}
		
		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession은 HttpServletRequest 안에 있음
		int midx_int = Integer.parseInt(midx);
		String ip = userip.getUserIp(request);
		
		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
		int value = boardService.boardInsert(bv);
		
		String path = "";
		if(value == 2) {
			rttr.addFlashAttribute("msg", "글쓰기 성공");
			path = "redirect:/board/travelList.do";
		} else {
			rttr.addFlashAttribute("msg", "입력이 잘못되었습니다.");
			path = "redirect:/board/travelWrite.do";
		}
		
		return path;
	}
	
	@PostMapping("/imagePreview.do")
    @ResponseBody
    public String handleFileUpload(@RequestParam("upload") MultipartFile file) throws IOException {
        // 업로드된 파일을 지정된 디렉토리에 저장
        if (file.isEmpty()) {
            return "{\"error\":\"No file uploaded\"}";
        }

        // 파일 이름 생성 (UUID 또는 원래 이름 사용 가능)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String uploadDir = "D:\\dev\\myprj\\myprjSpring\\myprj\\src\\main\\webapp\\resources\\ckeditor5\\imagePreview\\";
        Path path = Paths.get(uploadDir, fileName);

        // 파일을 디스크에 저장
        Files.createDirectories(path.getParent());  // 디렉토리 생성
        Files.write(path, file.getBytes());  // 파일 쓰기

        // 클라이언트로 URL 반환 (업로드된 파일의 상대 경로)
        return "{\"url\":\"/uploads/" + fileName + "\"}";
    }
	
	
//	@RequestMapping(value="/imagePreview.do", method=RequestMethod.POST)
//	public ResponseEntity<Map<String, Object>> imagePreview(@RequestParam("upload") MultipartFile upload) {
//		
//		System.out.println("imagePreview 들어옴");
//		
//		// 파일 저장 디렉토리. 실제경로
//		String uploadDirectory = "D:\\dev\\myprj\\myprjSpring\\myprj\\src\\main\\webapp\\resources\\ckeditor5\\imagePreview\\";
//		File directory = new File(uploadDirectory);
//		
//		// 디렉토리가 존재하지 않으면 생성
//		if (!directory.exists()) {
//			directory.mkdirs();  // 디렉토리 생성
//		}
//		
//	    String fileName = UUID.randomUUID().toString() + "_" + upload.getOriginalFilename();  // 고유한 파일 이름 생성
//	    File file  = new File(directory, fileName);
//
//	    try {
//	        // 파일을 서버에 저장
//	        upload.transferTo(file);
//
//	        // 업로드된 파일의 URL을 반환
//	        String fileUrl = "http://localhost:80/myapp/resources/ckeditor5/imagePreview/" + fileName;  // 상대 경로 또는 절대 경로로 반환
//	        
//	        Map<String, Object> response = new HashMap<>();
//	        response.put("uploaded", true); // 업로드 성공 여부
//	        response.put("url", fileUrl);
//	        
//	        return ResponseEntity.ok(response);
//	        
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	    }		 
//	}
	

//	@RequestMapping(value="boardWriteAction.aws", method=RequestMethod.POST)
//	public String boardWriteAction(
//			BoardVo bv,
//			@RequestParam("attachfile") MultipartFile filename,  // input의 name 이름이 BoardVo에 있는 프로퍼티 이름과 동일하면 BoardVo로 값이 넘어가서 @RequestParam으로 받을 수 없으므로, input의 name을 filename이 아닌 attachfile으로 한다.
//			HttpServletRequest request,
//			RedirectAttributes rttr
//			) throws Exception {
//		
//		logger.info("boardWriteAction들어옴");
//		
//		// 파일첨부
//		MultipartFile file = filename;
//		String uploadedFileName = "";
//		
//		if(!file.getOriginalFilename().equals("")) {			
//			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());  // getOriginalFilename 대.소문자 주의
//		}
//		
//		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession은 HttpServletRequest 안에 있음
//		int midx_int = Integer.parseInt(midx);  // session.getAttribute()가 String 타입일 가능성이 더 높으므로 (int)session.getAttribute("midx")로 바로 형변환 하지 않고 String으로 변환 후 int로 변환한다.
//		String ip = userip.getUserIp(request);
//		
//		bv.setUploadedFilename(uploadedFileName);
//		bv.setMidx(midx_int);
//		bv.setIp(ip);
//		
//		int value = boardService.boardInsert(bv);
//		
//		String path = "";
//		if(value == 2) {
//			rttr.addFlashAttribute("msg", "글쓰기 성공");
//			path = "redirect:/board/boardList.aws";  // redirect는 새로운 주소로 보내기 때문에 .aws도 붙여줘야 함. 주소 앞에 request.getContextPath()도 붙여야 하지만 서버에서 지원해주므로 생략
//		} else {
//			rttr.addFlashAttribute("msg", "입력이 잘못되었습니다.");
//			path = "redirect:/board/boardWrite.aws";
//		}
//		return path;
//	}
	
	@RequestMapping(value="/{bidx}/boardContents.do")
	public String boardContents(@PathVariable("bidx") int bidx, Model model) {
		
		logger.info("boardContents들어옴");
		
		boardService.boardViewCntUpdate(bidx);  // 조회수 업데이트 하기
		BoardVo bv = boardService.boardSelectOne(bidx);  // 해당되는 bidx의 게시물 데이터 가져옴
		
		String menu = "";
		String path = "";
		if(bv.getBoardcode().equals("travel")) {
			if(bv.getPeriod() == 1) {
				menu = "당일치기";
			} else if(bv.getPeriod() == 2) {
				menu = "1박2일";
			} else if(bv.getPeriod() == 3) {
				menu = "2박3일";
			} else if(bv.getPeriod() == 4) {
				menu = "3박4일";
			}
			path = "WEB-INF/board/travelContents";
		} else if(bv.getBoardcode().equals("free")) {
			menu = "자유게시판";
			path = "WEB-INF/board/freeContents";
		} else if(bv.getBoardcode().equals("notice")){
			menu = "공지사항";
			path = "WEB-INF/board/noticeContents";
		}
		
		model.addAttribute("bv", bv);
		model.addAttribute("menu", menu);
		
		return path;
	}
	
	@RequestMapping(value="/{bidx}/travelReservation.do")
	public String travelReservation(
			@PathVariable("bidx") int bidx,
			Model model) {

		logger.info("travelReservation들어옴");
		
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
		model.addAttribute("clist", clist);
		model.addAttribute("menu", menu);
		
		return "WEB-INF/board/travelReservation";
	}
	
	// 파일을 보여줄 가상 경로에 파일 옮기기
	@RequestMapping(value="/displayFile.aws", method=RequestMethod.GET)  // 가상경로와 매핑이 되어야 함
	public ResponseEntity<byte[]> displayFile(
			@RequestParam("fileName") String fileName,
			@RequestParam(value="down", defaultValue="0") int down  // 다운 받을지, 화면에서 보여줄지 선택
			) {

		logger.info("displayFile들어옴");
		
		ResponseEntity<byte[]> entity = null;  // ResponseEntity : Collection처럼 객체를 담는다.
		InputStream in = null;
		
		try{
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);  // 파일의 확장자를 꺼냄
			MediaType mType = MediaUtils.getMediaType(formatName);  // MediaUtils에 확장자를 넣어서 파일의 타입을 알아냄
			
			HttpHeaders headers = new HttpHeaders();		
			 
			in = new FileInputStream(uploadPath+fileName);  // 파일 읽기
						
			if(mType != null){  // 파일의 타입이 JPG, GIF, PNG 중 하나일 경우
				
				if (down==1) {  // 다운을 받는다
					fileName = fileName.substring(fileName.indexOf("_")+1);
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.add("Content-Disposition", "attachment; filename=\""+
							new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");	
					
				}else {  // 다운받지 않고 타입을 저장
					headers.setContentType(mType);	
				}
				
			}else{  // 미리보기 하지 않고 다운을 받는다.
				
				fileName = fileName.substring(fileName.indexOf("_")+1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; filename=\""+
						new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");				
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);  // 생성자의 매개변수에 값을 받아서 생성
			
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
			
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
				
		return entity;
	}
	
	
	
	

	@ResponseBody
	@RequestMapping(value="boardRecom.aws")
	public JSONObject boardRecom(@RequestParam("bidx") int bidx) {
		
		logger.info("boardRecom들어옴");
		
		int recom = boardService.boardRecomUpdate(bidx);

		JSONObject js = new JSONObject();

		js.put("recom", recom);
		
		return js;
	}

	@RequestMapping(value="boardDelete.aws")
	public String boardDelete(@RequestParam("bidx") int bidx, Model model) {
		
		logger.info("boardDelete들어옴");
		
		model.addAttribute("bidx", bidx);

		return "WEB-INF/board/boardDelete";
		
	}
	
	@RequestMapping(value="boardDeleteAction.aws", method=RequestMethod.POST)
	public String boardDeleteAction(
			@RequestParam("bidx") int bidx,
			@RequestParam("password") String password,
			HttpSession session,
			RedirectAttributes rttr) {
		
		logger.info("boardDeleteAction들어옴");		
		
		int midx = Integer.parseInt(session.getAttribute("midx").toString());
		int value = boardService.boardDelete(bidx, midx, password);

		String path = "redirect:/board/boardList.aws";
		rttr.addFlashAttribute("msg", "글삭제 성공");
		if(value == 0) {
			path = "redirect:/board/boardDelete.aws?bidx=" + bidx;	
			rttr.addFlashAttribute("msg", "글삭제 실패");
		}
		
		return path;
	}
	
	@RequestMapping(value="boardModify.aws", method=RequestMethod.GET)
	public String boardModify(
			@RequestParam("bidx") int bidx,
			Model model) {
		
		logger.info("boardModify들어옴");
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		
		model.addAttribute("bv", bv);
		
		return "WEB-INF/board/boardModify";
	}

	@RequestMapping(value="boardModifyAction.aws", method=RequestMethod.POST)
	public String boardModifyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,
			HttpServletRequest request,
			RedirectAttributes rttr,
			Model model,
			@RequestParam("isFileChange") String isFileChange
			) throws Exception {
		
		logger.info("boardModifyAction들어옴");
		
		BoardVo bvOrigin = boardService.boardSelectOne(bv.getBidx());

		String path = "";
		if(bvOrigin.getPassword().equals(bv.getPassword())) {
			int value = 0;

			String uploadedFileName = "";
			if(isFileChange.equals("true")) {
				// 파일첨부
				MultipartFile file = filename;
				
				if(!file.getOriginalFilename().equals("")) {			
					uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
				}
			} else {
				uploadedFileName = bvOrigin.getFilename();
			}
			
			String midx = request.getSession().getAttribute("midx").toString();
			int midx_int = Integer.parseInt(midx);
			String ip = userip.getUserIp(request);
			
			bv.setUploadedFilename(uploadedFileName);
			bv.setMidx(midx_int);
			bv.setIp(ip);
			
			// 파일 업로드하고 upadte를 하기 위한 service를 만든다
			value = boardService.boardUpdate(bv);
			
			if(value == 1) {
				rttr.addFlashAttribute("msg", "글수정 성공");
				path = "redirect:/board/boardContents.aws?bidx=" + bv.getBidx();
			} else {
				rttr.addFlashAttribute("msg", "입력이 잘못되었습니다.");
				path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
			}
			
		} else {
				
			// 비밀번호가 일치하지 않으면
			rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
			path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
		}
			
		return path;
	}
	
	@RequestMapping(value="boardReply.aws", method=RequestMethod.GET)
	public String boardReply(
		@RequestParam("bidx") int bidx,
		Model model) {

		logger.info("boardReply들어옴");
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		
		model.addAttribute("bv", bv);
		
		return "WEB-INF/board/boardReply";
	
	}

	@RequestMapping(value="boardReplyAction.aws", method=RequestMethod.POST)
	public String boardReplyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,  // input의 name 이름이 BoardVo에 있는 프로퍼티 이름과 동일하면 BoardVo로 값이 넘어가서 @RequestParam으로 받을 수 없으므로, input의 name을 filename이 아닌 attachfile으로 한다.
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		logger.info("boardReplyAction들어옴");
		
		// 파일첨부
		MultipartFile file = filename;
		String uploadedFileName = "";
		
		if(!file.getOriginalFilename().equals("")) {			
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
		}

		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx);
		String ip = userip.getUserIp(request);

		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);		
		
		int maxBidx = 0;
		maxBidx = boardService.boardReply(bv);

		String path = "";
		if (maxBidx != 0) {
			rttr.addFlashAttribute("msg", "답변 등록 성공");
			path = "redirect:/board/boardContents.aws?bidx=" + maxBidx;
			
		} else {
			rttr.addFlashAttribute("msg", "답변이 등록되지 않았습니다.");
			path ="redirect:/board/boardReply.aws?bidx=" + bv.getBidx();
			
		}
		
		return path;
	}
}
