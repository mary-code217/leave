package com.hoho.leave.domain.leave.request.service;

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

    // 파일 업로드(pdf, png, jpeg, jpg)
    public LeaveRequestAttachment uploadFile(MultipartFile files) {
        String originalFileName = files.getOriginalFilename();
        String storedName = generateStoredFileName(originalFileName);
        String fullPath = getFullPath(storedName);

        return null;
    }
    
    // 파일 가져오기

    // 로컬 저장소 Path
    public String getFullPath(String fileName) {
        return uploadDir + File.separator + fileName;
    }

    // 저장용 파일명 - UUID
    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
}
