package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.SalesLegKey;

public interface SalesLegMapper {
    int deleteByPrimaryKey(SalesLegKey key);

    int insert(SalesLeg record);

    int insertSelective(SalesLeg record);

    SalesLeg selectByPrimaryKey(SalesLegKey key);

    int updateByPrimaryKeySelective(SalesLeg record);

    int updateByPrimaryKeyWithBLOBs(SalesLeg record);

    int updateByPrimaryKey(SalesLeg record);
}