package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.repository.GradeRepository;
import com.hoho.leave.util.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    @Transactional
    public void createGrade(GradeCreateRequest gradeCreateRequest) {
        if(gradeRepository.existsByGradeName(gradeCreateRequest.getGradeName())){
            throw new BusinessException("이미 존재하는 직급 입니다.");
        };
        gradeRepository.save(Grade.create(gradeCreateRequest.getGradeName(), gradeCreateRequest.getOrderNo()));
    }

}
