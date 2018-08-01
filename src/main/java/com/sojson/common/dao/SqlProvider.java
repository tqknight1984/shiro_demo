package com.sojson.common.dao;

import com.sojson.common.model.UTrade;

public class SqlProvider {

    public String selectByField(UTrade trade) {
        String sql = "select * from u_trade where 1=1";
        if(trade.getStatus() != null){
            sql += " and status =  #{status}";
        }
        if(trade.getSymbol() != null){
            sql += " and symbol =  #{symbol}";
        }
        if(trade.getSite() != null){
            sql += " and site =  #{site}";
        }
        sql += " limit 100";
        System.out.println("sql--selectByStatus-->"+sql);
        return sql;
    }


}
