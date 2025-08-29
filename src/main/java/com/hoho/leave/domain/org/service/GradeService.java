package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.request.GradeUpdateRequest;
import com.hoho.leave.domain.org.dto.response.GradeDetailResponse;
import com.hoho.leave.domain.org.dto.response.GradeListResponse;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.repository.GradeRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createGrade(GradeCreateRequest gradeCreateRequest) {
        if(gradeRepository.existsByGradeName(gradeCreateRequest.getGradeName())){
            throw new BusinessException("생성 실패 - 이미 존재하는 직급 입니다.");
        };
        gradeRepository.save(Grade.create(gradeCreateRequest.getGradeName(), gradeCreateRequest.getOrderNo()));
    }

    @Transactional
    public void updateGrade(Long gradeId, GradeUpdateRequest gradeUpdateRequest) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new BusinessException("수정 실패 - 존재하지 않는 직급 입니다."));

        if(gradeRepository.existsByGradeNameAndIdNot(gradeUpdateRequest.getGradeName(), gradeId)) {
           throw new BusinessException("수정 실패 - 이미 존재하는 직급 입니다.");
        }

        grade.changeOrderNo(gradeUpdateRequest.getOrderNo());
        grade.rename(gradeUpdateRequest.getGradeName());
    }

    @Transactional
    public void deleteGrade(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new BusinessException("삭제 실패 - 존재하지 않는 직급 입니다."));

        if(userRepository.existsByGradeId(gradeId)){
            throw new BusinessException("삭제 실패 - 해당 직급을 사용하는 유저가 존재합니다.");
        }

        gradeRepository.delete(grade);
    }

    @Transactional(readOnly = true)
    public GradeDetailResponse getGrade(Long gradeId) {

        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new BusinessException("조회 실패 - 존재하지 않는 직급 입니다."));

        return GradeDetailResponse.from(grade);
    }

    @Transactional(readOnly = true)
    public GradeListResponse getAllGrades(Integer size, Integer page) {

        Sort sort = Sort.by(Sort.Order.asc("orderNo"));
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Grade> grades = gradeRepository.findAll(pageable);

        List<GradeDetailResponse> list = grades.getContent().stream().map(GradeDetailResponse::from).toList();

        return GradeListResponse.from(
                page,
                size,
                list,
                grades.getTotalPages(),
                grades.getTotalElements(),
                grades.isFirst(),
                grades.isLast()
        );
    }
}
