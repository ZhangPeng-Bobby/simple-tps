package com.citi.group2.simpletps.service;

import com.citi.group2.simpletps.entity.Client;
import com.citi.group2.simpletps.mapper.ClientMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private ClientMapper clientMapper;

    public ClientService(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }

    public List<Client> getAllClient() {
        return clientMapper.selectAllClient();
    }
}
