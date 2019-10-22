package com.citi.group2.simpletps.mapper;

import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.entity.TraderLegKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
}