package com.myprj.myapp.persistance;

import java.util.ArrayList;

import com.myprj.myapp.domain.CalendarVo;

public interface CalendarMapper {

	public ArrayList<CalendarVo> calendarSelectAll(int bidx);
	
}