package com.hoho.leave.domain.org.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 직책 엔티티.
 * 
 * 조직 내 직책 정보를 관리한다.
 * 
 */
@Entity
@Getter
@Table(
        name = "position",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_position_name", columnNames = "position_name")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_name", nullable = false)
    private String positionName;

    @Column(name = "order_no")
    private Integer orderNo;

    /**
     * 직책을 생성한다.
     *
     * @param request 직책 생성 요청 정보
     * @return 생성된 직책 엔티티
     */
    public static Position create(PositionCreateRequest request) {
        Position position = new Position();

        position.positionName = request.getPositionName();
        position.orderNo = request.getOrderNo();

        return position;
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
     * 직책명을 변경한다.
     *
     * @param newPositionName 새로운 직책명
     */
    public void rename(String newPositionName) {
        this.positionName = newPositionName;
    }
}