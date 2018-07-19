package com.sojson.common.dao;

import com.sojson.common.model.UPermission;
import com.sojson.permission.bo.UPermissionBo;

import java.util.List;
import java.util.Set;

public interface UTradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UPermission record);

    int insertSelective(UPermission record);

    UPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UPermission record);

    int updateByPrimaryKey(UPermission record);

	List<UPermissionBo> selectPermissionById(Long id);
	//根据用户ID获取权限的Set集合
	Set<String> findPermissionByUserId(Long id);
}