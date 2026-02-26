package com.fluffypuppy.shop.cart.controller;

import com.fluffypuppy.shop.cart.dto.CartDetailDto;
import com.fluffypuppy.shop.cart.dto.CartItemDto;
import com.fluffypuppy.shop.cart.dto.CartOrderDto;
import com.fluffypuppy.shop.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    /**
     * 로그인된 사용자의 이메일을 추출하는 공통 메소드
     */
    private String getEmailByAuthentication(Authentication authentication) {
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            // 일반 로그인 시 이메일 추출
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            // SNS 로그인(구글, 카카오 등) 시 이메일 추출
            return ((OAuth2User) principal).getAttribute("email");
        }

        return authentication.getName();
    }

    /* 장바구니 목록 */
    @GetMapping("/cart")
    public String cartList(Model model, Authentication authentication) {

        String email = getEmailByAuthentication(authentication);

        if (email == null) {
            return "redirect:/members/login";
        }

        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(email);

        model.addAttribute("cartItems", cartDetailDtoList);
        return "cart/cartList";
    }

    /* 장바구니 담기 */
    @PostMapping("/cart")
    public @ResponseBody ResponseEntity<?> addCart(
            @RequestBody @Valid CartItemDto cartItemDto,
            BindingResult bindingResult,
            Authentication authentication) {

        String email = getEmailByAuthentication(authentication);
        if (email == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            Long cartItemId = cartService.addCart(cartItemDto, email);
            return new ResponseEntity<>(cartItemId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /* 장바구니 수량 조회 */
    @GetMapping("/cart/count")
    public @ResponseBody ResponseEntity<Integer> getCartCount(Authentication authentication) {
        String email = getEmailByAuthentication(authentication);

        if (email == null) {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }

        try {
            List<CartDetailDto> cartDetailList = cartService.getCartList(email);
            int totalCount = cartDetailList.size();

            return new ResponseEntity<>(totalCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }

    /* 장바구니 수량 수정 */
    @PatchMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity<?> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam int count,
            Authentication authentication) {

        String email = getEmailByAuthentication(authentication);
        if (email == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if (count <= 0) {
            return new ResponseEntity<>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        }

        if (!cartService.validateCartItem(cartItemId, email)) {
            return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    /* 장바구니 상품 삭제 */
    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity<?> deleteCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        String email = getEmailByAuthentication(authentication);
        if (email == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if (!cartService.validateCartItem(cartItemId, email)) {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    /* 장바구니 주문 */
    @PostMapping("/cart/orders")
    public @ResponseBody ResponseEntity<?> orderCartItem(
            @RequestBody List<CartOrderDto> cartOrderDtoList,
            Authentication authentication) {

        String email = getEmailByAuthentication(authentication);
        if (email == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        if(cartOrderDtoList == null || cartOrderDtoList.isEmpty()){
            return new ResponseEntity<>("주문할 상품을 선택해주세요.", HttpStatus.BAD_REQUEST);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), email)) {
                return new ResponseEntity<>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        try {
            Long orderId = cartService.orderCartItem(cartOrderDtoList, email);
            return new ResponseEntity<>(orderId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
