package com.myprj.myapp.persistance;

import java.util.ArrayList;

import com.myprj.myapp.domain.MemberVo;

// mybatis�� �޼���
public interface MemberMapper {
	
	public int memberInsert(MemberVo mv);

	public int memberIdCheck(String memberId);

	public MemberVo memberLoginCheck(String memberId);

	public ArrayList<MemberVo> memberSelectAll();
}
