package com.citi.group2.simpletps.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tps")
public class tpsController {
    @RequestMapping(value = "sales-leg", method = RequestMethod.POST)
    public void insertSalesLeg() {

    }

    @RequestMapping(value = "trader-leg", method = RequestMethod.POST)
    public void insertTraderLeg() {

    }

    @RequestMapping(value = "sales-leg", method = RequestMethod.PUT)
    public void updateSalesLeg() {

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

    @RequestMapping(value = "sales-leg", method = RequestMethod.GET)
    public void getSalesLeg() {

    }

    @RequestMapping(value = "trader-login", method = RequestMethod.POST)
    public void traderLogin() {

    }
}
