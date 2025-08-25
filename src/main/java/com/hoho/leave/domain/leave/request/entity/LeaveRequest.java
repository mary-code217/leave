package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.config.BaseEntity;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Table(name = "leave_request")
public class LeaveRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus status;

    @Column(name = "start_day")
    private LocalDate  startDay;

    @Column(name = "end_day")
    private LocalDate endDay;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
}
