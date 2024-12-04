package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.emotion.*;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotion;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmotionService {
    @Autowired
    private EmotionRepository emotionRepository;

    @Autowired
    private SelectedEmotionRepository selectedEmotionRepository;

    public EmotionResponseDTO getEmotionInfo(Long id) {
        Optional<Emotion> emotion = emotionRepository.findById(id);

        if(emotion.isEmpty()) return null;

        EmotionResponseDTO emotionResponseDTO = new EmotionResponseDTO();
        emotionResponseDTO.setEmotion_id(id);
        emotionResponseDTO.setEmotion_name(emotion.get().getTitle());
        emotionResponseDTO.setDescription(emotion.get().getExample());
        emotionResponseDTO.setCategory(emotion.get().getCategory());

        return emotionResponseDTO;
    }

    public List<EmotionDTO> getEmotionDetail(List<String> titles) {
        List<EmotionDTO> emotions = titles.stream()
                .map(title -> {
                    Optional<Emotion> emotion = emotionRepository.findByTitle(title);
                    if (emotion.isEmpty()) {
                        return null; // 존재하지 않는 경우 처리
                    }
                    EmotionDTO emotionDTO = new EmotionDTO();
                    emotionDTO.setId(emotion.get().getId());
                    emotionDTO.setExample(emotion.get().getExample());
                    emotionDTO.setCategory(emotion.get().getCategory());
                    emotionDTO.setTitle(emotion.get().getTitle());
                    emotionDTO.setDescription(emotion.get().getDescription());
                    return emotionDTO;
                })
                .filter(Objects::nonNull) // null 제거 (옵션)
                .collect(Collectors.toList());
        return emotions;
    }

    public List<Emotion> getEmotionObjectsByNames(List<String> emotionNames) {
        return emotionNames.stream().map(emotionName -> emotionRepository.findByTitle(emotionName)
                .orElseThrow(() -> new IllegalArgumentException("Emotion not found with name: " + emotionName)))
                .collect(Collectors.toList());
    }


    public Map<String, Long> get30daysEmotionCategory(Member member) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        // 감정 데이터 가져오기
        List<SelectedEmotion> selectedEmotions = selectedEmotionRepository.findByDiaryParagraph_Diary_MemberAndDiaryParagraph_Diary_CreatedDateBetween(member, startDate, endDate);

        // 감정 데이터를 카테고리별로 그룹화
        Map<String, Long> stats = selectedEmotions.stream()
                .map(SelectedEmotion::getEmotion)
                .collect(Collectors.groupingBy(
                        emotion -> emotion.getCategory().name(), // Enum -> 문자열
                        Collectors.counting()
                ));

        // 모든 카테고리를 0으로 초기화한 뒤 기존 결과와 병합
        Map<String, Long> result = new HashMap<>();
        EmotionCategory.getAllCategories().forEach(category -> result.put(category, 0L)); // 없는 카테고리를 0으로 초기화
        result.putAll(stats); // 기존 결과 병합

        return result;
    }
}
