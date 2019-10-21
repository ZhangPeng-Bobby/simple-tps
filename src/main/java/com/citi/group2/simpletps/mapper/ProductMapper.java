package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.Product;

public interface ProductMapper {
    int deleteByPrimaryKey(String cusip);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(String cusip);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
}