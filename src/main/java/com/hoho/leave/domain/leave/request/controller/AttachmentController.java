package com.hoho.leave.domain.leave.request.controller;

import com.hoho.leave.domain.leave.request.dto.request.AttachmentUploadRequest;
import com.hoho.leave.domain.leave.request.dto.response.AttachmentResponse;
import com.hoho.leave.domain.leave.request.service.AttachmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAttachment (
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("request") @Valid AttachmentUploadRequest request) throws Exception {

        attachmentService.uploadFile(files, request);

        return ResponseEntity.status(HttpStatus.OK).body("파일 업로드 성공");
    }

    @GetMapping("/{leaveRequestId}")
    public ResponseEntity<List<AttachmentResponse>> getAttachment(@PathVariable Long leaveRequestId) {

        List<AttachmentResponse> response = attachmentService.getAttachments(leaveRequestId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<?> getImages(@PathVariable String fileName) throws MalformedURLException {

        UrlResource resource = new UrlResource("file:" + attachmentService.getFullPath(fileName));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws MalformedURLException {

        UrlResource resource = new UrlResource("file:" + attachmentService.getFullPath(fileName));

        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
    
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Long attachmentId) {

        attachmentService.deleteAttachment(attachmentId);

        return ResponseEntity.status(HttpStatus.OK).body("파일 삭제 성공");
    }
}
