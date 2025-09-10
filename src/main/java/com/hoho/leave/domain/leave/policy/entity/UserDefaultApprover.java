package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "user_default_approver",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_uda_user_step", columnNames = {"user_id", "step_no"})
        }
)
public class UserDefaultApprover extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Column(name = "step_no", nullable = false)
    private Integer stepNo;
}
