package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.domain.leave.request.dto.request.AttachmentUploadRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import com.hoho.leave.domain.leave.request.repository.AttachmentRepository;
import com.hoho.leave.util.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

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
    public LeaveRequestAttachment uploadFile(MultipartFile files, AttachmentUploadRequest request) {
        // 빈 파일 체크
        if(fileIsEmpty(files)) throw new BusinessException("파일이 존재하지 않습니다.");

        // MIME 타입 확인
        if(isAllowedMime(files.getContentType())) throw new BusinessException("허용하지 않는 확장자 입니다.");

        // 용량체크
        

        String originalFileName = files.getOriginalFilename();
        String storedName = generateStoredFileName(originalFileName);
        String fullPath = getFullPath(storedName);

        return null;
    }

    /** 파일 가져오는 메서드 */

    /** 로컬 저장소 Path */
    private String getFullPath(String fileName) {
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
}
