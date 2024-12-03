package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByChat(Chat chat);


    List<Diary> findAllByMember(Member member);

    List<Diary> findByMemberAndCreatedDate(Member member, LocalDate localDateTime); // 나중에 일기 하나만 생성되면 List 빼야함
}
