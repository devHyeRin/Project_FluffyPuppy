package com.fluffypuppy.shop.repository;

import com.fluffypuppy.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);

    Cart findByMemberEmail(String email);
}
