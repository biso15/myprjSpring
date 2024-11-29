package com.myprj.myapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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



@Controller  // Controller ��ü�� �������
@RequestMapping(value="/board")  // �ߺ��� �ּҴ� ���ʿ��� �ѹ��� ó��
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired(required=false)  // @Autowired : Ÿ���� ���� ��ü�� ã�Ƽ� ����. required=false : ���� ���� ������ ���, null�� ����
	private BoardService boardService;
		
	@Autowired(required=false)
	private CalendarService calendarService;
	
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
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,  // input�� name �̸��� BoardVo�� �ִ� ������Ƽ �̸��� �����ϸ� BoardVo�� ���� �Ѿ�� @RequestParam���� ���� �� �����Ƿ�, input�� name�� filename�� �ƴ� attachfile���� �Ѵ�.
			HttpServletRequest request,
			RedirectAttributes rttr,
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
		
		//String midx = request.getSession().getAttribute("midx").toString();  // HttpSession�� HttpServletRequest �ȿ� ����
		//int midx_int = Integer.parseInt(midx);

		int midx_int = 1;
		
		String ip = userip.getUserIp(request);
		
		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
		int value = boardService.boardInsert(bv);
			
		String path = "";
		if(value == 1) {
			rttr.addFlashAttribute("msg", "�۾��� ����");
			path = "redirect:/board/{boardcode}/{period}/boardList.do";
		} else {
			rttr.addFlashAttribute("msg", "�Է��� �߸��Ǿ����ϴ�.");
			path = "redirect:/board/{boardcode}/{period}/boardWrite.do";
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
	

//	@RequestMapping(value="boardWriteAction.aws", method=RequestMethod.POST)
//	public String boardWriteAction(
//			BoardVo bv,
//			@RequestParam("attachfile") MultipartFile filename,  // input�� name �̸��� BoardVo�� �ִ� ������Ƽ �̸��� �����ϸ� BoardVo�� ���� �Ѿ�� @RequestParam���� ���� �� �����Ƿ�, input�� name�� filename�� �ƴ� attachfile���� �Ѵ�.
//			HttpServletRequest request,
//			RedirectAttributes rttr
//			) throws Exception {
//		
//		logger.info("boardWriteAction����");
//		
//		// ����÷��
//		MultipartFile file = filename;
//		String uploadedFileName = "";
//		
//		if(!file.getOriginalFilename().equals("")) {			
//			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());  // getOriginalFilename ��.�ҹ��� ����
//		}
//		
//		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession�� HttpServletRequest �ȿ� ����
//		int midx_int = Integer.parseInt(midx);  // session.getAttribute()�� String Ÿ���� ���ɼ��� �� �����Ƿ� (int)session.getAttribute("midx")�� �ٷ� ����ȯ ���� �ʰ� String���� ��ȯ �� int�� ��ȯ�Ѵ�.
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
//			rttr.addFlashAttribute("msg", "�۾��� ����");
//			path = "redirect:/board/boardList.aws";  // redirect�� ���ο� �ּҷ� ������ ������ .aws�� �ٿ���� ��. �ּ� �տ� request.getContextPath()�� �ٿ��� ������ �������� �������ֹǷ� ����
//		} else {
//			rttr.addFlashAttribute("msg", "�Է��� �߸��Ǿ����ϴ�.");
//			path = "redirect:/board/boardWrite.aws";
//		}
//		return path;
//	}
	
	@RequestMapping(value="/{bidx}/boardContents.do")
	public String boardContents(@PathVariable("bidx") int bidx, Model model) {
		
		logger.info("boardContents����");
		
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
	
	@RequestMapping(value="/{bidx}/travelReservation.do")
	public String travelReservation(
			@PathVariable("bidx") int bidx,
			Model model) {

		logger.info("travelReservation����");
		
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
		model.addAttribute("clist", clist);
		model.addAttribute("menu", menu);
		
		return "WEB-INF/board/travelReservation";
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
	
	
	
	

	@ResponseBody
	@RequestMapping(value="boardRecom.aws")
	public JSONObject boardRecom(@RequestParam("bidx") int bidx) {
		
		logger.info("boardRecom����");
		
		int recom = boardService.boardRecomUpdate(bidx);

		JSONObject js = new JSONObject();

		js.put("recom", recom);
		
		return js;
	}

	@RequestMapping(value="boardDelete.aws")
	public String boardDelete(@RequestParam("bidx") int bidx, Model model) {
		
		logger.info("boardDelete����");
		
		model.addAttribute("bidx", bidx);

		return "WEB-INF/board/boardDelete";
		
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
	
	@RequestMapping(value="boardModify.aws", method=RequestMethod.GET)
	public String boardModify(
			@RequestParam("bidx") int bidx,
			Model model) {
		
		logger.info("boardModify����");
		
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
		
		logger.info("boardModifyAction����");
		
		BoardVo bvOrigin = boardService.boardSelectOne(bv.getBidx());

		String path = "";
		if(bvOrigin.getPassword().equals(bv.getPassword())) {
			int value = 0;

			String uploadedFileName = "";
			if(isFileChange.equals("true")) {
				// ����÷��
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
			
			// ���� ���ε��ϰ� upadte�� �ϱ� ���� service�� �����
			value = boardService.boardUpdate(bv);
			
			if(value == 1) {
				rttr.addFlashAttribute("msg", "�ۼ��� ����");
				path = "redirect:/board/boardContents.aws?bidx=" + bv.getBidx();
			} else {
				rttr.addFlashAttribute("msg", "�Է��� �߸��Ǿ����ϴ�.");
				path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
			}
			
		} else {
				
			// ��й�ȣ�� ��ġ���� ������
			rttr.addFlashAttribute("msg", "��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
		}
			
		return path;
	}
	
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
