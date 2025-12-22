package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 직책 리포지토리.
 * <p>
 * 직책 엔티티에 대한 데이터베이스 접근을 담당한다.
 * </p>
 */
public interface PositionRepository extends JpaRepository<Position, Long> {

    /**
     * 직책명으로 직책 존재 여부를 확인한다.
     *
     * @param positionName 직책명
     * @return 존재 여부
     */
    boolean existsByPositionName(String positionName);

    /**
     * 특정 직책을 제외하고 직책명으로 존재 여부를 확인한다.
     *
     * @param positionName 직책명
     * @param positionId 제외할 직책 ID
     * @return 존재 여부
     */
    boolean existsByPositionNameAndIdNot(String positionName, Long positionId);

    /**
     * 직책명으로 직책을 조회한다.
     *
     * @param positionName 직책명
     * @return 직책 엔티티
     */
    Optional<Position> findByPositionName(String positionName);
}
