package com.fluffypuppy.shop.order.dto;

import com.fluffypuppy.shop.constant.OrderStatus;
import com.fluffypuppy.shop.order.entity.Order;
import com.fluffypuppy.shop.order.entity.OrderItem;
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
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        this.totalPrice = order.getTotalPrice();

        if (order.getOrderStatus().isCancelled()) {
            this.cancelDate = order.getUpdateTime();
        }
    }

    @Getter @Setter
    public static class OrderItemDto {
        private String itemNm;
        private int count;
        private int orderPrice;
        private String imgUrl;

        public OrderItemDto(OrderItem orderItem, String imgUrl) {
            this.itemNm = orderItem.getItem().getItemNm();
            this.count = orderItem.getCount();
            this.orderPrice = orderItem.getOrderPrice();
            this.imgUrl = imgUrl;
        }
    }

    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
