package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.service.LegMatchService;
import com.citi.group2.simpletps.service.SalesLegService;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin
    @RequestMapping(value = "sales-leg", method = RequestMethod.POST)
    public String insertSalesLeg(@RequestBody SalesLeg salesLeg) {
        //insert and get the txnId
        Integer insertAns = salesLegService.insertSalesLeg(salesLeg);
        Integer txnId = salesLegService.getLastInsertId();

        //modify the salesLeg to PENDING and insert again
        SalesLeg insertedSalesLeg = salesLegService.selectNewestByTxnId(txnId);
        insertedSalesLeg.setInterVNum(insertedSalesLeg.getInterVNum() + 1);
        insertedSalesLeg.setInterId("SW" + insertedSalesLeg.getInterVNum());
        insertedSalesLeg.setStatus("PENDING");
        salesLegService.insertSalesLeg(insertedSalesLeg);

        //auto match
        legMatchService.autoMatchSalesLeg(txnId);
        return JSONObject.toJSONString(insertAns);
    }

    @RequestMapping(value = "sales-leg", method = RequestMethod.PUT)
    public String updateSalesLeg(@RequestBody SalesLeg salesLeg) {
        return JSONObject.toJSONString(salesLegService.updateSalesLeg(salesLeg));
    }

    @CrossOrigin
    @RequestMapping(value = "sales-leg", method = RequestMethod.GET)
    public String getSalesLeg() {
        List<SalesLeg> salesLegList = salesLegService.getSalesLeg();
        return JSONObject.toJSONString(salesLegList);
    }

    @CrossOrigin
    @RequestMapping(value = "newest-sales-leg", method = RequestMethod.GET)
    public String getNewestSalesLeg() {
        List<SalesLeg> salesLegList = salesLegService.getNewestSalesLeg();
        return JSONObject.toJSONString(salesLegList);
    }
}
