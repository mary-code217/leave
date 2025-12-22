package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 휴가 신청 첨부파일 엔티티.
 * <p>
 * 휴가 신청서에 첨부되는 파일 정보를 관리한다.
 * </p>
 */
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * 첨부파일 엔티티를 생성한다.
     *
     * @param originalName 원본 파일명
     * @param storeName 저장된 파일명
     * @param filePath 파일 경로
     * @param contentType 컨텐츠 타입
     * @param sizeBytes 파일 크기 (bytes)
     * @param uploadedBy 업로드한 사용자
     * @return 생성된 첨부파일 엔티티
     */
    public static LeaveRequestAttachment create(String originalName, String storeName, String filePath,
                                                String contentType, Long sizeBytes, User uploadedBy) {
        LeaveRequestAttachment attachment = new LeaveRequestAttachment();

        attachment.originalName = originalName;
        attachment.storeName = storeName;
        attachment.filePath = filePath;
        attachment.contentType = contentType;
        attachment.sizeBytes = sizeBytes;
        attachment.uploadedBy = uploadedBy;

        return attachment;
    }

    /**
     * 휴가 신청을 설정한다.
     *
     * @param leaveRequest 휴가 신청
     */
    protected void addLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }
}

