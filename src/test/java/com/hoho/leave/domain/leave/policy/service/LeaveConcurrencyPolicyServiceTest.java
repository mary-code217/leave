package com.hoho.leave.domain.leave.policy.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class LeaveConcurrencyPolicyServiceTest {

    @Autowired
    LeaveConcurrencyPolicyService leaveConcurrencyPolicyService;

    @Test
    void 동시정책테스트() {
        boolean exist = leaveConcurrencyPolicyService.checkTeamConcurrencyPolicyRange
                (1L, LocalDate.of(2025, 10, 7), LocalDate.of(2025, 10, 13));

        Assertions.assertThat(exist).isTrue();
    }
}