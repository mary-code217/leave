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

/**
 * 팀(부서) 관리 컨트롤러.
 * <p>
 * 조직 내 팀의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    /**
     * 팀을 생성한다.
     *
     * @param teamCreateRequest 팀 생성 요청 정보
     * @return 생성 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createTeam(@RequestBody @Valid TeamCreateRequest teamCreateRequest) {

        teamService.createTeam(teamCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("부서 등록 성공");
    }

    /**
     * 팀을 수정한다.
     *
     * @param teamId 팀 ID
     * @param teamUpdateRequest 팀 수정 요청 정보
     * @return 수정 성공 메시지
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Long teamId,
                                        @RequestBody @Valid TeamUpdateRequest teamUpdateRequest) {

        teamService.updateTeam(teamId, teamUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("부서 변경 성공");
    }

    /**
     * 팀을 삭제한다.
     *
     * @param teamId 팀 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId) {

        teamService.deleteTeam(teamId);

        return ResponseEntity.status(HttpStatus.OK).body("부서 삭제 성공");
    }

    /**
     * 특정 팀을 조회한다.
     *
     * @param teamId 팀 ID
     * @return 팀 상세 정보
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDetailResponse> getTeam(@PathVariable Long teamId) {

        TeamDetailResponse response = teamService.getTeam(teamId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 전체 팀 목록을 조회한다.
     *
     * @param size 페이지 크기 (기본값: 5, 최소: 1, 최대: 20)
     * @param page 페이지 번호 (기본값: 1, 최소: 1)
     * @return 팀 목록 및 페이징 정보
     */
    @GetMapping("")
    public ResponseEntity<TeamListResponse> getAllTeams(@RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size,
                                                        @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        TeamListResponse response = teamService.getAllTeams(size, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
