package com.myprj.myapp.service;

import java.util.ArrayList;

import com.myprj.myapp.domain.MemberVo;

// ���������� (MEMBER ��ɿ���) ����� �޼��带 �����ϴ� ��
public interface MemberService {
		
	public int memberInsert(MemberVo mv);

	public int memberIdCheck(String id);
	
	public MemberVo memberSelect(String id);

	public int memberUpdate(MemberVo mv);
		
}
