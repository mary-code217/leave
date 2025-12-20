package com.hoho.leave.domain.org.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.dto.response.TeamDetailResponse;
import com.hoho.leave.domain.org.dto.response.TeamListResponse;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.repository.TeamRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createTeam(TeamCreateRequest request) {
        checkDuplicateTeam(request.getTeamName());

        teamRepository.save(teamRegister(request));
    }

    @Transactional
    public void updateTeam(Long teamId, TeamUpdateRequest request) {
        Team team = getTeamEntity(teamId);

        checkDuplicateTeam(request.getTeamName(), teamId);

        updateParent(team, request.getParentId());

        team.changeOrder(request.getOrderNo());

        team.rename(request.getTeamName());
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = getTeamEntity(teamId);
        checkExistChildOrUser(teamId);
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public TeamDetailResponse getTeam(Long teamId) {
        Team team = getTeamEntity(teamId);
        Long userCount = userRepository.countByTeamId(teamId);
        Long childrenCount = teamRepository.countByParentId(teamId);
        return TeamDetailResponse.of(team, userCount, childrenCount);
    }

    @Transactional(readOnly = true)
    public TeamListResponse getAllTeams(Integer size, Integer page) {
        Pageable pageable = getPageable(size, page);

        Page<Team> pageList = teamRepository.findAll(pageable);

        List<TeamDetailResponse> list = getTeamDetailResponses(pageList);

        return TeamListResponse.of(pageList, list);
    }

    /**
     * 부서 등록
     */
    private Team teamRegister(TeamCreateRequest request) {
        Team team;

        if (request.getParentId() == null) {
            team = Team.createTeam(request);
        } else {
            Team parent = getTeamEntity(request.getParentId());
            team = parent.createChild(request);
        }

        return team;
    }

    /**
     * 페이지 정보 생성
     */
    private static PageRequest getPageable(Integer size, Integer page) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("orderNo")));
    }

    /**
     * 부서 엔티티 조회
     */
    public Team getTeamEntity(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Not Found Team : " + teamId));
    }

    /**
     * 중복 부서 체크
     * @param teamName 부서명
     */
    private void checkDuplicateTeam(String teamName) {
        if (teamRepository.existsByTeamName(teamName)) {
            throw new DuplicateException("Duplicate TeamName : " + teamName);
        }
    }

    /**
     * 중복 부서 체크
     * @param teamName 부서명
     * @param teamId 팀ID
     */
    private void checkDuplicateTeam(String teamName, Long teamId) {
        if (teamRepository.existsByTeamNameAndIdNot(teamName, teamId)) {
            throw new DuplicateException("Duplicate TeamName : " + teamName);
        }
    }

    /**
     * 상위 부서 수정
     */
    private void updateParent(Team team, Long parentId) {
        if (parentId != null) {
            if (parentId.equals(team.getId())) {
                throw new BusinessException("Update Failed : 자기 자신을 상위 부서로 지정할 수 없습니다.");
            }
            Team parent = teamRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Not Found Team : " + parentId));
            team.moveTo(parent);
        }
    }

    /**
     * 하위 부서 또는 소속 사용자 존재 여부 체크
     */
    private void checkExistChildOrUser(Long teamId) {
        if (teamRepository.existsByParentId(teamId)) {
            throw new BusinessException("Delete Failed : 하위 부서가 존재합니다.");
        } else if (userRepository.existsByTeamId(teamId)) {
            throw new BusinessException("Delete Failed : 소속 사용자가 존재합니다.");
        }
    }

    /**
     * 부서 상세 응답 리스트 생성 (N+1 최적화)
     * - 기존: 부서 N개 조회 시 userCount N번 + childrenCount N번 = 2N+1 쿼리
     * - 개선: 부서 목록 1번 + userCount 1번 + childrenCount 1번 = 3 쿼리
     */
    private List<TeamDetailResponse> getTeamDetailResponses(Page<Team> pageList) {
        // 조회된 부서들의 ID 목록 추출
        List<Long> teamIds = pageList.getContent().stream()
                .map(Team::getId)
                .toList();

        // 부서별 소속 유저 수 일괄 조회 (IN 쿼리 1회)
        Map<Long, Long> userCountMap = toCountMap(userRepository.countByTeamIds(teamIds));
        // 부서별 하위 부서 수 일괄 조회 (IN 쿼리 1회)
        Map<Long, Long> childrenCountMap = toCountMap(teamRepository.countChildrenByTeamIds(teamIds));

        // Map에서 조회하여 응답 DTO 생성
        List<TeamDetailResponse> list = new ArrayList<>();
        for (Team team : pageList.getContent()) {
            Long userCount = userCountMap.getOrDefault(team.getId(), 0L);
            Long childrenCount = childrenCountMap.getOrDefault(team.getId(), 0L);
            list.add(TeamDetailResponse.of(team, userCount, childrenCount));
        }
        return list;
    }

    /**
     * Object[] 결과를 Map<ID, Count>로 변환
     */
    private Map<Long, Long> toCountMap(List<Object[]> results) {
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }
}
