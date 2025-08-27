package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.repository.PositionRepository;
import com.hoho.leave.util.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    @Transactional
    public void createPosition(PositionCreateRequest positionCreateRequest) {
        if(positionRepository.existsByPositionName(positionCreateRequest.getPositionName())) {
            throw new BusinessException("이미 존재하는 직책 입니다.");
        }
        positionRepository.save(Position.create(positionCreateRequest.getPositionName(), positionCreateRequest.getOrderNo()));
    }
}
