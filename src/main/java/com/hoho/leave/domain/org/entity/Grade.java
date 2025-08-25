package com.hoho.leave.domain.org.entity;

import com.hoho.leave.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "grade",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_grade_name", columnNames = "grade_name"),
        }
)
public class Grade extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_name", nullable = false)
    private String gradeName;

    @Column(name = "order_no")
    private Integer orderNo;
}