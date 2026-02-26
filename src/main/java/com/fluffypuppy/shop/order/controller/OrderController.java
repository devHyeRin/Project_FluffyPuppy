package com.fluffypuppy.shop.order.controller;

import com.fluffypuppy.shop.order.dto.OrderRequestDto;
import com.fluffypuppy.shop.order.dto.OrderHistDto;
import com.fluffypuppy.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity<?> order (@RequestBody @Valid OrderRequestDto orderDto,
                                               BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        try{
            String email = principal.getName();
            Long orderId = orderService.order(orderDto, email);
            return new ResponseEntity<>(orderId, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //주문 이력 조회
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page,
                            Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.orElse(0), 5);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderHist";
    }

    //주문 취소
    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId,
                                                       Principal principal){

        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}
