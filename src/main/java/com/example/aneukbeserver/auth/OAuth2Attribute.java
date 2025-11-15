package com.example.aneukbeserver.auth;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE) // Builder 메서드를 외부에서 사용하지 않으므로, Private
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
    private String attributeKey;
    private String email;
    private String name;
    private String provider;

    // 서비스에 따라 OAuth2Attribute 객체 생성
    static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, attributeKey, attributes);
            case "kakao":
                return ofKakao(provider, "email", attributes);
            case "naver":
                return ofNaver(provider, "id", attributes);
            default:
                throw new RuntimeException();
        }
    }

    // Google 로그인일 경우 사용하는 메서드
    // 사용자 정보가 따로 Wrappng 되지 않고 제공되어 바로 get 메서드로 접근 가능
    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .provider(provider)
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    // Kakao 로그인일 경우 사용하는 메서드
    // 필요한 사용자 정보가 kakaoAccount -> kakaoProfile 로 두번 감싸져 있음
    // get() 메서드를 두번 이용해ㅐ 사용자 정보를 꺼내야함
    private static OAuth2Attribute ofKakao(String provider, String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = kakaoAccount == null ? null : (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .email(kakaoAccount == null ? null : (String) kakaoAccount.get("email"))
                .name(kakaoProfile == null ? null : (String) kakaoProfile.get("nickname"))
                .provider(provider)
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofNaver(String provider, String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .attributes(response)
                .provider(provider)
                .attributeKey(attributeKey)
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);
        map.put("name", name);

        return map;
    }
}
