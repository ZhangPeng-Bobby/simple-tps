package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(String cusip);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(String cusip);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    @Select({"SELECT cusip FROM product"})
    List<String> selectAllCusip();
}