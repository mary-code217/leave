package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
