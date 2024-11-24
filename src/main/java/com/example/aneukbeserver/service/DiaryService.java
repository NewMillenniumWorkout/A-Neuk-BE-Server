package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chat.ChatRepository;
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

    @Autowired
    private ChatRepository chatRepository;

    public void saveDiary(Chat chat, Member member) {
        Diary diary = new Diary();
        diary.setChat(chat);
        diary.setMember(member);
        diary.setChat(chat);
        diaryRepository.save(diary);
    }

    public Diary getByChatId(Long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);

        if (chat.isEmpty()) return null;

        Diary diary = diaryRepository.findByChat(chat.get());

        return diary;

    }
}
