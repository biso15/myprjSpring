package com.myprj.myapp.service;

import java.util.ArrayList;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.SearchCriteria;

public interface BoardService {

	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri, String boardcode, int period);
	
	public int boardTotalCount(SearchCriteria scri, String boardcode, int period);
	
	public int boardInsert(BoardVo bv);

	public BoardVo boardSelectOne(int bidx);
	
	public int boardViewCntUpdate(int bidx);
		
	public int boardDelete(int bidx);
	
	public int boardUpdate(BoardVo bv);
	
	public int boardReply(BoardVo bv);
	
	public ArrayList<BoardVo> boardSelect(String boardcode, int number);
	
}
