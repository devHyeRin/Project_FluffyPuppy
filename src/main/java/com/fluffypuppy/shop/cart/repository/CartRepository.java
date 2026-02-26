package com.fluffypuppy.shop.cart.repository;

import com.fluffypuppy.shop.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);

    Cart findByMemberEmail(String email);
}
