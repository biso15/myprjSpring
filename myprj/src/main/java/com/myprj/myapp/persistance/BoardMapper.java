package com.myprj.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myprj.myapp.domain.BoardVo;
import com.myprj.myapp.domain.SearchCriteria;

// mybatis�� �޼���
public interface BoardMapper {
		
	public ArrayList<BoardVo> boardSelectAll(HashMap<String,Object> hm);  // HashMap�� ����ϴ� Mybatis ����. ���������� Service�� ���������� �� �޼��忡���� �޶���
	
	public int boardTotalCount(HashMap<String,Object> hm);
	
	public int boardInsert(BoardVo bv);

	public int boardOriginbidxUpadte(int bidx);

	public BoardVo boardSelectOne(int bidx);
		
	public int boardViewCntUpdate(int bidx);
	
	public int boardRecomUpdate(BoardVo bv);
	
	public int boardDelete(HashMap<String,Object> hm);

	public int boardUpdate(BoardVo bv);

	public int boardReplyUpdate(BoardVo bv);
	
	public int boardReplyInsert(BoardVo bv);
	
	
	
	public ArrayList<BoardVo> boardSelect(HashMap<String,Object> hm);
		
}
