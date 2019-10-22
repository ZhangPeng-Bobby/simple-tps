package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.Product;
import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.service.BackOfficeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("back-office")
public class BackOfficeController {
    private BackOfficeService backOfficeService;

    public BackOfficeController(BackOfficeService backOfficeService) {
        this.backOfficeService = backOfficeService;
    }

    @RequestMapping(value = "trader-leg", method = RequestMethod.POST)
    public String inputTraderLeg(@RequestBody TraderLeg traderLeg) {
        return JSONObject.toJSONString(backOfficeService.inputTraderLeg(traderLeg));
    }

    @RequestMapping(value = "sales-leg", method = RequestMethod.POST)
    public String inputSalesLeg(@RequestBody SalesLeg salesLeg) {
        return JSONObject.toJSONString(backOfficeService.inputSalesLeg(salesLeg));
    }

    //Product input is optional.
    @RequestMapping(value = "product", method = RequestMethod.POST)
    public String inputProduct(@RequestBody Product product) {
        return JSONObject.toJSONString(backOfficeService.inputProduct(product));
    }

    @RequestMapping(value = "validate", method = RequestMethod.GET)
    public String validate() {
        return JSONObject.toJSONString(backOfficeService.validate());
    }
}
