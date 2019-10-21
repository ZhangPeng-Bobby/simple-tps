package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.mapper.TraderMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class TraderService {
    private TraderMapper traderMapper;

    @Autowired
    public TraderService(TraderMapper traderMapper){
        this.traderMapper = traderMapper;
    }

    public Trader findById(int id){
        return traderMapper.selectByPrimaryKey(id);
    }

    public boolean comparePassword(Trader trader, Trader traderInDb){
        return trader.gettPwd().equals(traderInDb.gettPwd());
    }
}
