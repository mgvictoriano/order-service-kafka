package com.order.service.api.consumer.repository;

import com.order.service.api.consumer.model.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends JpaRepository<OrderData, Long> {


}
