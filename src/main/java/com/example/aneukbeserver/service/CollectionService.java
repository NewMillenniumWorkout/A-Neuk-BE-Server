package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.collection.Collection;
import com.example.aneukbeserver.domain.collection.CollectionRepository;
import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.emotion.EmotionCategory;
import com.example.aneukbeserver.domain.emotion.EmotionRepository;
import com.example.aneukbeserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private EmotionRepository emotionRepository;


    public Map<String, Object> getEmotionCollection(Member member) {
        Map<String, Object> statistics = new HashMap<>();

        long totalEmotions = emotionRepository.count(); // 혹시 감정이 추가될수도 있으니 ..

        // 특정 멤버의 Collection
        List<Collection> collections = collectionRepository.findByMember(member);

        // 사용한 전체
        long usedEmotions = collections.stream()
                .map(collection -> collection.getEmotion().getId())
                .distinct()
                .count();

        statistics.put("usedEmotionCount", usedEmotions);
        statistics.put("totalEmotionCount", totalEmotions);

        Map<EmotionCategory, Long> totalByCategory = emotionRepository.countByCategoryGrouped().stream()
                .collect(Collectors.toMap(EmotionRepository.CategoryCount::getCategory, EmotionRepository.CategoryCount::getCount));

        // 카테고리별
        Map<String, Long> categoryUsageStats = collections.stream()
                .collect(Collectors.groupingBy(
                        collection -> collection.getEmotion().getCategory().toString(),
                        Collectors.counting()
                ));

        // 모든 카테고리를 0으로 초기화한 뒤 기존 결과와 병합
        Map<String, Long> result = new HashMap<>();
        EmotionCategory.getAllCategories().forEach(category -> result.put(category, 0L)); // 없는 카테고리를 0으로 초기화
        result.putAll(categoryUsageStats); // 기존 결과 병합

        Map<String, Object> categoryStats = new HashMap<>();
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            String category = entry.getKey();
            long count = entry.getValue();

            EmotionCategory categoryEnum = EmotionCategory.valueOf(category);
            long totalInCategory = totalByCategory.getOrDefault(categoryEnum, 0L);

            categoryStats.put(category,  Map.of(
                    "usedCount", count,
                    "totalCount", totalInCategory
            ));
        }
        statistics.put("categoryStats", categoryStats);

        return statistics;
    }

    public void saveEmotionCollection(List<Emotion> emotionList, Member member) {
        if (emotionList == null || emotionList.isEmpty()) {
            return;
        }

        Map<Long, Emotion> uniqueEmotions = new HashMap<>();
        Map<Long, Long> emotionCounts = new HashMap<>();
        for (Emotion emotion : emotionList) {
            if (emotion == null || emotion.getId() == null) {
                continue;
            }
            uniqueEmotions.putIfAbsent(emotion.getId(), emotion);
            emotionCounts.put(emotion.getId(), emotionCounts.getOrDefault(emotion.getId(), 0L) + 1);
        }

        if (uniqueEmotions.isEmpty()) {
            return;
        }

        List<Collection> existingCollections = collectionRepository.findByMemberAndEmotionIn(
                member,
                new ArrayList<>(uniqueEmotions.values())
        );
        Map<Long, Collection> existingByEmotionId = existingCollections.stream()
                .collect(Collectors.toMap(collection -> collection.getEmotion().getId(), collection -> collection));

        List<Collection> toSave = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : emotionCounts.entrySet()) {
            Long emotionId = entry.getKey();
            Long count = entry.getValue();
            Emotion emotion = uniqueEmotions.get(emotionId);
            if (emotion == null) {
                continue;
            }

            Collection collection = existingByEmotionId.get(emotionId);
            if (collection != null) {
                collection.setUsageCount(collection.getUsageCount() + count);
            } else {
                collection = new Collection();
                collection.setMember(member);
                collection.setEmotion(emotion);
                collection.setUsageCount(count);
            }
            toSave.add(collection);
        }

        if (!toSave.isEmpty()) {
            collectionRepository.saveAll(toSave);
        }


    }
}
