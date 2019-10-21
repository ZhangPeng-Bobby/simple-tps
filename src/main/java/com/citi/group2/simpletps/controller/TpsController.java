package com.citi.group2.simpletps.controller;

import com.citi.group2.simpletps.service.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@RestController
@RequestMapping("tps")
public class TpsController {

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

    @RequestMapping(value = "all-cusip", method = RequestMethod.GET)
    public void getAllCusip() {

    }

    @RequestMapping(value = "all-client", method = RequestMethod.GET)
    public void getAllClient() {

    }
}
