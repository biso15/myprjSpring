package com.myprj.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myprj.myapp.domain.BoardDto;
import com.myprj.myapp.domain.BoardVo;

// mybatis용 메서드
public interface BoardMapper {
		
	public ArrayList<BoardDto> boardSelectAll(HashMap<String,Object> hm);  // HashMap을 사용하는 Mybatis 등장. 이전까지는 Service와 유사했지만 이 메서드에서는 달라짐
	
	public int boardTotalCount(HashMap<String,Object> hm);
	
	public int boardInsert(BoardVo bv);

	public BoardVo boardSelectOne(int bidx);
		
	public int boardViewCntUpdate(int bidx);
	
	public int boardDelete(int bidx);

	public int boardUpdate(BoardVo bv);

	public int boardReplyUpdate(BoardVo bv);
	
	public int boardReplyInsert(BoardVo bv);
	
	
	
	public ArrayList<BoardVo> boardSelect(HashMap<String,Object> hm);
		
}
