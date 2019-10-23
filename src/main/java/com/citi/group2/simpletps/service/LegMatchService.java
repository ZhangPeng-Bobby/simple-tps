package com.citi.group2.simpletps.service;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.Product;
import com.citi.group2.simpletps.entity.SalesLeg;
import com.citi.group2.simpletps.entity.TraderLeg;
import com.citi.group2.simpletps.mapper.ProductMapper;
import com.citi.group2.simpletps.mapper.SalesLegMapper;
import com.citi.group2.simpletps.mapper.TraderLegMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class LegMatchService {
    private TraderLegMapper traderLegMapper;
    private SalesLegMapper salesLegMapper;
    private ProductMapper productMapper;

    @Autowired
    public LegMatchService(TraderLegMapper traderLegMapper, SalesLegMapper salesLegMapper, ProductMapper productMapper) {
        this.traderLegMapper = traderLegMapper;
        this.salesLegMapper = salesLegMapper;
        this.productMapper = productMapper;
    }

    public Boolean autoMatchSalesLeg(Integer salesLegTxnId) {
        SalesLeg queriedSalesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);

        TraderLeg generatedTradeLeg = new TraderLeg();
        TraderLeg matchedTraderLeg = null;
        if (null != queriedSalesLeg && null == queriedSalesLeg.getMatchedTraderLeg()) {
            generatedTradeLeg.setCusip(queriedSalesLeg.getCusip());
            generatedTradeLeg.setNotionalAmount(queriedSalesLeg.getNotionalAmount());
            generatedTradeLeg.setPrice(queriedSalesLeg.getPrice());
            generatedTradeLeg.setsId(queriedSalesLeg.getsId());
            generatedTradeLeg.setStatus("PENDING");
            matchedTraderLeg = traderLegMapper.selectNewestSelective(generatedTradeLeg);
        }

        if (null != matchedTraderLeg && null == matchedTraderLeg.getMatchedSellerLeg()) {
            //insert updated records
            queriedSalesLeg.setInterVNum(queriedSalesLeg.getInterVNum() + 1);
            queriedSalesLeg.setInterId("SW-AM" + queriedSalesLeg.getInterVNum());
            queriedSalesLeg.setMatchedTraderLeg(matchedTraderLeg.getTxnId());
            int salesLegInserted = salesLegMapper.insert(queriedSalesLeg);

            matchedTraderLeg.setInterVNum(matchedTraderLeg.getInterVNum() + 1);
            matchedTraderLeg.setInterId("SW-AM" + matchedTraderLeg.getInterVNum());
            matchedTraderLeg.setMatchedSellerLeg(queriedSalesLeg.getTxnId());
            int traderLegInserted = traderLegMapper.insert(matchedTraderLeg);

            System.out.println("Auto match: sales leg " + queriedSalesLeg.getTxnId() + " is auto matched with trader leg " + matchedTraderLeg.getTxnId());

            return 1 == salesLegInserted && 1 == traderLegInserted;
        }

        return false;
    }

    public Boolean autoMatchTraderLeg(Integer traderLegTxnId) {
        TraderLeg queriedTraderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);

        SalesLeg generatedSalesLeg = new SalesLeg();
        SalesLeg matchedSalesLeg = null;
        if (null != queriedTraderLeg && null == queriedTraderLeg.getMatchedSellerLeg()) {
            generatedSalesLeg.setCusip(queriedTraderLeg.getCusip());
            generatedSalesLeg.setNotionalAmount(queriedTraderLeg.getNotionalAmount());
            generatedSalesLeg.setPrice(queriedTraderLeg.getPrice());
            generatedSalesLeg.setsId(queriedTraderLeg.getsId());
            generatedSalesLeg.setStatus("PENDING");
            matchedSalesLeg = salesLegMapper.selectNewestSelective(generatedSalesLeg);
        }


        if (null != matchedSalesLeg && null == matchedSalesLeg.getMatchedTraderLeg()) {
            //insert updated records
            queriedTraderLeg.setInterVNum(queriedTraderLeg.getInterVNum() + 1);
            queriedTraderLeg.setInterId("TW-AM" + queriedTraderLeg.getInterVNum());
            queriedTraderLeg.setMatchedSellerLeg(matchedSalesLeg.getTxnId());
            int traderLegInserted = traderLegMapper.insert(queriedTraderLeg);

            matchedSalesLeg.setInterVNum(matchedSalesLeg.getInterVNum() + 1);
            matchedSalesLeg.setInterId("TW-AM" + matchedSalesLeg.getInterVNum());
            matchedSalesLeg.setMatchedTraderLeg(queriedTraderLeg.getTxnId());
            int salesLegInserted = salesLegMapper.insert(matchedSalesLeg);

            return 1 == salesLegInserted && 1 == traderLegInserted;
        }

        System.out.println("Auto match: sales leg " + queriedTraderLeg.getTxnId() + " is auto matched with trader leg" +
                " " + matchedSalesLeg.getTxnId());

        return false;
    }

    public Boolean forceMatch(Integer salesLegTxnId, Integer traderLegTxnId) {
        SalesLeg salesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);
        TraderLeg traderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);

        SalesLeg newSalesLeg = null;
        TraderLeg newTraderLeg = null;

        int newSalesLegInserted = 0, newTraderLegInserted = 0;
        if (null != salesLeg && null != traderLeg) {
            if (null == salesLeg.getMatchedTraderLeg() && null == traderLeg.getMatchedSellerLeg()) {
                //generate new legs
                newSalesLeg = salesLeg;
                newTraderLeg = traderLeg;
                newSalesLeg.setMatchedTraderLeg(traderLeg.getTxnId());
                newTraderLeg.setMatchedSellerLeg(salesLeg.getTxnId());
                newSalesLeg.setInterVNum(newSalesLeg.getInterVNum() + 1);
                newTraderLeg.setInterVNum(newTraderLeg.getInterVNum() + 1);
                newSalesLeg.setInterId("TW-FM" + newSalesLeg.getInterVNum());
                newTraderLeg.setInterId("TW-FM" + newTraderLeg.getInterVNum());

                //modify the generated sales leg according to the generated trader leg
                newSalesLeg.setNotionalAmount(newTraderLeg.getNotionalAmount());
                newSalesLeg.setPrice(newTraderLeg.getPrice());

                //insert the new records
                newSalesLegInserted = salesLegMapper.insertSelective(newSalesLeg);
                newTraderLegInserted = traderLegMapper.insertSelective(newTraderLeg);
            } else
                System.out.println("Force match failed because one or two of the legs is already matched");
        } else
            System.out.println("Force match failed due to invalid txnId");

        return 1 == newSalesLegInserted && 1 == newTraderLegInserted;
    }

    public Boolean backOfficeInteraction(Integer salesLegTxnId, Integer traderLegTxnId) {
        SalesLeg salesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);
        TraderLeg traderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);
        Product product = productMapper.selectByPrimaryKey(salesLeg.getCusip());

        //URLs
        String backOfficeBaseAddress = "http://localhost:8080/back-office";
        String salesInputAddress = backOfficeBaseAddress + "/sales-leg";
        String traderInputAddress = backOfficeBaseAddress + "/trader-leg";
        String productInputAddress = backOfficeBaseAddress + "/product";
        String validateAddress = backOfficeBaseAddress + "/validate";

        //invoke traderInput
        try {
            URL salesInputUrl = new URL(salesInputAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) salesInputUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(JSONObject.toJSONString(salesLeg));
            osw.flush();
            osw.close();
            httpURLConnection.getResponseCode();
            httpURLConnection.getResponseMessage();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            System.out.println(reader.readLine());
            System.out.println(httpURLConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //invoke salesInput
        try {
            URL traderInputUrl = new URL(traderInputAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) traderInputUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(JSONObject.toJSONString(traderLeg));
            osw.flush();
            osw.close();
            httpURLConnection.getResponseCode();
            httpURLConnection.getResponseMessage();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            System.out.println(reader.readLine());
            System.out.println(httpURLConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //invoke productInput
        try {
            URL productInputUrl = new URL(productInputAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) productInputUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(JSONObject.toJSONString(product));
            osw.flush();
            osw.close();
            httpURLConnection.getResponseCode();
            httpURLConnection.getResponseMessage();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            System.out.println(reader.readLine());
            System.out.println(httpURLConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //invoke validate


        return false;
    }
}
