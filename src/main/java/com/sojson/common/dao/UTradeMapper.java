package com.sojson.common.dao;

import com.sojson.common.model.UTrade;
import org.apache.ibatis.annotations.*;
//import com.sojson.permission.bo.UTradeBo;

import java.util.List;

public interface UTradeMapper {
//    int deleteByPrimaryKey(Long id);
//
    @Insert("insert into u_trade (symbol, order_id, price, amount, create_tm, account, site, type, status) values (#{symbol},#{order_id},#{price},#{amount},#{create_tm},#{account},#{site},#{type},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UTrade record);


//        @Select("select * from u_trade where status=#{status} limit 100")
//    @Results({ @Result(id = true, column = "id", property = "id"),
//            @Result(column = "username", property = "user_name"),
//            @Result(column = "city", property = "city") })
//    @UpdateProvider(type = SqlProvider.class, method = "selectByStatus")
    @SelectProvider(type = SqlProvider.class, method = "selectByField")
    List<UTrade> selectByField(UTrade trade);

    @UpdateProvider(type = SqlProvider.class, method = "updateFieldById")
    int updateFieldById(UTrade record);

}


