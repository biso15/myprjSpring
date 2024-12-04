package com.myprj.myapp.service;

import java.util.ArrayList;

import com.myprj.myapp.domain.CalendarVo;

public interface CalendarService {

	public ArrayList<CalendarVo> calendarSelectAll(int bidx);

	public int calendarInsert(CalendarVo cv);

	public int calendarUpdate(CalendarVo cv);

	public Integer calendarFindIdx(int bidx, String startday);

}
