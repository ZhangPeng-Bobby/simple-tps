package com.citi.group2.simpletps.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.citi.group2.simpletps.entity.Trader;
import com.citi.group2.simpletps.mapper.TraderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
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

    public String getToken(Trader trader){
        String token = "";
        try{
            token = JWT.create()
                    .withAudience(trader.gettId().toString())
                    .sign(Algorithm.HMAC256(trader.gettPwd()));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return token;
    }
}
