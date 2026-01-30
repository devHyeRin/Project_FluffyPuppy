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
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
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
    private final HttpSession httpSession;

    public Long addCart(CartItemDto cartItemDto){

        Member member = (Member) httpSession.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityExistsException::new);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){      // 카트 생성 전
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        /*카트에 담긴 아이템 추출*/
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){  // 카트에 아이템이 존재하는 경우
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }

        CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
        cartItemRepository.save(cartItem);
        return cartItem.getId();
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(){

        Member member = (Member) httpSession.getAttribute("member");
        if (member == null) {
            return new ArrayList<>();
        }

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            return new ArrayList<>();
        }

        return cartItemRepository.findCartDetailDtoList(cart.getId());
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId){
        Member curMember = (Member) httpSession.getAttribute("member");
        if (curMember == null) return false;

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember();
        return curMember.getId().equals(savedMember.getId());
    }
    /*카트 수정*/
    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }
    /*카트 삭제*/
    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }
    /*카트 상품 주문*/
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList){

        Member member = (Member) httpSession.getAttribute("member");
        if (member == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());

            orderDtoList.add(orderDto);
        }

        // ✅ email 대신 session member 사용
        Long orderId = orderService.orders(orderDtoList, member.getEmail());

        // 주문 후 장바구니 상품 삭제
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}
