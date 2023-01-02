package com.example.demo.entity;


import com.example.demo.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // 주문 상품 엔티티와 일대다 매핑 // mappedBy로 주문 상품 엔티티에 있는 order 필드에 의해 관리 받는다고 말함
    private List<OrderItem> orderItems = new ArrayList<>(); //  하나의 주문에 여러 개의 주문 상품

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
