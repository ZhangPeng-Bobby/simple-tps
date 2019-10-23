package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.entity.TraderLegKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TraderLegMapper {
    int deleteByPrimaryKey(TraderLegKey key);

    int insert(TraderLeg record);

    int insertSelective(TraderLeg record);

    TraderLeg selectByPrimaryKey(TraderLegKey key);

    int updateByPrimaryKeySelective(TraderLeg record);

    int updateByPrimaryKeyWithBLOBs(TraderLeg record);

    int updateByPrimaryKey(TraderLeg record);

    //get the record with the biggest inter_v_num from DB
    TraderLeg selectNewestByTxnId(Integer txnId);

    //get the record with the biggest inter_v_num from DB selectively
    TraderLeg selectNewestSelective(TraderLeg traderLeg);

    @Select({"SELECT * FROM trader_leg WHERE t_id=#{t_id}"})
    List<TraderLeg> selectTraderLeg(@Param("t_id") Integer tId);

    @Select({"SELECT LAST_INSERT_ID()"})
    Integer getLastInsertId();

    @Select({"SELECT * FROM trader_leg WHERE txn_id=#{txn_id} ORDER BY inter_v_num"})
    List<TraderLeg> selectTxnHistory(@Param("txn_id")Integer txnId);

    @Select({"SELECT *\n" +
            "FROM trader_leg t1\n" +
            "WHERE t1.t_id=#{t_id} AND inter_v_num = (\n" +
            "    SELECT max(inter_v_num)\n" +
            "    FROM trader_leg t2\n" +
            "    WHERE t2.txn_id = t1.txn_id);"})
    List<TraderLeg> selectNewest(@Param("t_id") Integer tId);
}