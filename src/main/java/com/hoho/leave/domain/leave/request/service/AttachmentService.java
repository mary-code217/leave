package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import com.hoho.leave.domain.leave.request.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Value("${upload.local.dir}")
    private String uploadDir;

    public String getFullPath(String fileName) {
        return uploadDir + File.separator + fileName;
    }

    // 파일 업로드(pdf, png, jpeg, jpg)
    public LeaveRequestAttachment uploadFile() {
        return null;
    }
    
    // 파일 가져오기

    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
}
