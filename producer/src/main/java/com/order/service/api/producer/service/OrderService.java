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
    private ProductRepository productRepository; // Suponha que você tenha um repositório para produtos

    public Orders createOrder(Orders orders, List<Long> productIds) {
        // Defina a data de criação para o momento atual
        orders.setCreationDate(LocalDateTime.now());

        // Define o pedido como aberto
        orders.setOpen(true);

        // Calcula o valor total do pedido com base nos produtos associados
        double totalValue = calculateTotalValue(productIds);
        orders.setTotalValue(totalValue);

        // Salva o pedido no banco de dados
        Orders savedOrder = orderRepository.save(orders);

        // Associa os produtos ao pedido
        List<Product> products = productRepository.findAllById(productIds);
        savedOrder.setProducts(products);

        // Salva novamente o pedido para atualizar a associação com os produtos
        savedOrder = orderRepository.save(savedOrder);

        return savedOrder;
    }

    public Orders getOrderDetailsById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    private double calculateTotalValue(List<Long> productIds) {
        // Implemente a lógica para calcular o valor total com base nos produtos
        // Aqui, você pode consultar os produtos pelo ID e somar seus preços unitários.
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
