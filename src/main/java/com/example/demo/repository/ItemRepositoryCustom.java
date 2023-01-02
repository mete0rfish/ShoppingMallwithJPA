package com.example.demo.repository;

import com.example.demo.dto.ItemSearchDto;
import com.example.demo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    // 상품 조회 조건을 담는 itemSearchDto, 페이징 정보를 담는 Pageable
    // Page<Item> 객체 반환
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
