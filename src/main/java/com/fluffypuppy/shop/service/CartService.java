package com.fluffypuppy.shop.service;

import com.fluffypuppy.shop.dto.CartDetailDto;
import com.fluffypuppy.shop.dto.CartItemDto;
import com.fluffypuppy.shop.dto.CartOrderDto;
import com.fluffypuppy.shop.dto.OrderDto;
import com.fluffypuppy.shop.entity.Cart;
import com.fluffypuppy.shop.entity.CartItem;
import com.fluffypuppy.shop.entity.Item;
import com.fluffypuppy.shop.entity.Member;
import com.fluffypuppy.shop.repository.CartItemRepository;
import com.fluffypuppy.shop.repository.CartRepository;
import com.fluffypuppy.shop.repository.ItemRepository;
import com.fluffypuppy.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    /* 장바구니 담기 */
    public Long addCart(CartItemDto cartItemDto, String email) {
        // 1. 회원 조회 (OAuth2User인 경우도 고려하여 이메일로 조회)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("상품 정보를 찾을 수 없습니다."));

        // 2. 해당 회원의 장바구니 찾기 (없으면 생성 - Get or Create 패턴)
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 3. 이미 장바구니에 담긴 상품인지 확인
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    /* 장바구니 목록 */
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) return new ArrayList<>();

        return cartItemRepository.findCartDetailDtoList(cart.getId());
    }

    /* 권한 체크 */
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        return cartItem.getCart().getMember().getId().equals(member.getId());
    }

    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    /* 장바구니 주문 */
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, member.getEmail());

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            cartItemRepository.deleteById(cartOrderDto.getCartItemId());
        }

        return orderId;
    }
}
