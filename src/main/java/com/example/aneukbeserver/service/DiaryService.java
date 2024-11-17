package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.diary.*;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraph;
import com.example.aneukbeserver.domain.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Long getDiaryIdByChatId(Chat chat) {
        return diaryRepository.findByChat(chat).getId();
    }

    public Optional<Diary> getDiary(Long diaryId) {
        return diaryRepository.findById(diaryId);
    }

    public String mergeParagraph(List<DiaryParagraph> paragraphs) {
        return paragraphs.stream()
                .map(paragraph -> paragraph.getFinalContent() != null
                                ? paragraph.getFinalContent()
                        : paragraph.getOriginalContent()
                ).collect(Collectors.joining());
    }

    public List<DiaryDTO> getAllDiary(Member member) {
        List<Diary> diaries= diaryRepository.findAllByMember(member);
        List<DiaryDTO> diaryDTOS = new ArrayList<>();

        diaries.forEach(
                diary -> {
                    DiaryDTO diaryDTO = new DiaryDTO();
                    diaryDTO.setDiary_id(diary.getId());
                    diaryDTO.setDate(diary.getCreatedDate().toLocalDate());
                    diaryDTO.setContent(mergeParagraph(diary.getParagraphs()));

                    diaryDTOS.add(diaryDTO);
                }
        );

        return diaryDTOS;
    }

    public List<MonthDiaryDTO> getMonthDiary(Member member, String month) {
        List<Diary> diaries = diaryRepository.findAllByMember(member);

        List<MonthDiaryDTO> monthDiaries = new ArrayList<>();

        diaries.forEach(
                diary ->
                {
                    String diaryMonth = diary.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));

                    if(diaryMonth.equals(month)) {
                        MonthDiaryDTO dto = new MonthDiaryDTO(diary.getCreatedDate().toLocalDate(), diary.getId());
                        monthDiaries.add(dto);
                    }
                }
        );

        return monthDiaries;
    }
}
