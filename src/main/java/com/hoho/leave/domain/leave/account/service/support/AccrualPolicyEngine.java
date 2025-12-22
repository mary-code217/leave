package com.hoho.leave.domain.leave.account.service.support;

import com.hoho.leave.domain.leave.account.entity.LeaveStage;
import com.hoho.leave.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 휴가 부여 정책 엔진.
 * <p>
 * 사용자의 입사일을 기준으로 휴가 부여 스케줄을 계산한다.
 * </p>
 */
@Service
public class AccrualPolicyEngine {
    /**
     * 사용자의 휴가 부여 스케줄을 계산한다.
     *
     * @param user 사용자
     * @return 휴가 부여 스케줄
     */
    public AccrualSchedule getAccountEvent(User user) {
        LeaveStage leaveStage;
        LocalDate nextAccrualAt;
        boolean isOneYear = LocalDate.now().isBefore(user.getHireDate().plusYears(1));
        if(isOneYear){
            leaveStage = LeaveStage.MONTHLY;
            nextAccrualAt = nextMonthlyAccrual(user.getHireDate(), LocalDate.now());
        }else {
            leaveStage = LeaveStage.ANNUAL;
            nextAccrualAt = nextAnnualAccrual(user.getHireDate(), LocalDate.now());
        }

        return new AccrualSchedule(leaveStage, nextAccrualAt);
    }

    /**
     * 다음 월차 부여일을 계산한다.
     *
     * @param hireDate 입사일
     * @param today 오늘 날짜
     * @return 다음 월차 부여일
     */
    public static LocalDate nextMonthlyAccrual(LocalDate hireDate, LocalDate today) {
        int hireDay = hireDate.getDayOfMonth();

        // 이번 달 앵커일 = 이번 달의 hireDay (해당 일이 없으면 그 달의 말일)
        LocalDate thisMonthAnchor = withDayOrMonthEnd(today.getYear(), today.getMonthValue(), hireDay);

        // today >= 이번 달 앵커일  →  다음 달 앵커일
        if (!today.isBefore(thisMonthAnchor)) {
            LocalDate nextMonth = today.plusMonths(1);
            return withDayOrMonthEnd(nextMonth.getYear(), nextMonth.getMonthValue(), hireDay);
        }
        // today < 이번 달 앵커일  →  이번 달 앵커일
        return thisMonthAnchor;
    }

    /**
     * 다음 연차 부여일을 계산한다.
     *
     * @param hireDate 입사일
     * @param today 오늘 날짜
     * @return 다음 연차 부여일
     */
    public static LocalDate nextAnnualAccrual(LocalDate hireDate, LocalDate today) {
        int month = hireDate.getMonthValue();
        int day = hireDate.getDayOfMonth();

        LocalDate thisYearAnniv = withDayOrMonthEnd(today.getYear(), month, day);

        // today >= 올해 기념일  →  내년 기념일
        if (!today.isBefore(thisYearAnniv)) {
            return withDayOrMonthEnd(today.plusYears(1).getYear(), month, day);
        }
        // today < 올해 기념일  →  올해 기념일
        return thisYearAnniv;
    }

    private static LocalDate withDayOrMonthEnd(int year, int month, int day) {
        LocalDate first = LocalDate.of(year, month, 1);
        int lastDay = first.lengthOfMonth();
        return LocalDate.of(year, month, Math.min(day, lastDay));
    }
}
