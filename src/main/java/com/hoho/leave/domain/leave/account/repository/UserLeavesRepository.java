package com.hoho.leave.domain.leave.account.repository;

import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLeavesRepository extends JpaRepository<UserLeaves, Long> {
    boolean existsByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<UserLeaves> findByUserId(Long userId);
}
