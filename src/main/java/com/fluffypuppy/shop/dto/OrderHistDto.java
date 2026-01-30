package com.fluffypuppy.shop.dto;

import com.fluffypuppy.shop.constant.OrderStatus;
import com.fluffypuppy.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {
    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    private LocalDateTime cancelDate;

    private int totalPrice;


    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();

        if (order.getOrderStatus().isCancelled()) {
            this.cancelDate = order.getUpdateTime(); // UpdateTime 컬럼을 주문 취소 날짜로 설정
        }

        this.totalPrice = order.getTotalPrice();
    }

    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
