package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Grade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 직급 리포지토리.
 * 
 * 직급 엔티티에 대한 데이터베이스 접근을 담당한다.
 * 
 */
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * 직급명으로 직급 존재 여부를 확인한다.
     *
     * @param gradeName 직급명
     * @return 존재 여부
     */
    boolean existsByGradeName(String gradeName);

    /**
     * 특정 직급을 제외하고 직급명으로 존재 여부를 확인한다.
     *
     * @param gradeName 직급명
     * @param gradeId 제외할 직급 ID
     * @return 존재 여부
     */
    boolean existsByGradeNameAndIdNot(String gradeName, Long gradeId);

    /**
     * 직급명으로 직급을 조회한다.
     *
     * @param gradeName 직급명
     * @return 직급 엔티티
     */
    Optional<Grade> findByGradeName(String gradeName);
}