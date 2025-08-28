package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Grade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    boolean existsByGradeName(String gradeName);
    boolean existsByGradeNameAndIdNot(String gradeName, Long gradeId);
}