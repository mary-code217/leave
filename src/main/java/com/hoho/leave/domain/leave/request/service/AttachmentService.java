package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.domain.leave.request.dto.request.AttachmentUploadRequest;
import com.hoho.leave.domain.leave.request.dto.response.AttachmentResponse;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import com.hoho.leave.domain.leave.request.repository.AttachmentRepository;
import com.hoho.leave.domain.leave.request.repository.LeaveRequestRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.util.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    @Value("${upload.local.dir}")
    private String uploadDir;

    @PostConstruct
    void ensureUploadDir() {
        try {
            Files.createDirectories(Path.of(uploadDir));
        } catch (IOException e) {
            throw new BusinessException("업로드 폴더를 생성할 수 없습니다. "+ uploadDir);
        }
    }

    /** 파일 업로드(pdf, png, jpeg, jpg) 메서드 */
    @Transactional
    public void uploadFile(List<MultipartFile> files, AttachmentUploadRequest request) {
        List<LeaveRequestAttachment> attachments = new ArrayList<>();
        LeaveRequest leaveRequest = leaveRequestRepository.findById(request.getLeaveRequestId())
                .orElseThrow(() -> new BusinessException("존재 하지 않는 신청서 입니다."));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("존재 하지 않는 유저 입니다."));

        files.forEach(f -> {
            // 빈 파일 체크
            if(fileIsEmpty(f)) throw new BusinessException("파일이 존재하지 않습니다.");
            // MIME 타입 체크
            if(!isAllowedMime(f.getContentType())) throw new BusinessException("허용하지 않는 확장자 입니다.");
            // 용량체크
            assertFileSizeAllowed(f.getSize(), f.getContentType());

            String originalName = f.getOriginalFilename();
            String storedName = generateStoredFileName(originalName);
            String fullPath = getFullPath(storedName);
            File dest = new File(fullPath);

            try {
                f.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            attachments.add(LeaveRequestAttachment.create(
                    leaveRequest,
                    originalName,
                    storedName,
                    fullPath,
                    f.getContentType(),
                    f.getSize(),
                    user
            ));
        });

        attachmentRepository.saveAll(attachments);
    }

    /** 파일 정보 가져오는 메서드 */
    @Transactional(readOnly = true)
    public List<AttachmentResponse> getAttachments(Long leaveRequestId) {

        List<LeaveRequestAttachment> attachments = attachmentRepository.findByLeaveRequestId(leaveRequestId);

        return attachments.stream().map(
                a -> AttachmentResponse.of(
                        a.getId(), a.getOriginalName(), a.getSizeBytes(),
                        a.getUploadedBy().getUsername(), a.getUpdatedAt())
        ).toList();
    }

    /** 파일 정보 삭제 */
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        LeaveRequestAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new BusinessException("존재 하지 않는 파일 입니다."));

        try {
            Files.deleteIfExists(Path.of(attachment.getFilePath()));
        } catch (IOException e) {
            throw new BusinessException("파일 삭제 실패!");
        }

        attachmentRepository.delete(attachment);
    }


    /** 로컬 저장소 Path */
    public String getFullPath(String fileName) {
        return uploadDir + File.separator + fileName;
    }

    /** 저장용 파일명 - 중복방지 */
    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    /** 파일 존재여부 확인 */
    private boolean fileIsEmpty(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    /** 허용 MIME */
    private static final Set<String> ALLOWED_MIME = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg"
    );

    private boolean isAllowedMime(String mime) {
        return ALLOWED_MIME.contains(mime);
    }

    /** MIME별 최대 허용 바이트 수 */
    private static final long MB = 1024L * 1024L;
    private static final Map<String, Long> MAX_SIZE_BY_MIME = Map.of(
            "image/jpeg",      10 * MB,
            "image/png",       10 * MB,
            "application/pdf", 20 * MB
    );

    /** 단일 파일 용량 체크: MIME 별 상한 적용 */
    private void assertFileSizeAllowed(long size, String mime) {
        Long limit = MAX_SIZE_BY_MIME.get(mime);

        if (size <= 0 || size > limit) {
            throw new BusinessException(
                    String.format("파일 크기가 허용 범위를 초과했습니다. (형식: %s, 최대: %dMB)", mime, limit / MB)
            );
        }
    }
}
