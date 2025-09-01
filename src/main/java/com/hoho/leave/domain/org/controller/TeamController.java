package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.dto.response.TeamDetailResponse;
import com.hoho.leave.domain.org.dto.response.TeamListResponse;
import com.hoho.leave.domain.org.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

        return ResponseEntity.status(HttpStatus.OK).body("부서 등록 성공");
    }
    
    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Long teamId,
                                        @RequestBody @Valid TeamUpdateRequest teamUpdateRequest) {

        teamService.updateTeam(teamId, teamUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("부서 변경 성공");
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId) {

        teamService.deleteTeam(teamId);

        return ResponseEntity.status(HttpStatus.OK).body("부서 삭제 성공");
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDetailResponse> getTeam(@PathVariable Long teamId) {

        TeamDetailResponse response = teamService.getTeam(teamId);

        return new ResponseEntity<TeamDetailResponse>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<TeamListResponse> getAllTeams(@RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size,
                                                        @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        TeamListResponse response = teamService.getAllTeams(size, page);

        return new ResponseEntity<TeamListResponse>(response, HttpStatus.OK);
    }

}
