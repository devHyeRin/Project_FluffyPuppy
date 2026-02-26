package com.fluffypuppy.shop.service;

import com.fluffypuppy.shop.dto.OrderRequestDto;
import com.fluffypuppy.shop.dto.OrderHistDto;
import com.fluffypuppy.shop.entity.*;
import com.fluffypuppy.shop.member.entity.Member;
import com.fluffypuppy.shop.repository.ItemImgRepository;
import com.fluffypuppy.shop.repository.ItemRepository;
import com.fluffypuppy.shop.member.repository.MemberRepository;
import com.fluffypuppy.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    //단건 주문
    public Long order(OrderRequestDto orderDto, String email) {

        List<OrderRequestDto> orderDtoList = new ArrayList<>();
        orderDtoList.add(orderDto);
        return orders(orderDtoList, email);
    }

    //다건 주문
    public Long orders(List<OrderRequestDto> orderDtoList, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderRequestDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + orderDto.getItemId()));

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    //주문 목록 조회
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<Long> itemIds = new ArrayList<>();
        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Long itemId = orderItem.getItem().getId();
                if (!itemIds.contains(itemId)) {
                    itemIds.add(itemId);
                }
            }
        }

        Map<Long, String> itemImgMap = new HashMap<>();
        if (!itemIds.isEmpty()) {
            List<ItemImg> itemImgList = itemImgRepository.findByItemIdInAndRepImgYn(itemIds, "Y");

            for (ItemImg itemImg : itemImgList) {
                itemImgMap.put(itemImg.getItem().getId(), itemImg.getImgUrl());
            }
        }

        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);

            for (OrderItem orderItem : order.getOrderItems()) {
                String imgUrl = itemImgMap.get(orderItem.getItem().getId());
                OrderHistDto.OrderItemDto orderItemDto = new OrderHistDto.OrderItemDto(orderItem, imgUrl);
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<>(orderHistDtos, pageable, totalCount);
    }

    //주문 본인 확인
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문 정보를 찾을 수 없습니다."));

        return StringUtils.equals(email, order.getMember().getEmail());
    }

    //주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("취소할 주문이 존재하지 않습니다."));

        order.cancelOrder();
    }
}

