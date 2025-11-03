package com.hoho.leave.domain.org.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static Position create(PositionCreateRequest request) {
        Position position = new Position();

        position.positionName = request.getPositionName();
        position.orderNo = request.getOrderNo();

        return position;
    }

    public void changeOrderNo(Integer orderNo) {
        if(orderNo != null) this.orderNo = orderNo;
    }

    public void rename(String newPositionName) {
        this.positionName = newPositionName;
    }
}