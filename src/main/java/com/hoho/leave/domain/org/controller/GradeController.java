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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/grade")
public class GradeController {

    private final GradeService gradeService;

    @PostMapping("")
    public ResponseEntity<?> createGrade(@RequestBody @Valid GradeCreateRequest gradeCreateRequest) {

        gradeService.createGrade(gradeCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직급 등록 성공");
    }

    @PutMapping("/{gradeId}")
    public ResponseEntity<?> updateGrade(@PathVariable Long gradeId,
                                         @RequestBody @Valid GradeUpdateRequest gradeUpdateRequest) {

        gradeService.updateGrade(gradeId, gradeUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직급 수정 성공");
    }

    @DeleteMapping("/{gradeId}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long gradeId) {

        gradeService.deleteGrade(gradeId);

        return ResponseEntity.status(HttpStatus.OK).body("직급 삭제 성공");
    }

    // 단건 조회
    @GetMapping("/{gradeId}")
    public ResponseEntity<GradeDetailResponse> getGrade(@PathVariable Long gradeId) {

        GradeDetailResponse response = gradeService.getGrade(gradeId);

        return new ResponseEntity<GradeDetailResponse>(response, HttpStatus.OK);
    }

    // 전체 조회
    @Validated
    @GetMapping("")
    public ResponseEntity<GradeListResponse> getAllGrades(@RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size,
                                                          @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        GradeListResponse response = gradeService.getAllGrades(size, page);

        return new ResponseEntity<GradeListResponse>(response, HttpStatus.OK);
    }
}
