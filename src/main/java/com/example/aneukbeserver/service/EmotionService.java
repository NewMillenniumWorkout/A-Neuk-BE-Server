package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.emotion.EmotionDTO;
import com.example.aneukbeserver.domain.emotion.EmotionRepository;
import com.example.aneukbeserver.domain.emotion.EmotionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmotionService {
    @Autowired
    private EmotionRepository emotionRepository;

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


}
