package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "leave_request_attachment",
        uniqueConstraints = {
                // 같은 물리 파일 경로는 한 번만(스토리지 키가 유일하다면 권장)
                @UniqueConstraint(name = "uq_lra_file_path", columnNames = {"file_path"})
        }
)
public class LeaveRequestAttachment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;        // FK 필수

    @Column(name = "file_name", nullable = false)
    private String fileName;                  // 원본 파일명(표시용)

    @Column(name = "file_path", nullable = false)
    private String filePath;                  // 스토리지 키/경로(유일)

    @Column(name = "content_type", nullable = false)
    private String contentType;               // MIME 타입

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;                   // 바이트 크기(>=0)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private User uploadedBy;            // 업로더(필수)
}

