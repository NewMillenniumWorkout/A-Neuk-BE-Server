package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.emotion.EmotionRepository;
import com.example.aneukbeserver.domain.emotion.EmotionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        return emotionResponseDTO;


    }
}
