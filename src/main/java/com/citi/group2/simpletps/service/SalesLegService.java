package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.mapper.SalesLegMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesLegService {
    private SalesLegMapper salesLegMapper;

    public SalesLegService(SalesLegMapper salesLegMapper) {
        this.salesLegMapper = salesLegMapper;
    }

    public List<SalesLeg> getSalesLeg() {
        return salesLegMapper.selectAllSalesLeg();
    }

    public int insertSalesLeg(SalesLeg salesLeg) {
        return salesLegMapper.insertSelective(salesLeg);
    }

    public int updateSalesLeg(SalesLeg salesLeg) {
        return salesLegMapper.updateByPrimaryKeySelective(salesLeg);
    }
}
