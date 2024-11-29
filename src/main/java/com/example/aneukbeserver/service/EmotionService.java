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
                    Emotion emotion = emotionRepository.findByTitle(title);
                    if (emotion == null) {
                        return null; // 존재하지 않는 경우 처리
                    }
                    EmotionDTO emotionDTO = new EmotionDTO();
                    emotionDTO.setId(emotion.getId());
                    emotionDTO.setExample(emotion.getExample());
                    emotionDTO.setCategory(emotion.getCategory());
                    emotionDTO.setTitle(emotion.getTitle());
                    emotionDTO.setDescription(emotion.getDescription());
                    return emotionDTO;
                })
                .filter(Objects::nonNull) // null 제거 (옵션)
                .collect(Collectors.toList());
        return emotions;
    }


}
