package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.Client;

public interface ClientMapper {
    int deleteByPrimaryKey(Integer cId);

    int insert(Client record);

    int insertSelective(Client record);

    Client selectByPrimaryKey(Integer cId);

    int updateByPrimaryKeySelective(Client record);

    int updateByPrimaryKey(Client record);
}