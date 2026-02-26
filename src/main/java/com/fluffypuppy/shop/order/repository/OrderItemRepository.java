package com.fluffypuppy.shop.order.repository;

import com.fluffypuppy.shop.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
