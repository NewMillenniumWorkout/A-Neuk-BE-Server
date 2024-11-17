package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.diary.DiaryAiResponseDTO;
import com.example.aneukbeserver.domain.diary.DiaryRepository;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraph;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraphDTO;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraphRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class DiaryParagraphService {
    @Autowired
    private DiaryParagraphRepository diaryParagraphRepository;

    @Autowired
    private DiaryRepository diaryRepository;

    @Transactional
    public void saveParagraphs(Chat chat, DiaryAiResponseDTO response) {
        List<DiaryParagraphDTO> paragraphDTOList = response.getContent_list();
        Diary diary = diaryRepository.findByChat(chat);
        log.info(String.valueOf(chat.getId()));
        log.info("diary: " + diary.getId());
        paragraphDTOList.forEach(dto -> {
                    DiaryParagraph diaryParagraph = new DiaryParagraph();
                    diaryParagraph.setOrderIndex(dto.getOrder_index());
                    diaryParagraph.setOriginalContent(dto.getOriginal_content());
                    diaryParagraph.setDiary(diary);

                    diaryParagraphRepository.save(diaryParagraph);
        });

    }
}
