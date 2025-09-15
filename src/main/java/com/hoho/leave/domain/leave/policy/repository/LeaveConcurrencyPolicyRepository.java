package com.hoho.leave.domain.leave.policy.repository;

import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveConcurrencyPolicyRepository extends JpaRepository<LeaveConcurrencyPolicy, Long> {
}
