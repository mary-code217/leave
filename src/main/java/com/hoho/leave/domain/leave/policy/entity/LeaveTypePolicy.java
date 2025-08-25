package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "leave_type_policy")
public class LeaveTypePolicy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leave_decrement")
    private Boolean leaveDecrement;

    @Column(name = "requires_attachment")
    private Boolean requiresAttachment;

    @Enumerated(EnumType.STRING)
    private LeaveCode leaveCode;
}
