package com.myprj.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.SearchCriteria;
import com.myprj.myapp.persistance.BoardMapper;

@Service  // ������ ����
public class BoardServiceImpl implements BoardService {

	private BoardMapper bm;

	/*
	 * @Autowired SqlSession sqlSession;  // �����ڿ��� �������� �ʰ� �̷��� ������ �� ������, �Ź� sqlSession.getMapper(BoardMapper.class);�� ����ϹǷ� �ߺ� �߻�
	 */
	
	@Autowired  // �������� ��ü�� ã����
	public BoardServiceImpl(SqlSession sqlSession) {
		this.bm = sqlSession.getMapper(BoardMapper.class);  // import�� ���ϸ� BoardMapper.class�� com.myprj.myapp.persistance.BoardMapper.class��� ��� ��
	}
	
	@Override
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri, String boardcode, int period) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();  // HashMap�� ArrayList�� ��������� "�̸�: ��"�� ������ ������ �ִ�. mybatis���� ������
		hm.put("startPageNum", (scri.getPage() - 1) * scri.getPerPageNum());
		hm.put("perPageNum", scri.getPerPageNum());
		hm.put("searchType", scri.getSearchType());
		hm.put("keyword", scri.getKeyword());
		hm.put("boardcode", boardcode);
		hm.put("period", period);
		
		ArrayList<BoardVo> blist = bm.boardSelectAll(hm);
		
		return blist;
	}

	@Override
	public int boardTotalCount(SearchCriteria scri, String boardcode, int period) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("scri", scri);
		hm.put("boardcode", boardcode);
		hm.put("period", period);
		
		int cnt = bm.boardTotalCount(hm);
		
		return cnt;
	}

	@Transactional  // Ʈ�����
	@Override
	public int boardInsert(BoardVo bv) {
		
		int value = bm.boardInsert(bv);
		int maxBidx = bv.getBidx();  // selectKey �����
		int value2 = bm.boardOriginbidxUpadte(maxBidx);
		
		return value + value2;
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
	public int boardRecomUpdate(int bidx) {

		BoardVo bv = new BoardVo();
		
		bv.setBidx(bidx);

		int cnt = bm.boardRecomUpdate(bv);
		
		int recom = bv.getRecom();
				
		return recom;
	}

	@Override
	public int boardDelete(int bidx, int midx, String password) {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("bidx", bidx);
		hm.put("midx", midx);
		hm.put("password", password);
		
		int cnt = bm.boardDelete(hm);
		
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
		// ������Ʈ�ϰ� �Է��ϱ�
		int value = bm.boardReplyUpdate(bv);
		int value2 = bm.boardReplyInsert(bv);
		int maxBidx = bv.getBidx();
		
		return maxBidx;
	}
	
	
	
	
	@Override
	public ArrayList<BoardVo> boardSelect(String boardcode, int number) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("boardcode", boardcode);
		hm.put("number", number);
		
		ArrayList<BoardVo> blist = bm.boardSelect(hm);
		
		return blist;
	};
}
