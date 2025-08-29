package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByTeamName(String teamName);
    boolean existsByTeamNameAndIdNot(String teamName, Long id);
    boolean existsByParentId(Long parentId);
    Long countByParentId(Long parentId);
}