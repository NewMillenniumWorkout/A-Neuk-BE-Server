package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
