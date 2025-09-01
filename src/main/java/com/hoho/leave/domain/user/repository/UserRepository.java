package com.hoho.leave.domain.user.repository;

import com.hoho.leave.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // org 도메인 관련 조회
    boolean existsByTeamId(Long teamId);
    boolean existsByGradeId(Long gradeId);
    boolean existsByPositionId(Long positionId);
    Long countByTeamId(Long teamId);

    // 유저 도메인 관련
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
