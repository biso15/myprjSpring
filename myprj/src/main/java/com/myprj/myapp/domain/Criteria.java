package com.myprj.myapp.domain;

//����¡�� �ϱ� ���ؼ� ������ �Ǵ� �����͸� ���Ŭ����
public class Criteria {
	private int page = 1;  // ������ ��ȣ�� ��� ����
	private int perPageNum = 15;  // ȭ�鿡 ����Ʈ �Խù� ��. num/page
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
	
}