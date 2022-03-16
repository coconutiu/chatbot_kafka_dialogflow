package com.etiqa.websocket.chat.controller;


import com.etiqa.websocket.chat.model.ChatMessage;
import com.etiqa.websocket.chat.service.ProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
//    @Value("${kafka.topic.chat-topic1}")
//    String myTopic;
////    @Value("${kafka.topic.chat-topic2}")
////    String myTopic2;
//    private final ProducerService producer;
//    private AtomicLong atomicLong = new AtomicLong();
//
//    KafkaController(ProducerService producer) {
//        this.producer = producer;
//    }
//
//    @PostMapping
//    public void sendMessageToKafkaTopic(@RequestParam("content") String content,@RequestParam("sender") String sender) {
//        this.producer.sendMessage(myTopic, new ChatMessage(atomicLong.addAndGet(1), ChatMessage.MessageType.CHAT,content, sender));
////        this.producer.sendMessage(myTopic2, new ChatMessage(atomicLong.addAndGet(1), ChatMessage.MessageType.RESPONSE,content, sender));
//    }
}