package com.hoho.leave.domain.org.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 직급 엔티티.
 * 
 * 조직 내 직급 정보를 관리한다.
 * 
 */
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

    /**
     * 직급을 생성한다.
     *
     * @param request 직급 생성 요청 정보
     * @return 생성된 직급 엔티티
     */
    public static Grade create(GradeCreateRequest request) {
        Grade grade = new Grade();

        grade.gradeName = request.getGradeName();
        grade.orderNo = request.getOrderNo();

        return grade;
    }

    /**
     * 정렬 순서를 변경한다.
     *
     * @param orderNo 새로운 정렬 순서
     */
    public void changeOrderNo(Integer orderNo) {
        if(orderNo != null) this.orderNo = orderNo;
    }

    /**
     * 직급명을 변경한다.
     *
     * @param gradeName 새로운 직급명
     */
    public void rename(String gradeName) {
        this.gradeName = gradeName;
    }
}