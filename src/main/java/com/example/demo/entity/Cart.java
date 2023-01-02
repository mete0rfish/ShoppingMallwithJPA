package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter @Setter
@ToString
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    // 회원 엔티티와 일대일 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id") // 매핑할 외래키 지정
    private Member member;

}
