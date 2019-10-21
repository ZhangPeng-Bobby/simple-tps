package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.Seller;
import com.citi.group2.simpletps.mapper.SellerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {
    private SellerMapper sellerMapper;

    public SellerService(SellerMapper sellerMapper) {
        this.sellerMapper = sellerMapper;
    }

    public List<Seller> getAllSeller() {
        return sellerMapper.selectSeller();
    }
}
