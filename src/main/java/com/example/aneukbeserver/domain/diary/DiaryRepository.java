package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByChat(Chat chat);


    List<Diary> findAllByMember(Member member);
}
