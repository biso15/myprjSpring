package com.myprj.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myprj.myapp.domain.ReservationDto;
import com.myprj.myapp.domain.ReservationVo;

public interface ReservationMapper {

	public int reservationInsert(ReservationVo rv);

	public ArrayList<ReservationDto> reservationSelectAll(HashMap<String,Object> hm);  // HashMap을 사용하는 Mybatis 등장. 이전까지는 Service와 유사했지만 이 메서드에서는 달라짐
	
	public int reservationTotalCount(HashMap<String,Object> hm);
	
	public ReservationDto reservationSelectOne(int ridx);

	public int reservationUpdate(ReservationVo rv);	

	public int reservationDelete(int ridx);
	
}

