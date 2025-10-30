package com.hoho.leave.domain.handover.service.support;

import java.util.HashSet;
import java.util.Set;

public class RecipientChangeSet {
    private Set<Long> existingIds = new HashSet<>();
    private Set<Long> desiredIds = new HashSet<>();

    public static RecipientChangeSet of(Set<Long> existingIds, Set<Long> desiredIds) {
        RecipientChangeSet recipientChangeSet = new RecipientChangeSet();

        recipientChangeSet.existingIds = existingIds;
        recipientChangeSet.desiredIds = desiredIds;

        return recipientChangeSet;
    }

    public Set<Long> getToAdd() {
        Set<Long> toAdd = new HashSet<>(desiredIds);
        toAdd.removeAll(existingIds);
        return toAdd;
    }

    public Set<Long> getToRemove() {
        Set<Long> toRemove = new HashSet<>(existingIds);
        toRemove.removeAll(desiredIds);
        return toRemove;
    }
}
