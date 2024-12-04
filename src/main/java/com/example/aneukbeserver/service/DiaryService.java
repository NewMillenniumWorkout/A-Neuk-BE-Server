package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chat.ChatRepository;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.diary.DiaryRepository;
import com.example.aneukbeserver.domain.diary.*;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraph;
import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotion;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiaryService {
    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private ChatRepository chatRepository;

    public void saveDiary(Chat chat, Member member) {
        Diary diary = chat.getDiary();
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

    public Long getDiaryIdByChatId(Chat chat) {
        return diaryRepository.findByChat(chat).getId();
    }

    public Optional<Diary> getDiary(Long diaryId) {
        return diaryRepository.findById(diaryId);
    }

    public String mergeParagraph(List<DiaryParagraph> paragraphs) {
        return paragraphs.stream()
                .sorted(Comparator.comparing(DiaryParagraph::getOrderIndex))
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
                    List<Emotion> emotionList = diary.getParagraphs().stream()
                            .flatMap(paragraph -> paragraph.getEmotionList().stream())
                            .map(SelectedEmotion::getEmotion) // Emotion 객체를 반환
                            .toList();
                    DiaryDTO diaryDTO = new DiaryDTO();
                    diaryDTO.setDiary_id(diary.getId());
                    diaryDTO.setDate(diary.getCreatedDate());
                    diaryDTO.setContent(mergeParagraph(diary.getParagraphs()));
                    diaryDTO.setImageUrl(diary.getImageUrl());
                    diaryDTO.setEmotionList(emotionList);
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
                        MonthDiaryDTO dto = new MonthDiaryDTO(diary.getCreatedDate(), diary.getId());
                        monthDiaries.add(dto);
                    }
                }
        );

        return monthDiaries;
    }

    public DiaryDTO getDateDiary(Member member, String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Diary> diaries = diaryRepository.findByMemberAndCreatedDate(member, localDate);

        if (diaries.isEmpty()) {
            throw new EntityNotFoundException("Diary not found for member and date");
        }

        Diary diary = diaries.get(diaries.size() - 1);
//        Diary diary = diaryRepository.findByMemberAndCreatedDate(member, localDate);

        List<Emotion> emotionList = diary.getParagraphs().stream()
                .flatMap(paragraph -> paragraph.getEmotionList().stream())
                .map(SelectedEmotion::getEmotion) // Emotion 객체를 반환
                .toList();

        return new DiaryDTO(diary.getId(), localDate, mergeParagraph(diary.getParagraphs()), diary.getImageUrl(), emotionList);
    }

    public Optional<Diary> getRandomDiary(Member member) {
        List<Diary> diaries = diaryRepository.findAllByMember(member);
        if(diaries.isEmpty()) return Optional.empty();

        return Optional.of(diaries.get(new Random().nextInt(diaries.size())));
    }

    public void saveImage(Diary diary, String image) {
        diary.setImageUrl(image);
        diaryRepository.save(diary);
    }

}

