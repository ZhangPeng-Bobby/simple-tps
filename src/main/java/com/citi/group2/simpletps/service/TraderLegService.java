package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.mapper.TraderLegMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraderLegService {
    private TraderLegMapper traderLegMapper;

    public TraderLegService(TraderLegMapper traderLegMapper) {
        this.traderLegMapper = traderLegMapper;
    }

    public List<TraderLeg> getTraderLeg(Trader trader) {
        return traderLegMapper.selectTraderLeg(trader.gettId());
    }

    public int insertTraderLeg(TraderLeg traderLeg) {
        return traderLegMapper.insertSelective(traderLeg);
    }

    public int updateTraderLeg(TraderLeg traderLeg) {
        return traderLegMapper.updateByPrimaryKeySelective(traderLeg);
    }

    public Integer getLastInsertId() {
        return traderLegMapper.getLastInsertId();
    }

    public TraderLeg selectNewestByTxnId(Integer txnId) {
        return traderLegMapper.selectNewestByTxnId(txnId);
    }

    public List<TraderLeg> getTxnHistory(Integer txnId) {
        return traderLegMapper.selectTxnHistory(txnId);
    }

    public List<TraderLeg> getNewestTraderLeg(Trader trader) {
        return traderLegMapper.selectNewest(trader.gettId());
    }
}
