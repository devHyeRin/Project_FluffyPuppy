package com.fluffypuppy.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    protected void setOrder(Order order) {
        this.order = order;
    }

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.count = count;
        orderItem.orderPrice = item.getPrice(); // 주문 당시 가격 저장

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice * count;
    }

    public void cancel(){
        this.getItem().addStock(count);
    }
}
