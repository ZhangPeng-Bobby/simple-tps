package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.Seller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SellerMapper {
    int deleteByPrimaryKey(Integer sId);

    int insert(Seller record);

    int insertSelective(Seller record);

    Seller selectByPrimaryKey(Integer sId);

    int updateByPrimaryKeySelective(Seller record);

    int updateByPrimaryKey(Seller record);

    @Select({"SELECT s_id,s_name FROM seller"})
    List<Seller> selectSeller();
}