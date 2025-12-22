package com.hoho.leave.domain.user.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.user.dto.request.UserJoinRequest;
import com.hoho.leave.domain.user.service.PasswordEncoder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 사용자 엔티티.
 * <p>
 * 시스템 사용자의 계정 정보와 조직 정보를 관리한다.
 * </p>
 */
@Entity
@Getter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uq_users_employee_no", columnNames = "employee_no")
        }
)
@NamedEntityGraph(
        name = "User.withOrg",
        attributeNodes = {
                @NamedAttributeNode("team"),
                @NamedAttributeNode("grade"),
                @NamedAttributeNode("position")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "employee_no", nullable = false)
    private String employeeNo;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    /**
     * 사용자를 생성한다.
     *
     * @param email 사용자 이메일
     * @param role 사용자 권한
     */
    public User(String email, UserRole role) {
        this.email = email;
        this.role = role;
    }

    /**
     * 회원가입 요청으로부터 사용자 엔티티를 생성한다.
     *
     * @param userJoinRequest 회원가입 요청 정보
     * @param encoder 비밀번호 인코더
     * @return 생성된 사용자 엔티티
     */
    public static User create(UserJoinRequest userJoinRequest, PasswordEncoder encoder) {
        User user = new User();

        user.username = userJoinRequest.getUsername();
        user.password = encoder.encode(userJoinRequest.getPassword());
        user.email = userJoinRequest.getEmail();
        user.employeeNo = userJoinRequest.getEmployeeNo();
        user.hireDate = userJoinRequest.getHireDate();
        user.role = UserRole.valueOf("ROLE_"+userJoinRequest.getRole());

        return user;
    }

    /**
     * 사용자에게 직급을 할당한다.
     *
     * @param grade 할당할 직급
     */
    public void assignGrade(Grade grade) {
        this.grade = grade;
    }

    /**
     * 사용자에게 직책을 할당한다.
     *
     * @param position 할당할 직책
     */
    public void assignPosition(Position position) {
        this.position = position;
    }

    /**
     * 사용자에게 부서를 할당한다.
     *
     * @param team 할당할 부서
     */
    public void assignTeam(Team team) {
        this.team = team;
    }

    /**
     * 사용자의 비밀번호를 변경한다.
     *
     * @param newPassword 새로운 비밀번호
     * @param encoder 비밀번호 인코더
     */
    public void updatePassword(String newPassword, PasswordEncoder encoder) {
        this.password = encoder.encode(newPassword);
    }

    /**
     * 사용자의 이름을 변경한다.
     *
     * @param username 새로운 이름
     */
    public void updateUsername(String username) {
        this.username = username;
    }
}
