package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
