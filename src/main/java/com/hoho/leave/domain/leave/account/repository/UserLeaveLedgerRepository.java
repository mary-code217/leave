package com.hoho.leave.domain.leave.account.repository;

import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLeaveLedgerRepository extends JpaRepository<UserLeaveLedger, Long> {
    @EntityGraph(attributePaths = {"userLeaves", "userLeaves.user"})
    Page<UserLeaveLedger> findAll(Pageable pageable);
}
