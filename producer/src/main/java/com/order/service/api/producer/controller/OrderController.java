package com.order.service.api.producer.controller;

import com.order.service.api.producer.model.Orders;
import com.order.service.api.producer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders orders, List<Long> products) {
        Orders createdOrders = orderService.createOrder(orders, products);
        return new ResponseEntity<>(createdOrders, HttpStatus.CREATED);
    }

    @PutMapping("/{idOrder}")
    public ResponseEntity<Orders> updateOrder(@PathVariable Long idOrder, @RequestBody Orders updatedOrders) {
        try {
            Orders updated = orderService.updateOrder(idOrder, updatedOrders);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long idOrder) {
        Optional<Orders> order = orderService.getOrderById(idOrder);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{idOrder}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long idOrder) {
        try {
            orderService.cancelOrder(idOrder);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
