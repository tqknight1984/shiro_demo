package com.sojson.common.dao;

import com.sojson.common.model.UOptLog;
import com.sojson.common.model.UFuture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

//import com.sojson.permission.bo.UFutureBo;

public interface UFutureMapper {
//    int deleteByPrimaryKey(Long id);
//
    @Insert("insert into u_future (symbol, order_id, price, amount, create_tm, account, site, side, type, status) values (#{symbol},#{order_id},#{price},#{amount},#{create_tm},#{account},#{site},#{side},#{type},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UFuture record);

    @Insert("insert into u_opt_log (name, create_tm, msg) values (#{name},#{create_tm},#{msg})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOptLog(UOptLog record);

//        @Select("select * from u_future where status=#{status} limit 100")
//    @Results({ @Result(id = true, column = "id", property = "id"),
//            @Result(column = "username", property = "user_name"),
//            @Result(column = "city", property = "city") })
//    @UpdateProvider(type = SqlProvider.class, method = "selectByStatus")
    @SelectProvider(type = SqlFutureProvider.class, method = "selectByField")
    List<UFuture> selectByField(UFuture trade);

    @UpdateProvider(type = SqlFutureProvider.class, method = "updateFieldById")
    int updateFieldById(UFuture record);


}


