package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.service.LegMatchService;
import com.citi.group2.simpletps.service.SalesLegService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tps")
public class SalesLegController {
    private SalesLegService salesLegService;
    private LegMatchService legMatchService;

    public SalesLegController(SalesLegService salesLegService, LegMatchService legMatchService) {
        this.salesLegService = salesLegService;
        this.legMatchService = legMatchService;
    }

    @RequestMapping(value = "sales-leg", method = RequestMethod.POST)
    public String insertSalesLeg(@RequestBody SalesLeg salesLeg) {
        Integer insertAns = salesLegService.insertSalesLeg(salesLeg);
        Integer txnId = salesLegService.getLastInsertId();
        legMatchService.autoMatchSalesLeg(txnId);
        return JSONObject.toJSONString(insertAns);
    }

    @RequestMapping(value = "sales-leg", method = RequestMethod.PUT)
    public String updateSalesLeg(@RequestBody SalesLeg salesLeg) {
        return JSONObject.toJSONString(salesLegService.updateSalesLeg(salesLeg));
    }

    @RequestMapping(value = "sales-leg", method = RequestMethod.GET)
    public String getSalesLeg() {
        List<SalesLeg> salesLegList = salesLegService.getSalesLeg();
        return JSONObject.toJSONString(salesLegList);
    }
}
