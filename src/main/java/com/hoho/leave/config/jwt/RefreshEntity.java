package com.hoho.leave.config.jwt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String refresh;
    private String expiration;

    public RefreshEntity(String userEmail, String refresh, String expiration) {
        this.userEmail = userEmail;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
