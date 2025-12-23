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

/**
 * 첨부파일 컨트롤러.
 * 
 * 휴가 신청서에 첨부되는 파일의 업로드, 조회, 다운로드, 삭제 기능을 제공한다.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;

    /**
     * 첨부파일을 업로드한다.
     *
     * @param files 업로드할 파일 목록
     * @param request 첨부파일 업로드 요청 정보
     * @return 파일 업로드 성공 메시지
     * @throws Exception 파일 업로드 중 발생하는 예외
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAttachment (
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("request") @Valid AttachmentUploadRequest request) throws Exception {

        attachmentService.uploadFile(files, request);

        return ResponseEntity.status(HttpStatus.OK).body("파일 업로드 성공");
    }

    /**
     * 특정 휴가 신청서의 첨부파일 목록을 조회한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @return 첨부파일 응답 목록
     */
    @GetMapping("/{leaveRequestId}")
    public ResponseEntity<List<AttachmentResponse>> getAttachment(@PathVariable Long leaveRequestId) {

        List<AttachmentResponse> response = attachmentService.getAttachments(leaveRequestId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 이미지 파일을 조회한다.
     *
     * @param fileName 파일명
     * @return 이미지 리소스
     * @throws MalformedURLException URL 형식이 잘못된 경우
     */
    @GetMapping("/images/{fileName}")
    public ResponseEntity<?> getImages(@PathVariable String fileName) throws MalformedURLException {

        UrlResource resource = new UrlResource("file:" + attachmentService.getFullPath(fileName));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * 파일을 다운로드한다.
     *
     * @param fileName 파일명
     * @return 다운로드할 파일 리소스
     * @throws MalformedURLException URL 형식이 잘못된 경우
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws MalformedURLException {

        UrlResource resource = new UrlResource("file:" + attachmentService.getFullPath(fileName));

        String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    /**
     * 첨부파일을 삭제한다.
     *
     * @param attachmentId 첨부파일 ID
     * @return 파일 삭제 성공 메시지
     */
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Long attachmentId) {

        attachmentService.deleteAttachment(attachmentId);

        return ResponseEntity.status(HttpStatus.OK).body("파일 삭제 성공");
    }
}
