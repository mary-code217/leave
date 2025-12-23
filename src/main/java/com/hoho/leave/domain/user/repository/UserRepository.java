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

/**
 * 사용자 레포지토리.
 * 
 * 사용자 엔티티의 데이터베이스 접근을 담당한다.
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 특정 부서에 소속된 사용자가 존재하는지 확인한다.
     *
     * @param teamId 부서 ID
     * @return 존재 여부
     */
    boolean existsByTeamId(Long teamId);

    /**
     * 특정 직급의 사용자가 존재하는지 확인한다.
     *
     * @param gradeId 직급 ID
     * @return 존재 여부
     */
    boolean existsByGradeId(Long gradeId);

    /**
     * 특정 직책의 사용자가 존재하는지 확인한다.
     *
     * @param positionId 직책 ID
     * @return 존재 여부
     */
    boolean existsByPositionId(Long positionId);

    /**
     * 특정 부서에 소속된 사용자 수를 조회한다.
     *
     * @param teamId 부서 ID
     * @return 소속 사용자 수
     */
    Long countByTeamId(Long teamId);

    /**
     * 부서별 소속 유저 수 일괄 조회 (N+1 최적화)
     * @param teamIds 조회할 부서 ID 목록
     * @return Object[0]: 부서 ID, Object[1]: 소속 유저 수
     */
    @Query("SELECT u.team.id, COUNT(u) FROM User u WHERE u.team.id IN :teamIds GROUP BY u.team.id")
    List<Object[]> countByTeamIds(@Param("teamIds") List<Long> teamIds);

    /**
     * 이메일이 이미 존재하는지 확인한다.
     *
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 이메일로 사용자를 조회한다.
     *
     * @param email 이메일
     * @return 사용자 엔티티
     */
    Optional<User> findByEmail(String email);

    /**
     * 조직 정보를 포함하여 사용자 목록을 페이지 단위로 조회한다.
     *
     * @param pageable 페이지 정보
     * @return 사용자 페이지
     */
    @EntityGraph(value = "User.withOrg", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select u from User u")
    Page<User> findPageWithOrg(Pageable pageable);

    /**
     * 조직 정보를 포함하여 사용자를 ID로 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     */
    @EntityGraph(value = "User.withOrg", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select u from User u where u.id = :userId")
    Optional<User> findByIdWithOrg(Long userId);

}
