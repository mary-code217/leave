package com.hoho.leave.domain.org.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "team")
public class Team extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Team parent; // 루트면 NULL

    @OneToMany(mappedBy = "parent",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("orderNo ASC")
    private List<Team> children = new ArrayList<>();

    @Column(name = "order_no")
    private Integer orderNo;

    public Team(String teamName, Integer orderNo) {
        if(teamName == null || teamName.isBlank())
            throw new IllegalArgumentException("부서명은 필수 입력 입니다.");
        if(orderNo != null && orderNo < 0)
            throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");
        this.teamName = teamName;
        this.orderNo = orderNo;
    }

    // 루트 부서 생성
    public static Team createTeam(String teamName, Integer orderNo) {
        return new Team(teamName, orderNo);
    }

    // 자식 부서 생성
    public Team createChild(String teamName, Integer orderNo) {
        Team child = new Team(teamName, orderNo);
        child.parent = this;
        this.children.add(child);
        return child;
    }

    // 부서명 변경
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("팀명은 비어 있을 수 없습니다.");
        }
        this.teamName = newName;
    }

    // 정렬기준 변경
    public void changeOrder(Integer newOrderNo) {
        if (newOrderNo != null && newOrderNo < 0) {
            throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");
        }
        this.orderNo = newOrderNo;
    }

    // 상위부서 이동
    public void moveTo(Team newParent) {
        if (this == newParent) {
            throw new IllegalArgumentException("자기 자신을 상위 부서로 지정할 수 없습니다.");
        }
        // 동일 parent면 변경 없음
        if (java.util.Objects.equals(this.parent, newParent)) {
            return;
        }
        // (선택) 사이클 방지: newParent의 조상에 this가 있는지 확인
        for (Team p = newParent; p != null; p = p.getParent()) {
            if (p == this) {
                throw new IllegalStateException("하위 부서로 이동할 수 없습니다.");
            }
        }
        // 기존 부모의 children에서 제거
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        // 새 부모로 교체
        this.parent = newParent;
        if (newParent != null) {
            newParent.children.add(this);
        }
    }
}