package com.order.service.api.producer.service;

import com.order.service.api.producer.model.Order;
import com.order.service.api.producer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long idOrder, Order updatedOrder) {
        Optional<Order> existingOrder = orderRepository.findById(idOrder);
        if (existingOrder.isEmpty()) {
            throw new RuntimeException("Order with ID" + idOrder + " not found.");
        }
        Order order = existingOrder.get();
        order.setOrderName(updatedOrder.getOrderName());
        order.setTotalValue(updatedOrder.getTotalValue());
        order.setOpen(updatedOrder.isOpen());

        return orderRepository.save(order);

    }

    public Optional<Order> getOrderById(Long idOrder) {
        return orderRepository.findById(idOrder);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void cancelOrder(Long idOrder) {
        orderRepository.deleteById(idOrder);
    }


}
