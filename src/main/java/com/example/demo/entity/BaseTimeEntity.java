package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.asm.Advice;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // Auditing 적용하기 위해
@MappedSuperclass // 공통 매핑 정보가 필요할때 (상속받은 자식 클래스에 매핑 정보만 제공)
@Getter @Setter
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티가 생성되고 저장될때 시간을 자동으로 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // 엔티티 값을 변경할 때 시간을 자동으로 저장
    private LocalDateTime updateTime;
}
