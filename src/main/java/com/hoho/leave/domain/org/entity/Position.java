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

    // 선택값: 지정 안 하면 null (UI에선 이름순 보조 정렬)
    @Column(name = "order_no")
    private Integer orderNo;

    protected Position() {}

    public Position(String positionName, Integer orderNo) {
        if (positionName == null || positionName.isBlank())
            throw new IllegalArgumentException("직급명은 비어 있을 수 없습니다.");
        if (orderNo != null && orderNo < 0)
            throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");
        this.positionName = positionName;
        this.orderNo = orderNo;
    }

    public static Position create(String positionName, Integer orderNo) {
        return new Position(positionName, orderNo);
    }

    public void changeOrderNo(Integer orderNo) {
        if (orderNo != null && orderNo < 0)
            throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");

        this.orderNo = orderNo;
    }

    public void rename(String newPositionName) {
        if (positionName == null || positionName.isBlank())
            throw new IllegalArgumentException("직급명은 비어 있을 수 없습니다.");

        this.positionName = newPositionName;
    }
}