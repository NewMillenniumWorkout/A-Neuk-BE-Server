package com.example.aneukbeserver.auth;

import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용하여 OAuth2User 정보 가져오기
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttribute = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체 만들기
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttribute, oAuth2User.getAttributes());

        // OAuth2Attribute의 속성값들을 Map으로 반환
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        // 사용자 email 또는 id 가져오기
        String email = (String) memberAttribute.get("email");

        if (!StringUtils.hasText(email)) {
            log.warn("{} OAuth2 login attempt is missing an email address.", registrationId);
            throw new OAuth2AuthenticationException(
                    new OAuth2Error(
                            "missing_email",
                            "카카오 계정에서 이메일 제공에 동의해야 로그인할 수 있습니다.",
                            null
                    ),
                    "OAuth2 provider did not return an email address"
            );
        }

        // 이메일로 가입된 회원인지 조회
        Optional<Member> findMember = memberService.findByEmail(email);

        if(findMember.isEmpty()) {
            // 화원이 존재하지 않을 경우
            memberAttribute.put("exist", false);

            // 회원의 권한(회원이 존재하지 않으므로 기본 권한인 ROLE_USER 넣어줌), 회원 속성, 속성 이름을 이용해 DefaultOAuth2User 객체 생성 후 반환
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    memberAttribute, "email");
        }

        // 회원이 존재할 경우, memberAttribute의 exist 값을 true로 설정
        memberAttribute.put("exist", true);

        // 회원의 권한과, 회원 속성, 속성 이름을 이용해 DefaultOAuth2User 객체 생성 후 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_".concat(findMember.get().getUserRole()))),
                memberAttribute, "email"
        );
    }


}
