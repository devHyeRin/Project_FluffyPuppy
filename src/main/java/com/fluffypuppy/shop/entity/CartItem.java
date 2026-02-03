package com.fluffypuppy.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "cart_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.cart = cart;
        cartItem.item = item;
        cartItem.count = count;
        return cartItem;
    }

    public void addCount(int count){
        if (this.count + count < 1) {
            throw new IllegalArgumentException("장바구니에는 최소 1개 이상의 상품이 담겨야 합니다.");
        }
        this.count += count;
    }

    public void updateCount(int count) {
        if (count < 1) throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        this.count = count;
    }
}
