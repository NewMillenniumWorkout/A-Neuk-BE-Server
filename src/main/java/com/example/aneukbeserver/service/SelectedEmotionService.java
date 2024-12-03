package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraph;
import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotion;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SelectedEmotionService {

    @Autowired
    private SelectedEmotionRepository selectedEmotionRepository;


    @Transactional
    public void saveSelectedEmotions(DiaryParagraph diaryParagraph, List<Emotion> emotionList) {
        for (Emotion emotion : emotionList) {
            SelectedEmotion selectedEmotion = new SelectedEmotion();
            selectedEmotion.setEmotion(emotion);
            selectedEmotion.setDiaryParagraph(diaryParagraph);
            selectedEmotionRepository.save(selectedEmotion);
        }
    }

}
