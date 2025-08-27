package com.hoho.leave.domain.org.entity;

import com.hoho.leave.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(
        name = "team",
        // 형제 중복 금지 버전 (전사 유일을 원하면 아래 uniqueConstraints 제거하고 column unique=true 유지)
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_team_parent_name", columnNames = {"parent_id", "team_name"})
        }
)
public class Team extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Team parent; // 루트면 NULL

    @OneToMany(mappedBy = "parent",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("teamName ASC")
    private List<Team> children = new ArrayList<>();

    @Column(name = "order_no")
    private Integer orderNo;

    protected Team() {}

    private Team(String name, Integer orderNo) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("부서명은 비어 있을 수 없습니다.");
        if (orderNo != null && orderNo < 0)   throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");
        this.teamName = name;
        this.orderNo  = orderNo;
    }

    // 루트 부서 생성 -> 상위부서 없음
    public static Team createRoot(String teamName, Integer orderNo) {
        return new Team(teamName, orderNo);
    }

    // 부모가 자신의 자식을 생성 (양방향 일관성 보장)
    public Team createChild(String teamName, Integer orderNo) {
        Team child = new Team(teamName, orderNo);
        child.parent = this;
        this.children.add(child);
        return child;
    }

    // --- 변경 메서드 ---
    public void rename(String newName) {
        if (newName == null || newName.isBlank())
            throw new IllegalArgumentException("팀명은 비어 있을 수 없습니다.");
        this.teamName = newName;
    }

    public void changeOrder(Integer no) {
        if (no != null && no < 0)
            throw new IllegalArgumentException("orderNo는 0 이상이어야 합니다.");
        this.orderNo = no;
    }

    public void moveTo(Team newParent) {
        if (Objects.equals(this.parent, newParent)) return;
        if (this.parent != null) this.parent.children.remove(this);
        this.parent = newParent;
        if (newParent != null) newParent.children.add(this);
    }
}