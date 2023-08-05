package com.gdsc.blended.jwt.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.Getter;


@Getter
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;

    public GoogleOAuth2UserInfo(GoogleIdToken.Payload payload) {
        this.id = payload.getSubject();
        this.email = payload.getEmail();
        this.name = (String) payload.get("name");
        this.profileImageUrl = (String) payload.get("picture");
    }
}
