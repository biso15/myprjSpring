package com.myprj.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myprj.myapp.domain.ReservationDto;
import com.myprj.myapp.domain.ReservationVo;
import com.myprj.myapp.domain.SearchCriteria;
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

	@Override
	public ArrayList<ReservationDto> reservationSelectAll(SearchCriteria scri, int midx, String adminyn) {

		HashMap<String,Object> hm = new HashMap<String,Object>();  // HashMap은 ArrayList와 비슷하지만 "이름: 값"의 형식을 가지고 있다. mybatis에서 권장함
		hm.put("startPageNum", (scri.getPage() - 1) * scri.getPerPageNum());
		hm.put("perPageNum", scri.getPerPageNum());
		hm.put("searchType", scri.getSearchType());
		hm.put("keyword", scri.getKeyword());
		hm.put("midx", midx);
		hm.put("adminyn", adminyn);
		
		ArrayList<ReservationDto> rlist = rm.reservationSelectAll(hm);
		
		return rlist;
	};

	@Override
	public int reservationTotalCount(SearchCriteria scri, int midx, String adminyn) {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("scri", scri);
		hm.put("midx", midx);
		hm.put("adminyn", adminyn);
		
		int cnt = rm.reservationTotalCount(hm);
		
		return cnt;
	};
	
	@Override
	public ReservationDto reservationSelectOne(int ridx, int cidx, int bidx) {

		ReservationDto rd = rm.reservationSelectOne(ridx, cidx, bidx);
		
		return rd;
	};
	
}
