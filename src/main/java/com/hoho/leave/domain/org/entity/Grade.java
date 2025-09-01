package com.hoho.leave.domain.org.entity;

import com.hoho.leave.config.jpa.BaseEntity;
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

    protected Grade() {}

    public Grade(String gradeName, Integer orderNo) {
        if (gradeName == null || gradeName.isBlank())
            throw new IllegalArgumentException("직급명은 비어 있을 수 없습니다.");
        if (orderNo != null && orderNo < 0)
            throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");
        this.gradeName = gradeName;
        this.orderNo = orderNo;
    }

    public static Grade create(String gradeName, Integer orderNo) {
        return new Grade(gradeName, orderNo);
    }

    public void changeOrderNo(Integer orderNo) {
        if (orderNo != null && orderNo < 0)
            throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");

        this.orderNo = orderNo;
    }

    public void rename(String gradeName) {
        if (gradeName == null || gradeName.isBlank())
            throw new IllegalArgumentException("직급명은 비어 있을 수 없습니다.");

        this.gradeName = gradeName;
    }
}