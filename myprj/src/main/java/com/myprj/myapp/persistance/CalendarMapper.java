package com.myprj.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myprj.myapp.domain.CalendarVo;

public interface CalendarMapper {

	public ArrayList<CalendarVo> calendarSelectAll(int bidx);

	public int calendarInsert(CalendarVo cv);

	public int calendarUpdate(CalendarVo cv);

	public Integer calendarFindIdx(HashMap<String,Object> hm);
}

