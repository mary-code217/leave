package com.hoho.leave.domain.org.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.dto.request.PositionUpdateRequest;
import com.hoho.leave.domain.org.dto.response.PositionDetailResponse;
import com.hoho.leave.domain.org.dto.response.PositionListResponse;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.repository.PositionRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 직책 관리 서비스.
 * <p>
 * 직책의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    /**
     * 직책을 생성한다.
     *
     * @param request 직책 생성 요청 정보
     */
    @Transactional
    public void createPosition(PositionCreateRequest request) {
        checkDuplicatePosition(request.getPositionName());
        positionRepository.save(Position.create(request));
    }

    /**
     * 직책을 수정한다.
     *
     * @param positionId 직책 ID
     * @param request 직책 수정 요청 정보
     */
    @Transactional
    public void updatePosition(Long positionId, PositionUpdateRequest request) {
        Position position = getPositionEntity(positionId);
        checkDuplicatePosition(request.getPositionName(), positionId);
        position.changeOrderNo(request.getOrderNo());
        position.rename(request.getPositionName());
    }

    /**
     * 직책을 삭제한다.
     *
     * @param positionId 직책 ID
     */
    @Transactional
    public void deletePosition(Long positionId) {
        Position position = getPositionEntity(positionId);
        existUser(positionId);
        positionRepository.delete(position);
    }

    /**
     * 특정 직책을 조회한다.
     *
     * @param positionId 직책 ID
     * @return 직책 상세 응답 DTO
     */
    @Transactional(readOnly = true)
    public PositionDetailResponse getPosition(Long positionId) {
        return PositionDetailResponse.of(getPositionEntity(positionId));
    }

    /**
     * 전체 직책 목록을 조회한다.
     *
     * @param size 페이지 크기
     * @param page 페이지 번호
     * @return 직책 목록 응답 DTO
     */
    @Transactional(readOnly = true)
    public PositionListResponse getAllPositions(Integer size, Integer page) {
        Pageable pageable = getPageable(size, page);

        Page<Position> pageList = positionRepository.findAll(pageable);

        List<PositionDetailResponse> list = pageList.getContent().stream().map(PositionDetailResponse::of).toList();

        return PositionListResponse.of(pageList, list);
    }

    /**
     * 페이지 정보 생성
     */
    private static Pageable getPageable(Integer size, Integer page) {
        Sort sort = Sort.by(Sort.Order.asc("orderNo"));
        return PageRequest.of(page - 1, size, sort);
    }

    /**
     * 직책 엔티티 조회
     */
    private Position getPositionEntity(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("Not found Position : " + positionId));
    }

    /**
     * 중복 직책 체크
     * @param positionName 직책명
     */
    private void checkDuplicatePosition(String positionName) {
        if (positionRepository.existsByPositionName(positionName)) {
            throw new DuplicateException("Duplicate Position : " + positionName);
        }
    }

    /**
     * 중복 직책 체크
     * @param positionName 직책명
     * @param positionId 직책ID
     */
    private void checkDuplicatePosition(String positionName, Long positionId) {
        if (positionRepository.existsByPositionNameAndIdNot(positionName, positionId)) {
            throw new DuplicateException("Duplicate Position : " + positionName);
        }
    }

    /**
     * 소속 사용자 존재 여부 체크
     */
    private void existUser(Long positionId) {
        if (userRepository.existsByPositionId(positionId)) {
            throw new BusinessException("Delete failed : 사용자가 존재합니다.");
        }
    }
}
