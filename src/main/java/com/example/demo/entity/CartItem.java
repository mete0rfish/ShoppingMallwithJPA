package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="cart_item")
public class CartItem {

    @Id
    @Column(name="cart_item_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 장바구니에 여러 상품
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 상품은 여러 장바구니
    @JoinColumn(name="item_id")
    private Item item;

    private int count; // 같은 상품 몇개 담을지


}
