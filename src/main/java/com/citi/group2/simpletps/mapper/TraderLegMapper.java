package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.entity.TraderLegKey;

public interface TraderLegMapper {
    int deleteByPrimaryKey(TraderLegKey key);

    int insert(TraderLeg record);

    int insertSelective(TraderLeg record);

    TraderLeg selectByPrimaryKey(TraderLegKey key);

    int updateByPrimaryKeySelective(TraderLeg record);

    int updateByPrimaryKeyWithBLOBs(TraderLeg record);

    int updateByPrimaryKey(TraderLeg record);
}