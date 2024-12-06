package com.myprj.myapp.persistance;


import com.myprj.myapp.domain.MemberVo;

// mybatis용 메서드
public interface MemberMapper {
	
	public int memberInsert(MemberVo mv);

	public int memberIdCheck(String id);

	public MemberVo memberSelect(String id);

	public int memberUpdate(MemberVo mv);

	public int memberDelete(MemberVo mv);
	
	public String memberFindId(MemberVo mv);
	
	public String memberFindPw(MemberVo mv);		
	
}
