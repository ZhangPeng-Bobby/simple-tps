package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.mapper.SalesLegMapper;
import com.citi.group2.simpletps.mapper.TraderLegMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegMatchService {
    private TraderLegMapper traderLegMapper;
    private SalesLegMapper salesLegMapper;

    @Autowired
    public LegMatchService(TraderLegMapper traderLegMapper, SalesLegMapper salesLegMapper) {
        this.traderLegMapper = traderLegMapper;
        this.salesLegMapper = salesLegMapper;
    }

    public Boolean autoMatchSalesLeg(Integer salesLegTxnId) {
        SalesLeg queriedSalesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);

        TraderLeg generatedTradeLeg = new TraderLeg();
        TraderLeg matchedTraderLeg = null;
        if (null != queriedSalesLeg && null == queriedSalesLeg.getMatchedTraderLeg()) {
            generatedTradeLeg.setCusip(queriedSalesLeg.getCusip());
            generatedTradeLeg.setNotionalAmount(queriedSalesLeg.getNotionalAmount());
            generatedTradeLeg.setPrice(queriedSalesLeg.getPrice());
            generatedTradeLeg.setsId(queriedSalesLeg.getsId());
            generatedTradeLeg.setStatus("PENDING");
            matchedTraderLeg = traderLegMapper.selectNewestSelective(generatedTradeLeg);
        }

        if (null != matchedTraderLeg && null == matchedTraderLeg.getMatchedSellerLeg()) {
            //insert updated records
            queriedSalesLeg.setInterVNum(queriedSalesLeg.getInterVNum() + 1);
            queriedSalesLeg.setInterId("automatch");
            queriedSalesLeg.setMatchedTraderLeg(matchedTraderLeg.getTxnId());
            int salesLegInserted = salesLegMapper.insert(queriedSalesLeg);

            matchedTraderLeg.setInterVNum(matchedTraderLeg.getInterVNum() + 1);
            matchedTraderLeg.setInterId("automatch");
            matchedTraderLeg.setMatchedSellerLeg(queriedSalesLeg.getTxnId());
            int traderLegInserted = traderLegMapper.insert(matchedTraderLeg);

            System.out.println("Auto match: sales leg " + queriedSalesLeg.getTxnId() + " is auto matched with trader leg " + matchedTraderLeg.getTxnId());

            return 1 == salesLegInserted && 1 == traderLegInserted;
        }

        return false;
    }

    public Boolean autoMatchTraderLeg(Integer traderLegTxnId) {
        TraderLeg queriedTraderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);

        SalesLeg generatedSalesLeg = new SalesLeg();
        SalesLeg matchedSalesLeg = null;
        if (null != queriedTraderLeg && null == queriedTraderLeg.getMatchedSellerLeg()) {
            generatedSalesLeg.setCusip(queriedTraderLeg.getCusip());
            generatedSalesLeg.setNotionalAmount(queriedTraderLeg.getNotionalAmount());
            generatedSalesLeg.setPrice(queriedTraderLeg.getPrice());
            generatedSalesLeg.setsId(queriedTraderLeg.getsId());
            generatedSalesLeg.setStatus("PENDING");
            matchedSalesLeg = salesLegMapper.selectNewestSelective(generatedSalesLeg);
        }


        if (null != matchedSalesLeg && null == matchedSalesLeg.getMatchedTraderLeg()) {
            //insert updated records
            queriedTraderLeg.setInterVNum(queriedTraderLeg.getInterVNum() + 1);
            queriedTraderLeg.setInterId("automatch");
            queriedTraderLeg.setMatchedSellerLeg(matchedSalesLeg.getTxnId());
            int traderLegInserted = traderLegMapper.insert(queriedTraderLeg);

            matchedSalesLeg.setInterVNum(matchedSalesLeg.getInterVNum() + 1);
            matchedSalesLeg.setInterId("automatch");
            matchedSalesLeg.setMatchedTraderLeg(queriedTraderLeg.getTxnId());
            int salesLegInserted = salesLegMapper.insert(matchedSalesLeg);

            return 1 == salesLegInserted && 1 == traderLegInserted;
        }

        System.out.println("Auto match: sales leg " + queriedTraderLeg.getTxnId() + " is auto matched with trader leg" +
                " " + matchedSalesLeg.getTxnId());

        return false;
    }

    public Boolean forceMatch(Integer salesLegTxnId, Integer traderLegTxnId) {
        SalesLeg salesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);
        TraderLeg traderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);

        SalesLeg newSalesLeg = null;
        TraderLeg newTraderLeg = null;

        int newSalesLegInserted = 0, newTraderLegInserted = 0;
        if (null != salesLeg && null != traderLeg) {
            if (null == salesLeg.getMatchedTraderLeg() && null == traderLeg.getMatchedSellerLeg()) {
                //generate new legs
                newSalesLeg = salesLeg;
                newTraderLeg = traderLeg;
                newSalesLeg.setMatchedTraderLeg(traderLeg.getTxnId());
                newTraderLeg.setMatchedSellerLeg(salesLeg.getTxnId());
                newSalesLeg.setInterId("forceMatch");
                newTraderLeg.setInterId("forceMatch");
                newSalesLeg.setInterVNum(newSalesLeg.getInterVNum() + 1);
                newTraderLeg.setInterVNum(traderLeg.getInterVNum() + 1);

                //modify the generated sales leg according to the generated trader leg
                newSalesLeg.setNotionalAmount(newTraderLeg.getNotionalAmount());
                newSalesLeg.setPrice(newTraderLeg.getPrice());

                //insert the new records
                newSalesLegInserted = salesLegMapper.insertSelective(newSalesLeg);
                newTraderLegInserted = traderLegMapper.insertSelective(newTraderLeg);
            } else
                System.out.println("Force match failed because one or two of the legs is already matched");
        } else
            System.out.println("Force match failed due to invalid txnId");

        return 1 == newSalesLegInserted && 1 == newTraderLegInserted;
    }
}
