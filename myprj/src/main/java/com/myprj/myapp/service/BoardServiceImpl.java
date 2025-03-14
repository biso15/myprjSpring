package com.myprj.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myprj.myapp.domain.BoardDto;
import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.SearchCriteria;
import com.myprj.myapp.persistance.BoardMapper;

@Service  // 빼먹지 말자
public class BoardServiceImpl implements BoardService {

	private BoardMapper bm;

	/*
	 * @Autowired SqlSession sqlSession;  // 생성자에서 생성하지 않고 이렇게 생성할 수 있지만, 매번 sqlSession.getMapper(BoardMapper.class);를 써야하므로 중복 발생
	 */
	
	@Autowired  // 스프링아 객체를 찾아줘
	public BoardServiceImpl(SqlSession sqlSession) {
		this.bm = sqlSession.getMapper(BoardMapper.class);  // import를 안하면 BoardMapper.class를 com.myprj.myapp.persistance.BoardMapper.class라고 써야 함
	}
	
	@Override
	public ArrayList<BoardDto> boardSelectAll(SearchCriteria scri, String boardcode, int period) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();  // HashMap은 ArrayList와 비슷하지만 "이름: 값"의 형식을 가지고 있다. mybatis에서 권장함
		hm.put("startPageNum", (scri.getPage() - 1) * scri.getPerPageNum());
		hm.put("perPageNum", scri.getPerPageNum());
		hm.put("searchType", scri.getSearchType());
		hm.put("keyword", scri.getKeyword());
		hm.put("boardcode", boardcode);
		hm.put("period", period);
		
		ArrayList<BoardDto> blist = bm.boardSelectAll(hm);
		
		return blist;
	}

	@Override
	public int boardTotalCount(SearchCriteria scri, String boardcode, int period) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("searchType", scri.getSearchType());
		hm.put("keyword", scri.getKeyword());
		hm.put("boardcode", boardcode);
		hm.put("period", period);
		
		int cnt = bm.boardTotalCount(hm);
		
		return cnt;
	}

	@Override
	public int boardInsert(BoardVo bv) {
		
		int value = bm.boardInsert(bv);
		int maxBidx = bv.getBidx();  // selectKey 결과값
		
		return maxBidx;
	}

	@Override
	public BoardVo boardSelectOne(int bidx) {
		
		BoardVo bv = bm.boardSelectOne(bidx);
		
		return bv;
	}	

	@Override
	public int boardViewCntUpdate(int bidx) {
		
		int cnt = bm.boardViewCntUpdate(bidx);
		
		return cnt;
	}

	@Override
	public int boardDelete(int bidx) {
		
		int cnt = bm.boardDelete(bidx);
		
		return cnt;
		
	}

	@Override
	public int boardUpdate(BoardVo bv) {
		
		int count = bm.boardUpdate(bv);
		
		return count;
	}
	
	@Transactional
	@Override
	public int boardReply(BoardVo bv) {
		// 업데이트하고 입력하기
		int value = bm.boardReplyUpdate(bv);
		int value2 = bm.boardReplyInsert(bv);
		int maxBidx = bv.getBidx();
		
		return maxBidx;
	}
	
	@Override
	public ArrayList<BoardVo> boardSelect(String boardcode, String sortType, int number) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("boardcode", boardcode);
		hm.put("sortType", sortType);
		hm.put("number", number);
		
		ArrayList<BoardVo> blist = bm.boardSelect(hm);
		
		return blist;
	};
}
