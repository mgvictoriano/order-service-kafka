package com.order.service.api.producer.service;

import com.order.service.api.producer.model.Orders;
import com.order.service.api.producer.model.Product;
import com.order.service.api.producer.repository.OrderRepository;
import com.order.service.api.producer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Orders createOrder(Orders orders, List<Long> productIds) {

        orders.setCreationDate(LocalDateTime.now());
        orders.setOpen(true);

        double totalValue = calculateTotalValue(productIds);
        orders.setTotalValue(totalValue);

        Orders savedOrder = orderRepository.save(orders);

        List<Product> products = productRepository.findAllById(productIds);
        savedOrder.setProducts(products);

        savedOrder = orderRepository.save(savedOrder);

        return savedOrder;
    }

    public Orders getOrderDetailsById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    private double calculateTotalValue(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        double totalValue = 0.0;
        for (Product product : products) {
            totalValue += product.getUnitPrice().doubleValue();
        }
        return totalValue;
    }

    public Orders updateOrder(Long idOrder, Orders updatedOrders) {
        Optional<Orders> existingOrder = orderRepository.findById(idOrder);
        if (existingOrder.isEmpty()) {
            throw new RuntimeException("Order with ID" + idOrder + " not found.");
        }
        Orders orders = existingOrder.get();
        orders.setOrderName(updatedOrders.getOrderName());
        orders.setTotalValue(updatedOrders.getTotalValue());
        orders.setOpen(updatedOrders.isOpen());

        return orderRepository.save(orders);

    }

    public Optional<Orders> getOrderById(Long idOrder) {
        return orderRepository.findById(idOrder);
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public void cancelOrder(Long idOrder) {
        orderRepository.deleteById(idOrder);
    }


}
