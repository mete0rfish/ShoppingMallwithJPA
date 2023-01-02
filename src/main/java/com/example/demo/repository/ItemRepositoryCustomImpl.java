package com.example.demo.repository;

import com.example.demo.constant.ItemSellStatus;
import com.example.demo.dto.ItemSearchDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.QItem;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    // 2. 동적 쿼리를 생성하기 위해 JPAQueryFactory 사용
    private JPAQueryFactory queryFactory;

    // 3.JpaQueryFactory 생성자에 EntityManager 넣음
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 4. 상품 판매 상태가 전체(null)이면 null 반환 / 아니면 해당 조건의 상품만 조회
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){

        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    // 5. searchDateType을 1m으로 하면, dateTime이 한달 전으로 설정 되고, 최근 한 달 동안 등록된 상품만 등록하도록 설정
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }


    // 6.  searchBy 값이 상품명에 검색어를 포함 혹은 생성자의 아이디에 검색어를 포함하는지 구분 후 조회
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }


    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        // 7. queryFactory로 쿼리를 생성
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item) // selectForm() : 상품 데이터를 조회하기 위해 QItem의 item을 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), // where : BooleanExpression 반환하는 조건문들 넣어줌
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()) // offset : 데이터를 가지고 올 시작 인덱스 지정
                .limit(pageable.getPageSize()) // limit : 한번에 가져올 최대 개수 지정
                .fetchResults(); // fetchResults : 조회한 리스트 및 전체 개수를 포함하는 QueryResults를 반환 (상품 데이터 리스트 조회, 상품 데이터 전체 개수 조회 실행)
        List<Item> content = results.getResults();
        long total = results.getTotal();
        // 8. 조회 후 데이터를 PageImpl 객체로 반환
        return new PageImpl<>(content, pageable, total);
    }
}
