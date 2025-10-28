package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.domain.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(
        name = "leave_request_attachment",
        uniqueConstraints = {
                // 같은 물리 파일 경로는 한 번만(스토리지 키가 유일하다면 권장)
                @UniqueConstraint(name = "uq_lra_file_path", columnNames = {"file_path"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaveRequestAttachment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "store_Name", nullable = false)
    private String storeName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private User uploadedBy;

    @Builder(access = AccessLevel.PROTECTED)
    public LeaveRequestAttachment(LeaveRequest leaveRequest, String originalName, String storeName, String filePath, String contentType, Long sizeBytes, User uploadedBy) {
        this.leaveRequest = leaveRequest;
        this.originalName = originalName;
        this.storeName = storeName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.uploadedBy = uploadedBy;
    }

    public static LeaveRequestAttachment create(LeaveRequest leaveRequest, String originalName, String storeName, String filePath, String contentType, Long sizeBytes, User uploadedBy) {
        return LeaveRequestAttachment.builder()
                .leaveRequest(leaveRequest)
                .originalName(originalName)
                .storeName(storeName)
                .filePath(filePath)
                .contentType(contentType)
                .sizeBytes(sizeBytes)
                .uploadedBy(uploadedBy).build();
    }
}

