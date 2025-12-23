package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 기본 결재자 엔티티.
 * 
 * 사용자별 기본 결재선 정보를 관리한다.
 * 
 */
@Entity
@Getter
@Table(
        name = "user_default_approver",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_uda_user_step", columnNames = {"user_id", "step_no"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDefaultApprover extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Column(name = "step_no", nullable = false)
    private Integer stepNo;

    /**
     * 사용자 기본 결재자를 생성한다.
     *
     * @param user 사용자
     * @param approver 결재자
     * @param stepNo 결재 단계
     * @return 생성된 기본 결재자 엔티티
     */
    public static UserDefaultApprover of(User user, User approver, Integer stepNo) {
        UserDefaultApprover defaultApprover = new UserDefaultApprover();

        defaultApprover.user = user;
        defaultApprover.approver = approver;
        defaultApprover.stepNo = stepNo;

        return defaultApprover;
    }
}
