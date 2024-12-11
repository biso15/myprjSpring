package com.myprj.myapp.service;

import java.util.ArrayList;

import com.myprj.myapp.domain.CommentVo;

public interface CommentService {
	
	public ArrayList<CommentVo> commentSelectAll(int bidx);

	public int commentInsert(CommentVo cv);

	public int commentDelete(CommentVo cv);

	public int commentTotalCnt(int bidx);
}
