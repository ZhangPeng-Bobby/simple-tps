package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.annotation.LoginRequired;
import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.service.LegMatchService;
import com.citi.group2.simpletps.service.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tps")
public class TpsController {
    private TraderService traderService;
    private LegMatchService legMatchService;

    @Autowired
    public TpsController(TraderService traderService, LegMatchService legMatchService) {
        this.traderService = traderService;
        this.legMatchService = legMatchService;
    }

    @RequestMapping(value = "force-match", method = RequestMethod.GET)
    public Boolean forceMatch(@RequestParam Integer traderLegTxnId, @RequestParam Integer salesLegTxnId) {
        return legMatchService.forceMatch(salesLegTxnId, traderLegTxnId);
    }

    @CrossOrigin
    @RequestMapping(value = "trader-login", method = RequestMethod.POST)
    public Object traderLogin(@RequestBody Trader trader) {
        Trader traderInDb = traderService.findById(trader.gettId());
        JSONObject json = new JSONObject();
        if (traderInDb == null) {
            json.put("message", "user doesn't exist");
        } else if (!traderService.comparePassword(trader, traderInDb))
            json.put("message", "wrong password");
        else {
            String token = traderService.getToken(traderInDb);
            json.put("token", token);
        }
        return json;
    }

    @LoginRequired
    @GetMapping("/test")
    public Object testLog() {
        return 9 / 0;
    }
}
