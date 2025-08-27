package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.request.GradeUpdateRequest;
import com.hoho.leave.domain.org.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/grade")
public class GradeController {

    private final GradeService gradeService;

    @PostMapping("")
    public ResponseEntity<?> createGrade(@RequestBody @Valid GradeCreateRequest gradeCreateRequest) {

        gradeService.createGrade(gradeCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("직급 등록 성공");
    }

    @PutMapping("{gradeId}")
    public ResponseEntity<?> updateGrade(@PathVariable Long gradeId,
                                         @RequestBody @Valid GradeUpdateRequest gradeUpdateRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body("직급 수정 성공");
    }
}
