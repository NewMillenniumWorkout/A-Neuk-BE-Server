package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.diary.DiaryAiResponseDTO;
import com.example.aneukbeserver.domain.diary.DiaryRepository;
import com.example.aneukbeserver.domain.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiaryService {
    @Autowired
    private DiaryRepository diaryRepository;

    public void saveDiary(Chat chat, Member member) {
        Diary diary = new Diary();
        diary.setChat(chat);
        diary.setMember(member);
        diary.setChat(chat);
        diaryRepository.save(diary);
    }
}
