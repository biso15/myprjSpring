package com.myprj.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myprj.myapp.domain.ReservationDto;
import com.myprj.myapp.domain.ReservationVo;

public interface ReservationMapper {

	public int reservationInsert(ReservationVo rv);

	public ArrayList<ReservationDto> reservationSelectAll(HashMap<String,Object> hm);  // HashMap�� ����ϴ� Mybatis ����. ���������� Service�� ���������� �� �޼��忡���� �޶���
	
	public int reservationTotalCount(HashMap<String,Object> hm);
	
	public ReservationDto reservationSelectOne(int ridx);

	public int reservationUpdate(ReservationVo rv);	

	public int reservationDelete(int ridx);
	
}

