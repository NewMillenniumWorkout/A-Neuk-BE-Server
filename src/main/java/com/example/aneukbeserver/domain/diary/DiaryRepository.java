package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByChat(Chat chat);
}
