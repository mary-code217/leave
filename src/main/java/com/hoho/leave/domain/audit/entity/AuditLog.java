package com.hoho.leave.domain.audit.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "audit_log")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_name", nullable = false)
    private String actorName;   // 이름

    @Column(name = "employee_no")
    private String employeeNo;  // 사번(null 가능)

    @Column(name = "summary", nullable = false)
    private String summary;     // 한 줄 요약
}

