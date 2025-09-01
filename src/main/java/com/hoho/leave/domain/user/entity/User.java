package com.hoho.leave.domain.user.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.user.dto.request.UserJoinRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uq_users_employee_no", columnNames = "employee_no")
        }
)
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;                  // 로그인/표시 이름(전사 유니크 필요하면 여기에 unique 추가 X, 위 테이블 레벨로)

    @Column(name = "password", nullable = false)
    private String password;                  // 해시 저장(BCrypt 60자, 여유를 두어 100)

    @Column(name = "email", nullable = false)
    private String email;                     // 로그인에 이메일을 쓴다면 NOT NULL + UNIQUE

    @Column(name = "employee_no", nullable = false)
    private String employeeNo;                // 사번(전사 유니크)

    @Column(name = "hire_date")
    private LocalDate hireDate;               // 선택(입사 전 생성 가능성 있으면 NULL 허용)

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;                    // ADMIN/APPROVER/USER (Security와 문자열 일치시킬 것)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;                        // 필수면 nullable=false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;                // 필수면 nullable=false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;                      // 필수면 nullable=false

    @Column(name = "is_active", nullable = false)
    private boolean active = true;            // 활성 여부(기본 true)


    public User(String username, String password, String email, String employeeNo, LocalDate hireDate, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.employeeNo = employeeNo;
        this.hireDate = hireDate;
        this.role = role;
    }

    public static User create(UserJoinRequest userJoinRequest) {
        return new User(
                userJoinRequest.getUsername(),
                userJoinRequest.getPassword(),
                userJoinRequest.getEmail(),
                userJoinRequest.getEmployeeNo(),
                userJoinRequest.getHireDate(),
                UserRole.valueOf("ROLE_"+userJoinRequest.getRole())
        );
    }

    public void assignGrade(Grade grade) {
        this.grade = grade;
    }

    public void assignPosition(Position position) {
        this.position = position;
    }

    public void assignTeam(Team team) {
        this.team = team;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateUsername(String username) {
        this.username = username;
    }
}
