package com.example.demo.repository;

import com.example.demo.dto.CartDetailDto;
import com.example.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 장바구니 들어갈 상품 저장하거나 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // new 키워드와 해당 DTO패키지, 클래명을 적어줌 (파라미터는 DTO 클래스에 명시한 순으로 넣음)
    @Query("select new com.example.demo.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) "+
    "from CartItem ci, ItemImg im " +
    "join ci.item i "+
    "where ci.cart.id = :cartId " +
    "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
    "order by ci.regTime desc")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}
