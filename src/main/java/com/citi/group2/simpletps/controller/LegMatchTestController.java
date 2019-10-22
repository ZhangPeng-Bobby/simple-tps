package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.service.LegMatchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("leg-match")
public class LegMatchTestController {
    private LegMatchService legMatchService;

    public LegMatchTestController(LegMatchService legMatchService) {
        this.legMatchService = legMatchService;
    }

    @RequestMapping(value = "auto-match-sales-leg", method = RequestMethod.GET)
    public Boolean autoMatchSalesLeg(@RequestParam Integer salesLegTxnId) {
        return legMatchService.autoMatchSalesLeg(salesLegTxnId);
    }

    @RequestMapping(value = "auto-match-trader-leg", method = RequestMethod.GET)
    public Boolean autoMatchTraderLeg(@RequestParam Integer traderLegTxnId) {
        return legMatchService.autoMatchTraderLeg(traderLegTxnId);
    }

    @RequestMapping(value = "force-match", method = RequestMethod.GET)
    public Boolean forceMatch(@RequestParam Integer traderLegTxnId, @RequestParam Integer salesLegTxnId) {
        return legMatchService.forceMatch(salesLegTxnId, traderLegTxnId);
    }
}
