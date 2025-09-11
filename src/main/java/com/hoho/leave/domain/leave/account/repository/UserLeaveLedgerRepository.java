package com.hoho.leave.domain.leave.account.repository;

import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLeaveLedgerRepository extends JpaRepository<UserLeaveLedger, Long> {
}
