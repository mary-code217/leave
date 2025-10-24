package com.hoho.leave.domain.user.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.user.dto.request.UserJoinRequest;
import com.hoho.leave.domain.user.service.PasswordEncoder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

    public User(String email, UserRole role) {
        this.email = email;
        this.role = role;
    }

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

    public void assignGrade(Grade grade) {
        this.grade = grade;
    }

    public void assignPosition(Position position) {
        this.position = position;
    }

    public void assignTeam(Team team) {
        this.team = team;
    }

    public void updatePassword(String newPassword, PasswordEncoder encoder) {
        this.password = encoder.encode(newPassword);
    }

    public void updateUsername(String username) {
        this.username = username;
    }
}
