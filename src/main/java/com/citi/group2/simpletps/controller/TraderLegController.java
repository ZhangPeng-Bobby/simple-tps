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

    @LoginRequired
    @RequestMapping(value = "trader-leg", method = RequestMethod.POST)
    public String insertTraderLeg(@RequestBody TraderLeg traderLeg) {
        //insert and get the txnId
        Integer insertAns = traderLegService.insertTraderLeg(traderLeg);
        Integer txnId = traderLegService.getLastInsertId();

        //modify the traderLeg to PENDING and insert again
        TraderLeg insertedTraderLeg = traderLegService.selectNewestByTxnId(txnId);
        insertedTraderLeg.setInterVNum(insertedTraderLeg.getInterVNum()+1);
        insertedTraderLeg.setInterId("SW"+insertedTraderLeg.getInterVNum());
        insertedTraderLeg.setStatus("PENDING");
        traderLegService.insertTraderLeg(insertedTraderLeg);

        //auto match
        legMatchService.autoMatchTraderLeg(txnId);
        return JSONObject.toJSONString(insertAns);
    }

    @LoginRequired
    @RequestMapping(value = "trader-leg", method = RequestMethod.PUT)
    public String updateTraderLeg(@RequestBody TraderLeg traderLeg) {
        return JSONObject.toJSONString(traderLegService.updateTraderLeg(traderLeg));
    }

    @LoginRequired
    @RequestMapping(value = "trader-leg", method = RequestMethod.GET)
    public String getTraderLeg(@CurrentTrader Trader trader) {
        List<TraderLeg> traderLegList = traderLegService.getTraderLeg(trader);
        return JSONObject.toJSONString(traderLegList);
    }
}
