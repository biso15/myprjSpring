package com.myprj.myapp.persistance;


import com.myprj.myapp.domain.MemberVo;

// mybatis�� �޼���
public interface MemberMapper {
	
	public int memberInsert(MemberVo mv);

	public int memberIdCheck(String id);

	public MemberVo memberSelect(String id);

	public int memberUpdate(MemberVo mv);
	
}
