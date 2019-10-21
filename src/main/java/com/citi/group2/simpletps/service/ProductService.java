package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<String> getAllCusip() {
        return productMapper.selectAllCusip();
    }
}
