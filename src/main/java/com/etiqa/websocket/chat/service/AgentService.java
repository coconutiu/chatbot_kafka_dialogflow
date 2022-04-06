package com.etiqa.websocket.chat.service;


import com.etiqa.websocket.chat.model.Agent;
import com.etiqa.websocket.chat.model.Customer;
import com.etiqa.websocket.chat.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;


    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    public Agent findAgentForCustomer(Customer customer) {
        return agentRepository.findAllById(customer.getAgentid()).get(0);
    }

}
