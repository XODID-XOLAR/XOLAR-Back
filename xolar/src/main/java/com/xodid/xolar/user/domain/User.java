package com.xodid.xolar.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id",updatable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    @Builder
    public User(String name, String email, SocialType socialType, String socialId) {
        this.name = name;
        this.email = email;
        this.socialType = socialType;
        this.socialId = socialId;
    }
}
