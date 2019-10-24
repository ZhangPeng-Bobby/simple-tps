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

            if (1 == salesLegInserted && 1 == traderLegInserted) {
                backOfficeInteraction(queriedSalesLeg.getTxnId(), matchedTraderLeg.getTxnId());
                return true;
            }
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


            System.out.println("Auto match: sales leg " + queriedTraderLeg.getTxnId() + " is auto matched with trader leg" +
                    " " + matchedSalesLeg.getTxnId());

            if (1 == salesLegInserted && 1 == traderLegInserted) {
                backOfficeInteraction(matchedSalesLeg.getTxnId(), queriedTraderLeg.getTxnId());
                return true;
            }
        }

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

        if (1 == newSalesLegInserted && 1 == newTraderLegInserted) {
            backOfficeInteraction(newSalesLeg.getTxnId(), newTraderLeg.getTxnId());
            return true;
        } else
            return false;
    }

    /*@return return true if accepted by the back office*/
    private Boolean backOfficeInteraction(Integer salesLegTxnId, Integer traderLegTxnId) {
        SalesLeg salesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);
        TraderLeg traderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);
        Product product = productMapper.selectByPrimaryKey(salesLeg.getCusip());

        //URLs
        String backOfficeBaseAddress = "http://localhost:1111/back-office";
        String salesInputAddress = backOfficeBaseAddress + "/sales-leg";
        String traderInputAddress = backOfficeBaseAddress + "/trader-leg";
        String productInputAddress = backOfficeBaseAddress + "/product";
        String validateAddress = backOfficeBaseAddress + "/validate";

        //messages
        String salesInputResult = null, traderInputResult = null, productInputResult = null, validateResult = null;

        //invoke salesInput
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
            salesInputResult = reader.readLine();
            System.out.println("salesInputResult: " + salesInputResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //invoke traderInput
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
//            httpURLConnection.getResponseCode();
//            httpURLConnection.getResponseMessage();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            traderInputResult = reader.readLine();
            System.out.println("traderInputResult: " + traderInputResult);
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
//            httpURLConnection.getResponseCode();
//            httpURLConnection.getResponseMessage();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            productInputResult = reader.readLine();
            System.out.println("productInputResult: " + productInputResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //invoke validate
        try {
            URL productInputUrl = new URL(validateAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) productInputUrl.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            validateResult = reader.readLine();
            System.out.println("validateAddress: " + validateResult);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //check bo result and insert processed records
        if (null == salesInputResult || null == traderInputResult || null == productInputResult || null == validateResult) {
            return false;
        } else if (salesInputResult.equals("\"SALES LEG INPUT SUCCESS\"") && traderInputResult.equals("\"TRADER LEG " +
                "INPUT SUCCESS\"") && productInputResult.equals("\"PRODUCT INPUT SUCCESS\"")) {
            //insert processed sales leg
            SalesLeg processedSalesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);
            processedSalesLeg.setStatus("PROCESSED");
            processedSalesLeg.setInterVNum(processedSalesLeg.getInterVNum() + 1);
            processedSalesLeg.setInterId("BO-PC" + processedSalesLeg.getInterVNum());
            salesLegMapper.insertSelective(processedSalesLeg);

            //insert processed trader leg
            TraderLeg processedTraderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);
            processedTraderLeg.setStatus("PROCESSED");
            processedTraderLeg.setInterVNum(processedTraderLeg.getInterVNum() + 1);
            processedTraderLeg.setInterId("BO-PC" + processedTraderLeg.getInterVNum());
            traderLegMapper.insertSelective(processedTraderLeg);
        }

        if (validateResult.equals("\"ACCEPTED\"")) {//if accepted by BO
            //insert accepted sales leg
            SalesLeg acceptedSalesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);
            acceptedSalesLeg.setStatus("ACCEPTED");
            acceptedSalesLeg.setInterVNum(acceptedSalesLeg.getInterVNum() + 1);
            acceptedSalesLeg.setInterId("BO-AC" + acceptedSalesLeg.getInterVNum());
            salesLegMapper.insertSelective(acceptedSalesLeg);

            //insert accepted trader leg
            TraderLeg acceptedTraderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);
            acceptedTraderLeg.setStatus("ACCEPTED");
            acceptedTraderLeg.setInterVNum(acceptedTraderLeg.getInterVNum() + 1);
            acceptedTraderLeg.setInterId("BO-AC" + acceptedTraderLeg.getInterVNum());
            traderLegMapper.insertSelective(acceptedTraderLeg);

            //update product remaining
            Product processedProduct = productMapper.selectByPrimaryKey(salesLeg.getCusip());
            processedProduct.setRemaining(processedProduct.getRemaining() - acceptedTraderLeg.getNotionalAmount());
            productMapper.updateByPrimaryKey(processedProduct);

            return true;
        } else {//if not accepted by BO (validateResult.equals("\"NOTIONAL TOO LARGE\""))
            //insert rejected sales leg
            SalesLeg rejectedSalesLeg = salesLegMapper.selectNewestByTxnId(salesLegTxnId);
            rejectedSalesLeg.setStatus("REJECTED");
            rejectedSalesLeg.setRejectCode(validateResult);
            rejectedSalesLeg.setRejectReason(validateAddress + ": Not enough notional left to sell");
            rejectedSalesLeg.setInterVNum(rejectedSalesLeg.getInterVNum() + 1);
            rejectedSalesLeg.setInterId("BO-RJ" + rejectedSalesLeg.getInterVNum());
            salesLegMapper.insertSelective(rejectedSalesLeg);

            //insert rejected trader leg
            TraderLeg rejectedTraderLeg = traderLegMapper.selectNewestByTxnId(traderLegTxnId);
            rejectedTraderLeg.setStatus("REJECTED");
            rejectedTraderLeg.setRejectCode(validateResult);
            rejectedTraderLeg.setRejectReason(validateAddress + ": Not enough notional left to sell");
            rejectedTraderLeg.setInterVNum(rejectedTraderLeg.getInterVNum() + 1);
            rejectedTraderLeg.setInterId("BO-RJ" + rejectedTraderLeg.getInterVNum());
            traderLegMapper.insertSelective(rejectedTraderLeg);

            return true;
        }
    }
}