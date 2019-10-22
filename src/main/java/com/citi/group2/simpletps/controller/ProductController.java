package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.Product;
import com.citi.group2.simpletps.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("tps")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "all-cusip", method = RequestMethod.GET)
    public String getAllCusip() {
        List<String> cusipList = productService.getAllCusip();
        return JSONObject.toJSONString(cusipList);
    }

    @RequestMapping(value = "product", method = RequestMethod.GET)
    public String getProduct(@RequestParam String cusip) {
        return JSONObject.toJSONString(productService.getProduct(cusip));
    }
}
