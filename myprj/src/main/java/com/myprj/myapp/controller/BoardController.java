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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.CalendarVo;
import com.myprj.myapp.domain.MemberVo;
import com.myprj.myapp.domain.PageMaker;
import com.myprj.myapp.domain.ReservationVo;
import com.myprj.myapp.domain.SearchCriteria;
import com.myprj.myapp.service.BoardService;
import com.myprj.myapp.service.CalendarService;
import com.myprj.myapp.service.MemberService;
import com.myprj.myapp.service.ReservationService;
import com.myprj.myapp.util.MediaUtils;
import com.myprj.myapp.util.UploadFileUtiles;
import com.myprj.myapp.util.UserIp;

@Controller  // Controller ��ü�� �������
@RequestMapping(value="/board")  // �ߺ��� �ּҴ� ���ʿ��� �ѹ��� ó��
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired(required=false)  // @Autowired : Ÿ���� ���� ��ü�� ã�Ƽ� ����. required=false : ���� ���� ������ ���, null�� ����
	private BoardService boardService;
	
	@Autowired(required=false)
	private CalendarService calendarService;

	@Autowired(required=false)
	private MemberService memberService;

	@Autowired(required=false)
	private ReservationService reservationService;
	
	@Autowired(required=false)
	private PageMaker pm;  // @Component ������̼� ��� ���� ���, private PageMaker pm = new PageMaker(); �̷��� ����ϸ� �Ǵµ�
	
	@Resource(name = "uploadPath")  // @Resource : �̸��� uploadpath�� ��ü�� ã�Ƽ� ����. Bean�� �ۼ��� id�� ã�´�.
	private String uploadPath;
	
	@Autowired(required=false)
	private UserIp userip;
	
	@RequestMapping(value="/{boardcode}/{period}/boardList.do")
	public String boardList(
			@PathVariable("boardcode") String boardcode,
			@PathVariable("period") int period,
			SearchCriteria scri,
			Model model) {
		
		logger.info("boardList����");
		
		pm.setScri(scri);  // <-- PageMaker�� SearhCriteria ��Ƽ� ������ �ٴѴ�
		
		// ����¡ ó���ϱ� ���� ��ü ������ ���� ��������
		int cnt = boardService.boardTotalCount(scri, boardcode, period);
		
		pm.setTotalCount(cnt);  // <-- PageMaker�� ��ü�Խù����� ��Ƽ� ������ ���
		
		String menu = "";
		String path = "";
		if(boardcode.equals("travel")) {
			if(period == 1) {
				menu = "����ġ��";
			} else if(period == 2) {
				menu = "1��2��";
			} else if(period == 3) {
				menu = "2��3��";
			} else if(period == 4) {
				menu = "3��4��";
			}
			path = "WEB-INF/board/travelList";
		} else if(boardcode.equals("free")) {
			menu = "�����Խ���";
			path = "WEB-INF/board/boardList";
		} else if(boardcode.equals("notice")){
			menu = "��������";
			path = "WEB-INF/board/boardList";
		}
		
		ArrayList<BoardVo> blist = boardService.boardSelectAll(scri, boardcode, period);
		model.addAttribute("blist", blist);	 // ȭ����� ������ �������� model ��ü�� ��´�(redirect ��� ���ϹǷ� Modele�� ���)
		model.addAttribute("pm", pm);  // forward ������� �ѱ�� ������ ������ �����ϴ�
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
		
		logger.info("boardWrite����");

		String menu = "";
		String path = "";
		if(boardcode.equals("travel")) {
			if(period == 1) {
				menu = "����ġ��";
			} else if(period == 2) {
				menu = "1��2��";
			} else if(period == 3) {
				menu = "2��3��";
			} else if(period == 4) {
				menu = "3��4��";
			}
			path = "WEB-INF/board/travelWrite";
		} else if(boardcode.equals("free")) {
			menu = "�����Խ���";
			path = "WEB-INF/board/freeWrite";
		} else if(boardcode.equals("notice")){
			menu = "��������";
			path = "WEB-INF/board/noticeWrite";
		}

		model.addAttribute("menu", menu);
		model.addAttribute("boardcode", boardcode);
		model.addAttribute("period", period);
		
		return path;
	}

	@RequestMapping(value="/{boardcode}/{period}/boardWriteAction.do", method=RequestMethod.POST)
	public String boardWriteAction(
			@PathVariable("boardcode") String boardcode,
			@PathVariable("period") int period,
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,  // input�� name �̸��� BoardVo�� �ִ� ������Ƽ �̸��� �����ϸ� BoardVo�� ���� �Ѿ�� @RequestParam���� ���� �� �����Ƿ�, input�� name�� filename�� �ƴ� attachfile���� �Ѵ�.
			HttpServletRequest request,
			RedirectAttributes rttr,
			Model model,
			@RequestPart(name = "posterImages", required = false) MultipartFile uploadPosterImages
			) throws Exception {
		
		logger.info("boardWriteAction����");
		
		// ����÷��(�����)
		MultipartFile file = filename;
		String uploadedFileName = "";
		
		if(!file.getOriginalFilename().equals("")) {
			String uploadPath = "D:\\dev\\myprj\\myprjSpring\\myprj\\src\\main\\webapp\\resources\\boardImages\\";
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
		}
		
		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession�� HttpServletRequest �ȿ� ����
		int midx_int = Integer.parseInt(midx);
		bv.setMidx(midx_int);
		
		String ip = userip.getUserIp(request);
		bv.setIp(ip);
		
		bv.setUploadedFilename(uploadedFileName);
		// String replaceFileName = uploadedFileName.replaceAll("(\\/\\d{4}\\/\\d{2}\\/\\d{2})\\/s-", "$1/");
        // bv.setUploadedFilename(replaceFileName);
        
		int bidx = 0;
		bidx = boardService.boardInsert(bv);
				
		String path = "";
		if(bidx != 0) {			
			model.addAttribute("bidx", bidx);			
			rttr.addFlashAttribute("msg", "�۾��� ����");
			path = "redirect:/board/" + bidx + "/boardContents.do";
			
		} else {			
			model.addAttribute("boardcode", boardcode);
			model.addAttribute("period", period);			
			rttr.addFlashAttribute("msg", "�Է��� �߸��Ǿ����ϴ�.");
			path = "redirect:/board/" + boardcode + "/" + period + "/boardWrite.do";
		}
		
		return path;
	}

	@RequestMapping(value="/{bidx}/boardModify.do")
	public String boardModify(
			@PathVariable("bidx") int bidx,
			Model model) {
		
		logger.info("boardModify����");
				
		BoardVo bv = boardService.boardSelectOne(bidx);		
		model.addAttribute("bv", bv);
		
		String menu = "";
		String path = "";
		if(bv.getBoardcode().equals("travel")) {
			if(bv.getPeriod() == 1) {
				menu = "����ġ��";
			} else if(bv.getPeriod() == 2) {
				menu = "1��2��";
			} else if(bv.getPeriod() == 3) {
				menu = "2��3��";
			} else if(bv.getPeriod() == 4) {
				menu = "3��4��";
			}
			path = "WEB-INF/board/travelModify";
		} else if(bv.getBoardcode().equals("free")) {
			menu = "�����Խ���";
			path = "WEB-INF/board/freeModifys";
		} else if(bv.getBoardcode().equals("notice")){
			menu = "��������";
			path = "WEB-INF/board/noticeModify";
		}
		
		model.addAttribute("bv", bv);
		model.addAttribute("menu", menu);
		
		return path;
		
	}
	
	@RequestMapping(value="/{bidx}/boardModifyAction.do", method=RequestMethod.POST)
	public String boardModifyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,
			HttpServletRequest request,
			RedirectAttributes rttr,
			@RequestParam("isFileChange") String isFileChange
			) throws Exception {
		
		
		logger.info("boardModifyAction����");

		String uploadedFileName = "";
		if(isFileChange.equals("true")) {
			// ����÷��(�����)
			MultipartFile file = filename;
			
			if(!file.getOriginalFilename().equals("")) {
				String uploadPath = "D:\\dev\\myprj\\myprjSpring\\myprj\\src\\main\\webapp\\resources\\boardImages\\";
				uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
			}
		} else {
			
			BoardVo bvOrigin = boardService.boardSelectOne(bv.getBidx());
			uploadedFileName = bvOrigin.getThumbnail();
		}
		
		String ip = userip.getUserIp(request);
		bv.setIp(ip);
		
		bv.setThumbnail(uploadedFileName);
        
		// ���� ���ε��ϰ� upadte�� �ϱ� ���� service�� �����
		int value = boardService.boardUpdate(bv);
		
		String path = "";
		if(value == 1) {
			rttr.addFlashAttribute("msg", "�ۼ��� ����");
			path = "redirect:/board/" + bv.getBidx() + "/boardContents.do";
		} else {
			rttr.addFlashAttribute("msg", "�Է��� �߸��Ǿ����ϴ�.");
			path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
		}
			
		return path;
	}
		
	@RequestMapping(value="/imagePreview.do", method=RequestMethod.POST)
	public ResponseEntity<Map<String, String>> imagePreview(@RequestParam("upload") MultipartFile upload, HttpServletRequest request) {
		
		System.out.println("imagePreview ����");
		
		// ���� ���� ���丮. �������
		String uploadDirectory = "D:\\dev\\myprj\\myprjSpring\\myprj\\src\\main\\webapp\\resources\\ckeditor5Builder\\ckeditor5\\imagePreview\\";
		File directory = new File(uploadDirectory);
		
		// ���丮�� �������� ������ ����
		if (!directory.exists()) {
			directory.mkdirs();  // ���丮 ����
		}
		
	    String fileName = UUID.randomUUID().toString() + "_" + upload.getOriginalFilename();  // ������ ���� �̸� ����
	    File file  = new File(directory, fileName);

	    try {
	        // ������ ������ ����
	        upload.transferTo(file);
	        
	        // ���ε�� ������ URL�� ��ȯ
	        String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + 
	                "/board/displayFile.do?fileName=" + fileName + "&type=" + "preview";
	        
	        // Map<String, Object> response = new HashMap<>();
	        // response.put("uploaded", true); // ���ε� ���� ����
	        // response.put("url", fileUrl);

	        Map<String, String> response = Map.of("url", fileUrl);
	        return ResponseEntity.ok(response); // JSON �������� ��ȯ
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@RequestMapping(value="/displayFile.do", method=RequestMethod.GET)  // �����ο� ������ �Ǿ�� ��
	public ResponseEntity<byte[]> displayFile(
			@RequestParam("fileName") String fileName,
			@RequestParam("type") String type  // �̸����� �̹������� ����
			) {
		
		logger.info("displayFile����");
	
		ResponseEntity<byte[]> entity = null;  // ResponseEntity : Collectionó�� ��ü�� ��´�.
		InputStream in = null;
		
		try{
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);  // ������ Ȯ���ڸ� ����
			MediaType mType = MediaUtils.getMediaType(formatName);  // MediaUtils�� Ȯ���ڸ� �־ ������ Ÿ���� �˾Ƴ�
			
			HttpHeaders headers = new HttpHeaders();		
			System.out.println("type : " + type);
			String uploadPath = "";
			if(type.equals("preview")) {
				uploadPath = "D:\\dev\\myprj\\myprjSpring\\myprj\\src\\main\\webapp\\resources\\ckeditor5Builder\\ckeditor5\\imagePreview\\";		
			} else if(type.equals("thumbnail")) {
				uploadPath = "D:\\dev\\myprj\\myprjSpring\\myprj\\src\\main\\webapp\\resources\\boardImages\\";
			}
			
			in = new FileInputStream(uploadPath+fileName);  // ���� �б�
		
			if(mType != null){  // ������ Ÿ���� JPG, GIF, PNG �� �ϳ��� ���				
				headers.setContentType(mType);
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);  // �������� �Ű������� ���� �޾Ƽ� ����
			
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
	
	@RequestMapping(value="/{bidx}/boardContents.do")
	public String boardContents(@PathVariable("bidx") int bidx, Model model) {

		logger.info("boardContents ����");
		
		boardService.boardViewCntUpdate(bidx);  // ��ȸ�� ������Ʈ �ϱ�
		BoardVo bv = boardService.boardSelectOne(bidx);  // �ش�Ǵ� bidx�� �Խù� ������ ������
		
		String menu = "";
		String path = "";
		if(bv.getBoardcode().equals("travel")) {
			if(bv.getPeriod() == 1) {
				menu = "����ġ��";
			} else if(bv.getPeriod() == 2) {
				menu = "1��2��";
			} else if(bv.getPeriod() == 3) {
				menu = "2��3��";
			} else if(bv.getPeriod() == 4) {
				menu = "3��4��";
			}
			path = "WEB-INF/board/travelContents";
		} else if(bv.getBoardcode().equals("free")) {
			menu = "�����Խ���";
			path = "WEB-INF/board/freeContents";
		} else if(bv.getBoardcode().equals("notice")){
			menu = "��������";
			path = "WEB-INF/board/noticeContents";
		}
		
		model.addAttribute("bv", bv);
		model.addAttribute("menu", menu);
		
		return path;
	}
			
	@RequestMapping(value="boardDeleteAction.aws", method=RequestMethod.POST)
	public String boardDeleteAction(
			@RequestParam("bidx") int bidx,
			@RequestParam("password") String password,
			HttpSession session,
			RedirectAttributes rttr) {
		
		logger.info("boardDeleteAction����");		
		
		int midx = Integer.parseInt(session.getAttribute("midx").toString());
		int value = boardService.boardDelete(bidx, midx, password);

		String path = "redirect:/board/boardList.aws";
		rttr.addFlashAttribute("msg", "�ۻ��� ����");
		if(value == 0) {
			path = "redirect:/board/boardDelete.aws?bidx=" + bidx;	
			rttr.addFlashAttribute("msg", "�ۻ��� ����");
		}
		
		return path;
	}
	
	
	
//	// ������ ������ ���� ��ο� ���� �ű��
//	@RequestMapping(value="/displayFile.aws", method=RequestMethod.GET)  // �����ο� ������ �Ǿ�� ��
//	public ResponseEntity<byte[]> displayFile(
//			@RequestParam("fileName") String fileName,
//			@RequestParam(value="down", defaultValue="0") int down  // �ٿ� ������, ȭ�鿡�� �������� ����
//			) {
//
//		logger.info("displayFile����");
//		
//		ResponseEntity<byte[]> entity = null;  // ResponseEntity : Collectionó�� ��ü�� ��´�.
//		InputStream in = null;
//		
//		try{
//			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);  // ������ Ȯ���ڸ� ����
//			MediaType mType = MediaUtils.getMediaType(formatName);  // MediaUtils�� Ȯ���ڸ� �־ ������ Ÿ���� �˾Ƴ�
//			
//			HttpHeaders headers = new HttpHeaders();		
//			 
//			in = new FileInputStream(uploadPath+fileName);  // ���� �б�
//						
//			if(mType != null){  // ������ Ÿ���� JPG, GIF, PNG �� �ϳ��� ���
//				
//				if (down==1) {  // �ٿ��� �޴´�
//					fileName = fileName.substring(fileName.indexOf("_")+1);
//					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//					headers.add("Content-Disposition", "attachment; filename=\""+
//							new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");	
//					
//				}else {  // �ٿ���� �ʰ� Ÿ���� ����
//					headers.setContentType(mType);	
//				}
//				
//			}else{  // �̸����� ���� �ʰ� �ٿ��� �޴´�.
//				
//				fileName = fileName.substring(fileName.indexOf("_")+1);
//				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//				headers.add("Content-Disposition", "attachment; filename=\""+
//						new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");				
//			}
//			
//			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);  // �������� �Ű������� ���� �޾Ƽ� ����
//			
//		}catch(Exception e){
//			e.printStackTrace();
//			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
//			
//		}finally{
//			try {
//				in.close();
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//			}
//		}
//				
//		return entity;
//	}
	
		
	@RequestMapping(value="boardReply.aws", method=RequestMethod.GET)
	public String boardReply(
		@RequestParam("bidx") int bidx,
		Model model) {

		logger.info("boardReply����");
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		
		model.addAttribute("bv", bv);
		
		return "WEB-INF/board/boardReply";
	
	}

	@RequestMapping(value="boardReplyAction.aws", method=RequestMethod.POST)
	public String boardReplyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,  // input�� name �̸��� BoardVo�� �ִ� ������Ƽ �̸��� �����ϸ� BoardVo�� ���� �Ѿ�� @RequestParam���� ���� �� �����Ƿ�, input�� name�� filename�� �ƴ� attachfile���� �Ѵ�.
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		logger.info("boardReplyAction����");
		
		// ����÷��
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
			rttr.addFlashAttribute("msg", "�亯 ��� ����");
			path = "redirect:/board/boardContents.aws?bidx=" + maxBidx;
			
		} else {
			rttr.addFlashAttribute("msg", "�亯�� ��ϵ��� �ʾҽ��ϴ�.");
			path ="redirect:/board/boardReply.aws?bidx=" + bv.getBidx();
			
		}
		
		return path;
	}
}
