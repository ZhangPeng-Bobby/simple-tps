package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.SalesLeg;
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

        return false;
    }
}
