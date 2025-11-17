package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {
    private static final String GUEST_EMAIL_TEMPLATE = "guest-%s@guest.local";
    private static final String GUEST_NAME_TEMPLATE = "Guest-%s";
    private static final String DEFAULT_ROLE = "ROLE_USER";

    @Autowired
    private MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public Member createGuestMember() {
        String guestCode;
        String guestEmail;

        do {
            guestCode = generateGuestCode();
            guestEmail = buildGuestEmail(guestCode);
        } while (memberRepository.findByEmail(guestEmail).isPresent());

        Member guest = new Member();
        guest.setEmail(guestEmail);
        guest.setName(String.format(GUEST_NAME_TEMPLATE, guestCode));
        guest.setUserRole(DEFAULT_ROLE);
        return memberRepository.save(guest);
    }

    private String generateGuestCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private String buildGuestEmail(String guestCode) {
        return String.format(GUEST_EMAIL_TEMPLATE, guestCode);
    }
}
