package com.hoho.leave.domain.org.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "grade",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_grade_name", columnNames = "grade_name"),
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_name", nullable = false)
    private String gradeName;

    @Column(name = "order_no")
    private Integer orderNo;

    public static Grade create(GradeCreateRequest request) {
        Grade grade = new Grade();

        grade.gradeName = request.getGradeName();
        grade.orderNo = request.getOrderNo();

        return grade;
    }
    public void changeOrderNo(Integer orderNo) {
        if(orderNo != null) this.orderNo = orderNo;
    }
    public void rename(String gradeName) {
        this.gradeName = gradeName;
    }
}