package com.example.tourguideinkorea.oauth2.entity;

import com.example.tourguideinkorea.entity.BaseTimeEntity;
import com.example.tourguideinkorea.oauth2.enums.Role;
import com.example.tourguideinkorea.oauth2.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    private String id;

    private String email; // 이메일
    private String name; // 닉네임
    private String givenName; // 닉네임
    private String picture; // 프로필 이미지
    private String locale;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String email;
        private String name;
        private String picture;
        private Role role;

        public Response(User user) {
            this.email = user.getEmail();
            this.name = user.getName();
            this.picture = user.getPicture();
            this.role = user.getRole();
        }
    }
}
