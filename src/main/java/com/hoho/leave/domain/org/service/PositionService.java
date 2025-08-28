package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.dto.request.PositionUpdateRequest;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.repository.PositionRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPosition(PositionCreateRequest positionCreateRequest) {
        if(positionRepository.existsByPositionName(positionCreateRequest.getPositionName())) {
            throw new BusinessException("이미 존재하는 직책 입니다.");
        }
        positionRepository.save(Position.create(positionCreateRequest.getPositionName(), positionCreateRequest.getOrderNo()));
    }

    @Transactional
    public void updatePosition(Long positionId, PositionUpdateRequest positionUpdateRequest) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new BusinessException("수정 실패 - 존재하지 않는 직책 입니다."));

        if(positionRepository.existsByPositionNameAndIdNot(positionUpdateRequest.getPositionName(), positionId)) {
            throw new BusinessException("수정 실패 - 이미 존재하는 직책 입니다.");
        }

        position.changeOrderNo(positionUpdateRequest.getOrderNo());
        position.rename(positionUpdateRequest.getPositionName());
    }

    @Transactional
    public void deletePosition(Long positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new BusinessException("삭제 실패 - 존재하지 않는 직책 입니다."));

        if(userRepository.existsByPositionId(positionId)) {
            throw new BusinessException("삭제 실패 - 해당 직책을 사용하는 유저가 존재합니다.");
        }

        positionRepository.delete(position);
    }
}
