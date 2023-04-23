package com.gdsc.blended.security;

import com.gdsc.blended.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2 반환 유저 정보
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle("id", attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        // google_account에 profile, email 정보 포함
        Map<String, Object> googleAccount = (Map<String, Object>)attributes.get("google_account");
        // profile에 nickname 정보 포함
        Map<String, Object> googleProfile = (Map<String, Object>)googleAccount.get("prfile");

        return OAuthAttributes.builder()
                .name((String) googleProfile.get("nickname"))
                .email((String) googleAccount.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .build();
    }
}
