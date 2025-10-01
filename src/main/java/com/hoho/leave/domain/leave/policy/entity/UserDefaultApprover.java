package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "user_default_approver",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_uda_user_step", columnNames = {"user_id", "step_no"})
        }
)
@NoArgsConstructor
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

    @Builder
    public UserDefaultApprover(User user, User approver, Integer stepNo) {
        this.user = user;
        this.approver = approver;
        this.stepNo = stepNo;
    }

    public static UserDefaultApprover of(User user, User approver, Integer stepNo) {
        return UserDefaultApprover.builder()
                .user(user)
                .approver(approver)
                .stepNo(stepNo).build();
    }
}
