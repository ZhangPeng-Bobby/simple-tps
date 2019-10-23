package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.Product;
import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.TraderLeg;
import org.springframework.stereotype.Service;

@Service
public class BackOfficeService {
    private TraderLeg traderLeg;
    private SalesLeg salesLeg;
    private Product product;

    public String inputTraderLeg(TraderLeg traderLeg) {
        this.traderLeg = traderLeg;
        return "TRADER LEG INPUT SUCCESS";
    }

    public String inputSalesLeg(SalesLeg salesLeg) {
        this.salesLeg = salesLeg;
        return "SALES LEG INPUT SUCCESS";
    }

    public String inputProduct(Product product) {
        this.product = product;
        return "PRODUCT INPUT SUCCESS";
    }

    public String validate() {
        if (traderLeg == null) {
            return "TRADER LEG MISSING";
        } else if (salesLeg == null) {
            return "SALES LEG MISSING";
        } else if (!traderLeg.getNotionalAmount().equals(salesLeg.getNotionalAmount()) ||
                !traderLeg.getPrice().equals(salesLeg.getPrice())) {
            return "LEGS NOT MATCHING";
        } else if (product != null && traderLeg.getNotionalAmount() > product.getRemaining()) {
            return "NOTIONAL TOO LARGE";
        }
        product = null;
        return "ACCEPTED";
    }
}
