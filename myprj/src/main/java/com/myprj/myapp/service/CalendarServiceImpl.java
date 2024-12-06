package com.myprj.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myprj.myapp.domain.CalendarVo;
import com.myprj.myapp.persistance.CalendarMapper;

@Service
public class CalendarServiceImpl implements CalendarService {

	private CalendarMapper cm;
	
	@Autowired
	public CalendarServiceImpl(SqlSession sqlSession) {
		this.cm = sqlSession.getMapper(CalendarMapper.class);
	}
	
	@Override
	public ArrayList<CalendarVo> calendarSelectAll(int bidx) {
		
		ArrayList<CalendarVo> clist = cm.calendarSelectAll(bidx);
		return clist;
	};
	
	@Override
	public int calendarInsert(CalendarVo cv) {
		
		int value = cm.calendarInsert(cv);
		
		return value;
	}

	@Override
	public int calendarUpdate(CalendarVo cv) {
		
		int value = cm.calendarUpdate(cv);
		
		return value;
	}

	@Override
	public Integer calendarFindIdx(int bidx, String startday) {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("bidx", bidx);
		hm.put("startday", startday);
		
		Integer value = cm.calendarFindIdx(hm);
		
		return value;
	}

	@Override
	public int calendarDelete(CalendarVo cv) {
		
		int value = cm.calendarDelete(cv);
		
		return value;
	}
}
