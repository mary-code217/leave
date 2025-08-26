package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.PositionCreateRequest;
import com.hoho.leave.domain.org.dto.TeamCreateRequest;
import com.hoho.leave.domain.org.repository.GradeRepository;
import com.hoho.leave.domain.org.repository.PositionRepository;
import com.hoho.leave.domain.org.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrgService {
    private final GradeRepository gradeRepository;
    private final PositionRepository positionRepository;
    private final TeamRepository teamRepository;

    void createGrade(GradeCreateRequest gradeCreateRequest) {

    }

    void createPosition(PositionCreateRequest positionCreateRequest) {

    }

    void createTeam(TeamCreateRequest teamCreateRequest) {

    }
}
