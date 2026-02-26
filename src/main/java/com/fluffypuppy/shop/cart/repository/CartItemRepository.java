package com.fluffypuppy.shop.cart.repository;

import com.fluffypuppy.shop.cart.dto.CartDetailDto;
import com.fluffypuppy.shop.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.fluffypuppy.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci " +
            "join ci.item i " +
            "join ItemImg im on im.item.id = i.id " +
            "where ci.cart.id = :cartId " +
            "and im.repImgYn = 'Y' " +
            "order by ci.createTime desc")
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
