package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.Trader;
import org.springframework.stereotype.Repository;

@Repository("TraderMapper")
public interface TraderMapper {
    int deleteByPrimaryKey(Integer tId);

    int insert(Trader record);

    int insertSelective(Trader record);

    Trader selectByPrimaryKey(Integer tId);

    int updateByPrimaryKeySelective(Trader record);

    int updateByPrimaryKey(Trader record);
}