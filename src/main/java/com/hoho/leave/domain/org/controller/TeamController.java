package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("")
    public ResponseEntity<?> createTeam(@RequestBody @Valid TeamCreateRequest teamCreateRequest) {

        teamService.createTeam(teamCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("부서 등록 성공");
    }
    
    @PatchMapping("{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Long teamId,
                                        @RequestBody @Valid TeamUpdateRequest teamUpdateRequest) {

        teamService.updateTeam(teamId, teamUpdateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("부서 변경 성공");
    }
}
