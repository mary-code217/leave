package com.hoho.leave.domain.shared;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티의 공통 필드를 정의하는 추상 기본 엔티티.
 * 
 * JPA Auditing을 사용하여 생성일시와 수정일시를 자동으로 관리한다.
 * 
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    /** 생성 일시 */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /** 수정 일시 */
    @LastModifiedDate
    protected LocalDateTime updatedAt;

//    @CreatedBy
//    @Column(updatable = false)
//    protected Long createBy;
//
//    @LastModifiedBy
//    protected Long updateBy;
}
