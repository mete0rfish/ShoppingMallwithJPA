package com.example.demo.dto;


import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import lombok.*;

@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }


    private String itemNm;

    private int count;

    private int orderPrice;

    private String imgUrl;
}
