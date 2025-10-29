package com.hoho.leave.domain.handover.dto;

import java.util.HashSet;
import java.util.Set;

public class Diff {
    Set<Long> existingIds = new HashSet<>();
    Set<Long> desiredIds = new HashSet<>();

    public static Diff setting(Set<Long> existingIds, Set<Long> desiredIds) {
        Diff diff = new Diff();

        diff.existingIds = existingIds;
        diff.desiredIds = desiredIds;

        return diff;
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
