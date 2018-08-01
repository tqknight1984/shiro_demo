package com.sojson.trade.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sojson.common.dao.UTradeMapper;
import com.sojson.common.model.UTrade;
import com.sojson.core.mybatis.page.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.sojson.permission.bo.UTradeBo;

@Service
public class TradeService{

	@Autowired
	UTradeMapper tradeMapper;


	public List<UTrade> selectByField(UTrade trade) {
		return tradeMapper.selectByField(trade);
	}

	public long insert(UTrade record) {
		long res =  tradeMapper.insert(record);
		System.out.println("------->"+res);
		return res;
	}
}
