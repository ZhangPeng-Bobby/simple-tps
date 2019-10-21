package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.Seller;
import com.citi.group2.simpletps.service.SellerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tps")
public class SellerController {
    private SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @RequestMapping(value = "all-seller", method = RequestMethod.GET)
    public String getAllSeller() {
        List<Seller> sellerList = sellerService.getAllSeller();
        return JSONObject.toJSONString(sellerList);
    }
}
