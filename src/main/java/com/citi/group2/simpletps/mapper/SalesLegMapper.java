package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.SalesLegKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SalesLegMapper {
    int deleteByPrimaryKey(SalesLegKey key);

    int insert(SalesLeg record);

    int insertSelective(SalesLeg record);

    SalesLeg selectByPrimaryKey(SalesLegKey key);

    int updateByPrimaryKeySelective(SalesLeg record);

    int updateByPrimaryKeyWithBLOBs(SalesLeg record);

    int updateByPrimaryKey(SalesLeg record);

    @Select({"SELECT * FROM sales_leg"})
    List<SalesLeg> selectAllSalesLeg();

    //get the record with the biggest inter_v_num from DB
    SalesLeg selectNewestByTxnId(Integer txnId);

    //get the record with the biggest inter_v_num from DB selectively
    SalesLeg selectNewestSelective(SalesLeg salesLeg);

    @Select({"SELECT LAST_INSERT_ID()"})
    Integer getLastInsertId();

    @Select({"SELECT * FROM sales_leg WHERE txn_id=#{txn_id} ORDER BY inter_v_num"})
    List<SalesLeg> selectTxnHistory(@Param("txn_id")Integer txnId);


    @Select({"SELECT *\n" +
            "FROM sales_leg s1\n" +
            "WHERE inter_v_num = (\n" +
            "    SELECT max(inter_v_num)\n" +
            "    FROM sales_leg s2\n" +
            "    WHERE s2.txn_id = s1.txn_id);"})
    List<SalesLeg> selectNewestSalesLeg();
}