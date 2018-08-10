package com.sojson.common.dao;

import com.sojson.common.model.UFuture;

public class SqlFutureProvider {

    public String selectByField(UFuture trade) {
        String sql = "select * from u_future where 1=1";
        if(trade.getStatus() != null){
            sql += " and status =  #{status}";
        }
        if(trade.getSymbol() != null){
            sql += " and symbol =  #{symbol}";
        }
        if(trade.getSite() != null){
            sql += " and site =  #{site}";
        }
        sql += " order by create_tm desc limit 100";
        System.out.println("sql--selectByStatus-->"+sql);
        return sql;
    }


    public String updateFieldById(UFuture trade) {
        if(trade.getId()<1)
            return null;
        String sql = "update u_future set ";
        if(trade.getStatus() != null){
            sql += " status = #{status},";
        }
        if(sql.endsWith(",")){
            sql = sql.substring(0, sql.length()-1);
        }
        sql += " where id = #{id}";
        System.out.println("sql--updateFieldById-->"+sql);
        return sql;
    }



}
