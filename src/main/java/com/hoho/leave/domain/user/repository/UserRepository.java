package com.hoho.leave.domain.user.repository;

import com.hoho.leave.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // org 도메인 관련 조회
    boolean existsByTeamId(Long teamId);
    boolean existsByGradeId(Long gradeId);
    boolean existsByPositionId(Long positionId);
    Long countByTeamId(Long teamId);

    /**
     * 부서별 소속 유저 수 일괄 조회 (N+1 최적화)
     * @param teamIds 조회할 부서 ID 목록
     * @return Object[0]: 부서 ID, Object[1]: 소속 유저 수
     */
    @Query("SELECT u.team.id, COUNT(u) FROM User u WHERE u.team.id IN :teamIds GROUP BY u.team.id")
    List<Object[]> countByTeamIds(@Param("teamIds") List<Long> teamIds);

    // 유저 도메인 관련
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @EntityGraph(value = "User.withOrg", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select u from User u")
    Page<User> findPageWithOrg(Pageable pageable);

    @EntityGraph(value = "User.withOrg", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select u from User u where u.id = :userId")
    Optional<User> findByIdWithOrg(Long userId);

}
