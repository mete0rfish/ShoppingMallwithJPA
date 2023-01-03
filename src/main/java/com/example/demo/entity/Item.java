package com.example.demo.entity;

import com.example.demo.constant.ItemSellStatus;
import com.example.demo.dto.ItemFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;       // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(name="price", nullable = false)
    private int price;     // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고 수량

    @Lob // BLOB, CLOB 타입 매핑
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private ItemSellStatus itemSellStatus; // 상품 판매 상태 (enum 타입 클래스)

    private LocalDateTime regTime;   // 등록 시간

    private LocalDateTime updateTime; // 수정 시간

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
}
