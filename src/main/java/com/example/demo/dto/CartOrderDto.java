package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

// 장바구니 페이지에서 주문할 상품 데이터를 전달
@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    // 장바구니에서 여러 개의 상품을 주문하기 때문에 자기를 List로 가짐
    private List<CartOrderDto> cartOrderDtoList;
}
