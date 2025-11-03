package com.hoho.leave.domain.org.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.request.GradeUpdateRequest;
import com.hoho.leave.domain.org.dto.response.GradeDetailResponse;
import com.hoho.leave.domain.org.dto.response.GradeListResponse;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.repository.GradeRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
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
    public void createGrade(GradeCreateRequest request) {
        checkDuplicateGrade(request.getGradeName());
        gradeRepository.save(Grade.create(request));
    }

    @Transactional
    public void updateGrade(Long gradeId, GradeUpdateRequest request) {
        Grade grade = getGradeEntity(gradeId);
        checkDuplicateGrade(request.getGradeName(), gradeId);
        grade.changeOrderNo(request.getOrderNo());
        grade.rename(request.getGradeName());
    }

    @Transactional
    public void deleteGrade(Long gradeId) {
        Grade grade = getGradeEntity(gradeId);
        existUser(gradeId);
        gradeRepository.delete(grade);
    }

    @Transactional(readOnly = true)
    public GradeDetailResponse getGrade(Long gradeId) {
        return GradeDetailResponse.of(getGradeEntity(gradeId));
    }

    @Transactional(readOnly = true)
    public GradeListResponse getAllGrades(Integer size, Integer page) {
        Pageable pageable = getPageable(size, page);

        Page<Grade> pageList = gradeRepository.findAll(pageable);

        List<GradeDetailResponse> list = pageList.getContent().stream().map(GradeDetailResponse::of).toList();

        return GradeListResponse.of(pageList, list);
    }

    /**
     * 페이지 정보 생성
     */
    private Pageable getPageable(Integer size, Integer page) {
        Sort sort = Sort.by(Sort.Order.asc("orderNo"));
        return PageRequest.of(page - 1, size, sort);
    }

    /**
     * 중복 직급 체크
     * @param gradeName 직급명
     */
    private void checkDuplicateGrade(String gradeName) {
        if (gradeRepository.existsByGradeName(gradeName)) {
            throw new DuplicateException("Duplicate Grade : " + gradeName);
        }
    }

    /**
     * 중복 직급 체크
     * @param gradeName 직급명
     * @param gradeId 직급ID
     */
    private void checkDuplicateGrade(String gradeName, Long gradeId) {
        if (gradeRepository.existsByGradeNameAndIdNot(gradeName, gradeId)) {
            throw new DuplicateException("Duplicate Grade : " + gradeName);
        }
    }

    /**
     * 직급 엔티티 조회
     */
    private Grade getGradeEntity(Long gradeId) {
        return gradeRepository.findById(gradeId)
                .orElseThrow(() -> new NotFoundException("Not Found Grade : " + gradeId));
    }

    /**
     * 소속 사용자 존재 여부 체크
     */
    private void existUser(Long gradeId) {
        if (userRepository.existsByGradeId(gradeId)) {
            throw new BusinessException("Delete Failed : 소속 사용자가 존재합니다.");
        }
    }
}
