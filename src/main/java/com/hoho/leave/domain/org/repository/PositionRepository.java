package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByPositionName(String positionName);
    boolean existsByPositionNameAndIdNot(String positionName, Long positionId);

    Optional<Position> findByPositionName(String positionName);
}
