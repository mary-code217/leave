package com.hoho.leave.domain.org.entity;

import com.hoho.leave.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
}