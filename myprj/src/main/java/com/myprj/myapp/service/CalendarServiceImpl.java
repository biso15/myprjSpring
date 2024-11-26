package com.myprj.myapp.service;

import java.util.ArrayList;

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

}
