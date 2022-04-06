package com.etiqa.websocket.chat.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.etiqa.websocket.chat.model.Agent;
import com.etiqa.websocket.chat.model.Customer;
import com.etiqa.websocket.chat.service.AgentService;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class CustomerResolver  implements GraphQLResolver<Customer> {
    private final AgentService agentService;

    public Agent agent(Customer customer) {
        return agentService.findAgentForCustomer(customer);
    }
}

