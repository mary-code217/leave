package com.hoho.leave.domain.handover.service.support;

import java.util.HashSet;
import java.util.Set;

/**
 * 인수인계 수신자 변경 집합.
 * <p>
 * 기존 수신자와 새 수신자 목록을 비교하여
 * 추가해야 할 수신자와 삭제해야 할 수신자를 계산한다.
 * </p>
 */
public class RecipientChangeSet {
    private Set<Long> existingIds = new HashSet<>();
    private Set<Long> desiredIds = new HashSet<>();

    /**
     * 기존 ID와 원하는 ID로 변경 집합을 생성한다.
     *
     * @param existingIds 기존 수신자 ID 집합
     * @param desiredIds  원하는 수신자 ID 집합
     * @return 변경 집합
     */
    public static RecipientChangeSet of(Set<Long> existingIds, Set<Long> desiredIds) {
        RecipientChangeSet recipientChangeSet = new RecipientChangeSet();

        recipientChangeSet.existingIds = existingIds;
        recipientChangeSet.desiredIds = desiredIds;

        return recipientChangeSet;
    }

    /**
     * 새로 추가해야 할 수신자 ID 집합을 반환한다.
     *
     * @return 추가 대상 ID 집합
     */
    public Set<Long> getToAdd() {
        Set<Long> toAdd = new HashSet<>(desiredIds);
        toAdd.removeAll(existingIds);
        return toAdd;
    }

    /**
     * 삭제해야 할 수신자 ID 집합을 반환한다.
     *
     * @return 삭제 대상 ID 집합
     */
    public Set<Long> getToRemove() {
        Set<Long> toRemove = new HashSet<>(existingIds);
        toRemove.removeAll(desiredIds);
        return toRemove;
    }
}
