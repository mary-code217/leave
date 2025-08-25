package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.PositionCreateRequest;
import com.hoho.leave.domain.org.dto.TeamCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrgController {
    
    @PostMapping("/grade")
    public ResponseEntity<?> createGrade(@RequestBody GradeCreateRequest gradeCreateRequest) {

        System.out.println("createGrade " + gradeCreateRequest.getGrade());

        return ResponseEntity.status(HttpStatus.CREATED).body("직급 등록 성공");
    }

    @PostMapping("/position")
    public ResponseEntity<?> createPosition(@RequestBody PositionCreateRequest  positionCreateRequest) {

        System.out.println("createPosition " + positionCreateRequest.getPosition());

        return ResponseEntity.status(HttpStatus.CREATED).body("직책 등록 성공");
    }

    @PostMapping("/team")
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateRequest teamCreateRequest) {

        System.out.println("createTeam " + teamCreateRequest.getTeam());
        System.out.println("createTeam " + teamCreateRequest.getParent());

        return ResponseEntity.status(HttpStatus.CREATED).body("부서 등록 성공");
    }
}
