package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByTeamName(String teamName);
    boolean existsByTeamNameAndIdNot(String teamName, Long id);
    boolean existsByParentId(Long parentId);
    Long countByParentId(Long parentId);

    Optional<Team> findByTeamName(String teamName);

    /**
     * 부서별 하위 부서 수 일괄 조회 (N+1 최적화)
     * @param teamIds 조회할 부서 ID 목록
     * @return Object[0]: 부서 ID, Object[1]: 하위 부서 수
     */
    @Query("SELECT t.parent.id, COUNT(t) FROM Team t WHERE t.parent.id IN :teamIds GROUP BY t.parent.id")
    List<Object[]> countChildrenByTeamIds(@Param("teamIds") List<Long> teamIds);
}