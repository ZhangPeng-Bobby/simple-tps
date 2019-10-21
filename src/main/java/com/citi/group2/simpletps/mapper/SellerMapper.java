package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.Seller;

public interface SellerMapper {
    int deleteByPrimaryKey(Integer sId);

    int insert(Seller record);

    int insertSelective(Seller record);

    Seller selectByPrimaryKey(Integer sId);

    int updateByPrimaryKeySelective(Seller record);

    int updateByPrimaryKey(Seller record);
}