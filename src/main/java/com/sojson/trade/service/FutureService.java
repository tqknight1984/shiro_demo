package com.sojson.trade.service;

import com.sojson.common.dao.UFutureMapper;
import com.sojson.common.model.UOptLog;
import com.sojson.common.model.UFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import com.sojson.permission.bo.UFutureBo;

@Service
public class FutureService {

	@Autowired
	UFutureMapper futureMapper;


	public List<UFuture> selectByField(UFuture trade) {
		return futureMapper.selectByField(trade);
	}

	public long insert(UFuture record) {
 		long res =  futureMapper.insert(record);
		System.out.println("--insert----->"+res);
		return res;
	}

	public int updateFieldById(UFuture record) {
		return futureMapper.updateFieldById(record);
	}

	public long insertOptLog(UOptLog record) {
		long res =  futureMapper.insertOptLog(record);
		return res;
	}
}
