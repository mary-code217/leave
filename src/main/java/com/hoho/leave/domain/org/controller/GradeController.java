package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.request.GradeUpdateRequest;
import com.hoho.leave.domain.org.dto.response.GradeDetailResponse;
import com.hoho.leave.domain.org.dto.response.GradeListResponse;
import com.hoho.leave.domain.org.service.GradeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 직급 관리 컨트롤러.
 * <p>
 * 직급의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/grade")
public class GradeController {

    private final GradeService gradeService;

    /**
     * 직급을 생성한다.
     *
     * @param gradeCreateRequest 직급 생성 요청 정보
     * @return 생성 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createGrade(@RequestBody @Valid GradeCreateRequest gradeCreateRequest) {

        gradeService.createGrade(gradeCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직급 등록 성공");
    }

    /**
     * 직급을 수정한다.
     *
     * @param gradeId 직급 ID
     * @param gradeUpdateRequest 직급 수정 요청 정보
     * @return 수정 성공 메시지
     */
    @PutMapping("/{gradeId}")
    public ResponseEntity<?> updateGrade(@PathVariable Long gradeId,
                                         @RequestBody @Valid GradeUpdateRequest gradeUpdateRequest) {

        gradeService.updateGrade(gradeId, gradeUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직급 수정 성공");
    }

    /**
     * 직급을 삭제한다.
     *
     * @param gradeId 직급 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/{gradeId}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long gradeId) {

        gradeService.deleteGrade(gradeId);

        return ResponseEntity.status(HttpStatus.OK).body("직급 삭제 성공");
    }

    /**
     * 특정 직급을 조회한다.
     *
     * @param gradeId 직급 ID
     * @return 직급 상세 정보
     */
    @GetMapping("/{gradeId}")
    public ResponseEntity<GradeDetailResponse> getGrade(@PathVariable Long gradeId) {

        GradeDetailResponse response = gradeService.getGrade(gradeId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 전체 직급 목록을 조회한다.
     *
     * @param size 페이지 크기 (기본값: 5, 최소: 1, 최대: 20)
     * @param page 페이지 번호 (기본값: 1, 최소: 1)
     * @return 직급 목록 및 페이징 정보
     */
    @Validated
    @GetMapping("")
    public ResponseEntity<GradeListResponse> getAllGrades(@RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size,
                                                          @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        GradeListResponse response = gradeService.getAllGrades(size, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
