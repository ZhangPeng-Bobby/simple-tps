package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.annotation.CurrentTrader;
import com.citi.group2.simpletps.annotation.LoginRequired;
import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.service.TraderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("tps")
public class TpsController {
    private TraderService traderService;
    @Autowired
    public TpsController(TraderService traderService){
        this.traderService = traderService;
    }

    @RequestMapping(value = "trader-leg", method = RequestMethod.POST)
    public void insertTraderLeg() {

    }

    @RequestMapping(value = "trader-leg", method = RequestMethod.PUT)
    public void updateTraderLeg() {

    }

    @RequestMapping(value = "force-match", method = RequestMethod.POST)
    public void forceMatch() {

    }

    @RequestMapping(value = "trader-leg", method = RequestMethod.GET)
    public void getTraderLeg() {

    }

    @RequestMapping(value = "trader-login", method = RequestMethod.POST)
    public Object traderLogin(@RequestBody Trader trader) {
        Trader traderInDb = traderService.findById(trader.gettId());
        JSONObject json = new JSONObject();
        if(traderInDb == null){
            json.put("message", "user doesn't exist");
        }else if(!traderService.comparePassword(trader, traderInDb))
            json.put("message", "wrong password");
        else {
            String token = traderService.getToken(traderInDb);
            json.put("token", token);
        }
        return json;
    }

    @LoginRequired
    @GetMapping("/test")
    public Object testCurrentTrader(@CurrentTrader Trader trader) {
        return trader;
    }
}
