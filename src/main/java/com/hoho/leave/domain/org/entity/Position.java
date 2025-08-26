package com.hoho.leave.domain.org.entity;

import com.hoho.leave.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "position",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_position_name", columnNames = "position_name")
        }
)
public class Position extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_name", nullable = false)
    private String positionName;
}