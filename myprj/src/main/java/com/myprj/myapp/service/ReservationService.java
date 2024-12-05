package com.myprj.myapp.service;

import java.util.ArrayList;

import com.myprj.myapp.domain.ReservationDto;
import com.myprj.myapp.domain.ReservationVo;
import com.myprj.myapp.domain.SearchCriteria;

public interface ReservationService {

	public int reservationInsert(ReservationVo rv);
	
	public ArrayList<ReservationDto> reservationSelectAll(SearchCriteria scri, int midx, String adminyn);
	
	public int reservationTotalCount(SearchCriteria scri, int midx, String adminyn);
	
	public ReservationDto reservationSelectOne(int ridx, int cidx, int bidx);
	
	
}
