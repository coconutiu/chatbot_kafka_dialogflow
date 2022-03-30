package com.etiqa.websocket.chat.resolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.etiqa.websocket.chat.model.Customer;
import com.etiqa.websocket.chat.service.CustomerService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    private final CustomerService customerService;


    public List<Customer> allCustomers() {
        return customerService.findAll();
    }

}
