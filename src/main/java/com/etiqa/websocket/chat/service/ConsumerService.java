package com.etiqa.websocket.chat.service;

import com.etiqa.websocket.chat.model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

//    @Value("${kafka.topic.chat-topic1}")
//    private String myTopic;
//    @Value("${kafka.topic.chat-topic2}")
//    private String myTopic2;
//    private final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//
//    @KafkaListener(topics = {"${kafka.topic.chat-topic1}"}, groupId = "group1")
//    public void consumeMessage(ConsumerRecord<String, String> consumerRecord) {
//        try {
//            ChatMessage chatMessage = objectMapper.readValue(consumerRecord.value(), ChatMessage.class);
//            logger.info("Consume message of topic:{} partition:{} -> {}", consumerRecord.topic(), consumerRecord.partition(), chatMessage.toString());
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }

//    @KafkaListener(topics = {"${kafka.topic.my-topic2}"}, groupId = "group2")
//    public void consumeMessage2(ChatMessage chatMessage) {
//        logger.info("消费者消费{}的消息 -> {}", myTopic2, chatMessage.toString());
//    }
}