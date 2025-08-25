package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "user_detail_approver",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_uda_user_step", columnNames = {"user_id", "step_no"})
        }
)
public class UserDetailApprover extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                     // 대상 사용자

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;                 // 지정 결재자

    @Column(name = "step_no", nullable = false)
    private Integer stepNo;                // 1 또는 2
}
