package com.etiqa.websocket.chat.service;

import com.etiqa.websocket.chat.model.Customer;
import com.etiqa.websocket.chat.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }


}
