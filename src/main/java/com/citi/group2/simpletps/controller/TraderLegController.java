package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.annotation.CurrentTrader;
import com.citi.group2.simpletps.annotation.LoginRequired;
import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.service.LegMatchService;
import com.citi.group2.simpletps.service.SalesLegService;
import com.citi.group2.simpletps.service.TraderLegService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tps")
public class TraderLegController {
    private TraderLegService traderLegService;
    private LegMatchService legMatchService;

    public TraderLegController(TraderLegService traderLegService, LegMatchService legMatchService) {
        this.traderLegService = traderLegService;
        this.legMatchService = legMatchService;
    }

    @CrossOrigin
    @LoginRequired
    @RequestMapping(value = "trader-leg", method = RequestMethod.POST)
    public String insertTraderLeg(@CurrentTrader Trader trader, @RequestBody TraderLeg traderLeg) {
        //insert and get the txnId
        traderLeg.settId(trader.gettId());
        Integer insertAns = traderLegService.insertTraderLeg(traderLeg);
        Integer txnId = traderLegService.getLastInsertId();

        //modify the traderLeg to PENDING and insert again if the txn is newly generated
        if (traderLeg.getStatus().equals("REQUESTED")) {
            System.out.println("inserted record has the status of REQUESTED, inserting a PENDING record as well");
            TraderLeg insertedTraderLeg = traderLegService.selectNewestByTxnId(txnId);
            insertedTraderLeg.setInterVNum(insertedTraderLeg.getInterVNum() + 1);
            insertedTraderLeg.setInterId("SW" + insertedTraderLeg.getInterVNum());
            insertedTraderLeg.setStatus("PENDING");
            traderLegService.insertTraderLeg(insertedTraderLeg);
        }

        //auto match
        if (legMatchService.autoMatchTraderLeg(txnId)) {
            return JSONObject.toJSONString(2);
        } else
            return JSONObject.toJSONString(1);
    }

    @LoginRequired
    @RequestMapping(value = "trader-leg", method = RequestMethod.PUT)
    public String updateTraderLeg(@RequestBody TraderLeg traderLeg) {
        return JSONObject.toJSONString(traderLegService.updateTraderLeg(traderLeg));
    }

    @CrossOrigin
    @LoginRequired
    @RequestMapping(value = "trader-leg", method = RequestMethod.GET)
    public String getTraderLeg(@CurrentTrader Trader trader) {
        List<TraderLeg> traderLegList = traderLegService.getTraderLeg(trader);
        return JSONObject.toJSONString(traderLegList);
    }

    @CrossOrigin
    @LoginRequired
    @RequestMapping(value = "newest-trader-leg", method = RequestMethod.GET)
    public String getNewestTraderLeg(@CurrentTrader Trader trader) {
        List<TraderLeg> traderLegList = traderLegService.getNewestTraderLeg(trader);
        return JSONObject.toJSONString(traderLegList);
    }


    @CrossOrigin
    @LoginRequired
    @RequestMapping(value = "tw-txn-history", method = RequestMethod.POST)
    public String getTxnHistory(@RequestBody Integer txnId) {
        List<TraderLeg> traderLegList = traderLegService.getTxnHistory(txnId);
        return JSONObject.toJSONString(traderLegList);
    }
}
