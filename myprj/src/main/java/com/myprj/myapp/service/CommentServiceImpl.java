package com.myprj.myapp.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myprj.myapp.domain.CommentVo;
import com.myprj.myapp.persistance.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentMapper cm;
	
	@Autowired  // �������� ��ü�� ã����
	public CommentServiceImpl(SqlSession sqlSession) {
		this.cm = sqlSession.getMapper(CommentMapper.class);
	}
	
	@Override
	public ArrayList<CommentVo> commentSelectAll(int bidx, int block) {  // Ÿ���� ���� ������ �������� �ϳ��� Ÿ��, ������ ��� ��ȣ�� ��� ����
		
		block = block * 15;
		ArrayList<CommentVo> clist = cm.commentSelectAll(bidx, block);

		return clist;
	}

	@Override
	public int commentInsert(CommentVo cv) {
			
		int value = cm.commentInsert(cv);
		
		return value;
	}

	@Override
	public int commentDelete(CommentVo cv) {

		int value = cm.commentDelete(cv);
		
		return value;
	}

	@Override
	public int commentTotalCnt(int bidx) {
		
		int cnt = cm.commentTotalCnt(bidx);
		
		return cnt;
	}
	
}
