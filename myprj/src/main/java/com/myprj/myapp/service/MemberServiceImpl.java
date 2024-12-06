package com.myprj.myapp.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myprj.myapp.domain.MemberVo;
import com.myprj.myapp.persistance.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	private MemberMapper mm;

	@Autowired  // �������� ��ü�� ã����
	public MemberServiceImpl(SqlSession sqlSession) {
		this.mm = sqlSession.getMapper(MemberMapper.class);  // .class ������ �ִ��� Ȯ���ϹǷ�, Ȯ���ڸ� �ٿ��� ��
	}
	
	@Override
	public int memberInsert(MemberVo mv) {
		int value = mm.memberInsert(mv);
		return value;
	}

	@Override
	public int memberIdCheck(String id) {
		int value = mm.memberIdCheck(id);
		return value;
	}
	
	@Override
	public MemberVo memberSelect(String id) {
		
		MemberVo mv = mm.memberSelect(id);
		return mv;
	}

	@Override
	public int memberUpdate(MemberVo mv) {
		
		int value = mm.memberUpdate(mv);
		return value;
	}

	@Override
	public int memberDelete(MemberVo mv) {
		
		int value = mm.memberDelete(mv);
		return value;
	}

	@Override
	public String memberFindId(MemberVo mv) {

		String id = mm.memberFindId(mv);
		return id;
	};

	@Override
	public String memberFindPw(MemberVo mv) {

		String email = mm.memberFindPw(mv);
		return email;
	};

}
