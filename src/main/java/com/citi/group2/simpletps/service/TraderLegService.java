package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.mapper.SalesLegMapper;
import com.citi.group2.simpletps.mapper.TraderLegMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraderLegService {
    private TraderLegMapper traderLegMapper;
    private SalesLegMapper salesLegMapper;
    private LegMatchService legMatchService;

    public TraderLegService(TraderLegMapper traderLegMapper, SalesLegMapper salesLegMapper, LegMatchService legMatchService) {
        this.traderLegMapper = traderLegMapper;
        this.salesLegMapper = salesLegMapper;
        this.legMatchService = legMatchService;
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

    public Boolean updateMatchedTraderLeg(TraderLeg traderLeg) {
        SalesLeg salesLeg = null;
        if (null != traderLeg.getMatchedSellerLeg()) {
            salesLeg = salesLegMapper.selectNewestByTxnId(traderLeg.getMatchedSellerLeg());
        }
        if (null != salesLeg) {
            //generate new records
            traderLeg.setStatus("PROCESSED");
            traderLeg.setInterVNum(traderLeg.getInterVNum() + 1);
            traderLeg.setInterId("TW-UM" + traderLeg.getInterVNum());

            salesLeg.setStatus("PROCESSED");
            salesLeg.setInterVNum(salesLeg.getInterVNum() + 1);
            salesLeg.setInterId("TW-UM" + salesLeg.getInterVNum());
            salesLeg.setNotionalAmount(traderLeg.getNotionalAmount());
            salesLeg.setPrice(traderLeg.getPrice());

            traderLegMapper.insertSelective(traderLeg);
            salesLegMapper.insertSelective(salesLeg);

            return legMatchService.backOfficeInteraction(salesLeg.getTxnId(), traderLeg.getTxnId());
        }

        return false;
    }
}
