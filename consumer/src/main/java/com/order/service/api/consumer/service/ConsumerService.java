package com.order.service.api.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.service.api.consumer.model.OrderData;
import com.order.service.api.consumer.repository.ConsumerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ConsumerRepository consumerRepository;


    @KafkaListener(topics = "SaveOrder", groupId = "SaveOrderService")
    public void execute(ConsumerRecord<String, String> record) {
        logger.info("Key = {}", record.key());
        logger.info("Header = {}", record.headers());
        logger.info("Partition = {}", record.partition());

        String strOrder = record.value();

        ObjectMapper mapper = new ObjectMapper();
        OrderData order;

        try {
            order = mapper.readValue(strOrder, OrderData.class);
            logger.info("Event Received = {}", order);

            save(order);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert event [data={}]", strOrder, e);
        }
    }

    private void save(OrderData orderData) {
        consumerRepository.save(orderData);
    }

    public OrderData updateOrder(Long idOrder, OrderData updatedOrder) {
        Optional<OrderData> existingOrder = consumerRepository.findById(idOrder);

        if (existingOrder.isEmpty()) {
            throw new RuntimeException("Order with ID " + idOrder + " not found.");
        }

        OrderData order = existingOrder.get();
        order.setIdOrder(updatedOrder.getIdOrder());
        order.setIdProduct(updatedOrder.getIdProduct());

        return consumerRepository.save(order);
    }


}
