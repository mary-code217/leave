package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 팀 리포지토리.
 * <p>
 * 팀 엔티티에 대한 데이터베이스 접근을 담당한다.
 * </p>
 */
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * 팀명으로 팀 존재 여부를 확인한다.
     *
     * @param teamName 팀명
     * @return 존재 여부
     */
    boolean existsByTeamName(String teamName);

    /**
     * 특정 팀을 제외하고 팀명으로 존재 여부를 확인한다.
     *
     * @param teamName 팀명
     * @param id 제외할 팀 ID
     * @return 존재 여부
     */
    boolean existsByTeamNameAndIdNot(String teamName, Long id);

    /**
     * 상위 팀 ID로 하위 팀 존재 여부를 확인한다.
     *
     * @param parentId 상위 팀 ID
     * @return 존재 여부
     */
    boolean existsByParentId(Long parentId);

    /**
     * 상위 팀 ID로 하위 팀 개수를 조회한다.
     *
     * @param parentId 상위 팀 ID
     * @return 하위 팀 개수
     */
    Long countByParentId(Long parentId);

    /**
     * 팀명으로 팀을 조회한다.
     *
     * @param teamName 팀명
     * @return 팀 엔티티
     */
    Optional<Team> findByTeamName(String teamName);

    /**
     * 부서별 하위 부서 수 일괄 조회 (N+1 최적화)
     * @param teamIds 조회할 부서 ID 목록
     * @return Object[0]: 부서 ID, Object[1]: 하위 부서 수
     */
    @Query("SELECT t.parent.id, COUNT(t) FROM Team t WHERE t.parent.id IN :teamIds GROUP BY t.parent.id")
    List<Object[]> countChildrenByTeamIds(@Param("teamIds") List<Long> teamIds);
}