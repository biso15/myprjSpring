package com.myprj.myapp.service;

import java.util.ArrayList;

import com.myprj.myapp.domain.MemberVo;

// 스프링에서 (MEMBER 기능에서) 사용할 메서드를 선언하는 곳
public interface MemberService {
		
	public int memberInsert(MemberVo mv);

	public int memberIdCheck(String id);
	
	public MemberVo memberSelect(String id);

	public int memberUpdate(MemberVo mv);
		
}
