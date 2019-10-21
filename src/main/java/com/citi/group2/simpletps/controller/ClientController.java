package com.citi.group2.simpletps.controller;

import com.alibaba.fastjson.JSONObject;
import com.citi.group2.simpletps.entity.Client;
import com.citi.group2.simpletps.service.ClientService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("tps")
public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(value = "all-client", method = RequestMethod.GET)
    public String getAllClient() {
        List<Client> clientList = clientService.getAllClient();
        return JSONObject.toJSONString(clientList);
    }
}
