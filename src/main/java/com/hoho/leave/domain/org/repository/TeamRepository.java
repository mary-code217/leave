package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByParentIsNullAndTeamName(String teamName);      // 루트에서 중복
    boolean existsByParentIdAndTeamName(Long parentId, String teamName); // 특정 부모 아래 중복

    boolean existsByParentIsNullAndTeamNameAndIdNot(String teamName, Long id);
    boolean existsByParent_IdAndTeamNameAndIdNot(Long parentId, String teamName, Long id);
}