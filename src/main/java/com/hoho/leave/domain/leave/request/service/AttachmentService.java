package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.common.exception.FileErrorException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.leave.request.dto.request.AttachmentUploadRequest;
import com.hoho.leave.domain.leave.request.dto.response.AttachmentResponse;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import com.hoho.leave.domain.leave.request.repository.AttachmentRepository;
import com.hoho.leave.domain.leave.request.repository.LeaveRequestRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
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
            throw new FileErrorException("업로드 폴더를 생성할 수 없습니다. "+ uploadDir);
        }
    }

    /** 파일 업로드(pdf, png, jpeg, jpg) 메서드 */
    @Transactional
    public void uploadFile(List<MultipartFile> files, AttachmentUploadRequest request) {
        LeaveRequest leaveRequest = getLeaveRequest(request.getLeaveRequestId());
        User user = getUser(request.getUserId());

        files.forEach(f -> {
            // 빈 파일 체크
            if(fileIsEmpty(f)) throw new FileErrorException("Empty Upload File");
            // MIME 타입 체크
            if(!isAllowedMime(f.getContentType())) throw new FileErrorException("Not Allowed MIME Type : " + f.getContentType());
            // 용량체크
            assertFileSizeAllowed(f.getSize(), f.getContentType());

            String originalName = f.getOriginalFilename();
            String storedName = generateStoredFileName(originalName);
            String fullPath = getFullPath(storedName);
            File dest = new File(fullPath);

            try {
                f.transferTo(dest);
            } catch (IOException e) {
                throw new FileErrorException(e.getMessage());
            }

            LeaveRequestAttachment attachment = LeaveRequestAttachment.create(
                    originalName, storedName,
                    fullPath, f.getContentType(),
                    f.getSize(), user
            );

            leaveRequest.addAttachment(attachment);
        });
    }

    /** 파일 삭제(단일) */
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        LeaveRequestAttachment attachment = getAttachmentEntity(attachmentId);

        deleteAttachmentByDir(Path.of(attachment.getFilePath()));

        LeaveRequest parent = Objects.requireNonNull(attachment.getLeaveRequest());
        parent.removeAttachment(attachment);
    }

    @Transactional
    public void deleteAttachments(Long leaveRequestId) {
        List<LeaveRequestAttachment> attachments = getByLeaveRequestId(leaveRequestId);

        attachments.forEach(atc -> {
                    deleteAttachmentByDir(Path.of(atc.getFilePath()));
                    LeaveRequest parent = Objects.requireNonNull(atc.getLeaveRequest());
                    parent.removeAttachment(atc);
                }
        );
    }

    /** 디렉토리에서 첨부파일 삭제 */
    private void deleteAttachmentByDir(Path of) {
        try {
            Files.deleteIfExists(of);
        } catch (IOException e) {
            throw new FileErrorException("File Delete Error : " + e.getMessage());
        }
    }

    /** 해당 휴가 신청서의 파일들을 응답해주는 DTO 반환 */
    @Transactional(readOnly = true)
    public List<AttachmentResponse> getAttachments(Long leaveRequestId) {
        List<LeaveRequestAttachment> attachments = getByLeaveRequestId(leaveRequestId);

        return attachments.stream().map(AttachmentResponse::of).toList();
    }

    /** 해당 휴가 신청서의 파일들 조회 */
    private List<LeaveRequestAttachment> getByLeaveRequestId(Long leaveRequestId) {
        return attachmentRepository.findByLeaveRequestId(leaveRequestId);
    }

    /** 유저 엔티티 조회 */
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not Found User : " + userId));
    }

    /** 휴가 신청서 엔티티 조회 */
    private LeaveRequest getLeaveRequest(Long leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new NotFoundException("Not Found Leave Request : " + leaveRequestId));
    }

    /** 파일 엔티티 조회 */
    private LeaveRequestAttachment getAttachmentEntity(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Not Found File : " + attachmentId));
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

    /** 허용 MIME 목록 */
    private static final Set<String> ALLOWED_MIME = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg"
    );

    /** 허용 MIME 확인*/
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
            throw new FileErrorException(
                    String.format("파일 크기가 허용 범위를 초과했습니다. (형식: %s, 최대: %dMB)", mime, limit / MB)
            );
        }
    }
}
