package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "leave_type",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_leave_type_name", columnNames = "leave_name")
        }
)
@org.hibernate.annotations.Check(constraints = "char_length(leave_name) > 0")
public class LeaveType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 휴가명: 전사 유일 + NOT NULL
    @Column(name = "leave_name", nullable = false)
    private String leaveName;

    // 설명: 선택(길이 제한 추천; 길어질 수 있으면 @Lob 고려)
    @Column(name = "leave_description")
    private String leaveDescription;
}
