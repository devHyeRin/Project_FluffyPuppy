package com.fluffypuppy.shop.controller;

import com.fluffypuppy.shop.dto.CartDetailDto;
import com.fluffypuppy.shop.dto.CartItemDto;
import com.fluffypuppy.shop.dto.CartOrderDto;
import com.fluffypuppy.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /* 장바구니 담기 */
    @PostMapping("/cart")
    @ResponseBody
    public ResponseEntity<?> addCart(
            @RequestBody @Valid CartItemDto cartItemDto,
            BindingResult bindingResult,
            Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = authentication.getName();

        Long cartItemId = cartService.addCart(cartItemDto, email);
        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    /* 장바구니 목록 */
    @GetMapping("/cart")
    public String cartList(Model model, Authentication authentication) {

        if (authentication == null) {
            return "redirect:/members/login";
        }

        String email = authentication.getName();
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(email);

        model.addAttribute("cartItems", cartDetailDtoList);
        return "cart/cartList";
    }

    /* 장바구니 수량 수정 */
    @PatchMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long cartItemId,
            int count,
            Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if (count <= 0) {
            return new ResponseEntity<>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        }

        if (!cartService.validateCartItem(cartItemId, authentication.getName())) {
            return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    /* 장바구니 상품 삭제 */
    @DeleteMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity<?> deleteCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if (!cartService.validateCartItem(cartItemId, authentication.getName())) {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    /* 장바구니 주문 */
    @PostMapping("/cart/orders")
    @ResponseBody
    public ResponseEntity<?> orderCartItem(
            @RequestBody CartOrderDto cartOrderDto,
            Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if (cartOrderDto.getCartOrderDtoList() == null ||
                cartOrderDto.getCartOrderDtoList().isEmpty()) {
            return new ResponseEntity<>("주문할 상품을 선택해주세요.", HttpStatus.BAD_REQUEST);
        }

        String email = authentication.getName();

        Long orderId = cartService.orderCartItem(
                cartOrderDto.getCartOrderDtoList(),
                email
        );

        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}
