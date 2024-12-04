package com.myprj.myapp.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myprj.myapp.domain.ReservationVo;
import com.myprj.myapp.persistance.ReservationMapper;

@Service
public class ReservationServiceImpl implements ReservationService {

	private ReservationMapper rm;
	
	@Autowired
	public ReservationServiceImpl(SqlSession sqlSession) {
		this.rm = sqlSession.getMapper(ReservationMapper.class);
	}
	
	@Override
	public int reservationInsert(ReservationVo rv){

		int value = rm.reservationInsert(rv);
		
		return value;
	};
}
