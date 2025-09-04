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
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "employee_no", nullable = false)
    private String employeeNo;

    @Column(name = "hire_date")
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


    public User(String username, String password, String email, String employeeNo, LocalDate hireDate, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.employeeNo = employeeNo;
        this.hireDate = hireDate;
        this.role = role;
    }

    public User(String email, UserRole role) {
        this.email = email;
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
