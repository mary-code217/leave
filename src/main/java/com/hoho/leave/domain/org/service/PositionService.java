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

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPosition(PositionCreateRequest request) {
        checkDuplicatePosition(request.getPositionName());
        positionRepository.save(Position.create(request));
    }

    @Transactional
    public void updatePosition(Long positionId, PositionUpdateRequest request) {
        Position position = getPositionEntity(positionId);
        checkDuplicatePosition(request.getPositionName(), positionId);
        position.changeOrderNo(request.getOrderNo());
        position.rename(request.getPositionName());
    }

    @Transactional
    public void deletePosition(Long positionId) {
        Position position = getPositionEntity(positionId);
        existUser(positionId);
        positionRepository.delete(position);
    }

    @Transactional(readOnly = true)
    public PositionDetailResponse getPosition(Long positionId) {
        return PositionDetailResponse.of(getPositionEntity(positionId));
    }

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
