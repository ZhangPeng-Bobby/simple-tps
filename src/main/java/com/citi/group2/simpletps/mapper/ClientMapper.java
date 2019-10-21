package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ClientMapper {
    int deleteByPrimaryKey(Integer cId);

    int insert(Client record);

    int insertSelective(Client record);

    Client selectByPrimaryKey(Integer cId);

    int updateByPrimaryKeySelective(Client record);

    int updateByPrimaryKey(Client record);

    @Select({"SELECT c_id,c_name FROM client"})
    List<Client> selectAllClient();
}