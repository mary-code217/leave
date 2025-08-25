package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.BaseEntity;
import com.hoho.leave.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user_detail_approver")
public class UserDetailApprover extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approver_id", nullable = false)
    private UserEntity approver;

    @Column(name = "level")
    private Integer level;
}
