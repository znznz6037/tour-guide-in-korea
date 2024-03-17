package com.example.tourguideinkorea.oauth2.entity;

import com.example.tourguideinkorea.oauth2.enums.Role;
import com.example.tourguideinkorea.oauth2.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GoogleUser {
    public String id;
    public String email;
    public Boolean verifiedEmail;
    public String name;
    public String givenName;
    public String familyName;
    public String picture;
    public String locale;

    public GoogleUser() {}

    public User toUser() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .email(this.email)
                .name(this.name)
                .givenName(this.givenName)
                .picture(this.picture)
                .locale(this.locale)
                .role(Role.USER)
                .socialType(SocialType.GOOGLE)
                .build();
    }
}