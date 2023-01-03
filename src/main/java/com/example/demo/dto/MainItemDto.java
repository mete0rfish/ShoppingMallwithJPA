package com.example.demo.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    // Querydsl 결과 조회시 MainItemDto 객체로 바로 받아오도록
    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price){
        this.id = id;
        this.itemDetail = itemDetail;
        this.itemNm = itemNm;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
