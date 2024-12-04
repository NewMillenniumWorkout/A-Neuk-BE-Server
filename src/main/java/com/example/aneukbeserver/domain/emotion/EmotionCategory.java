package com.example.aneukbeserver.domain.emotion;

import java.util.Arrays;
import java.util.List;

public enum EmotionCategory {
    통증,
    슬픔,
    지루,
    혐오,
    기쁨,
    중성,
    놀람,
    공포,
    기타,
    흥미,
    분노;


    // 모든 카테고리를 문자열로 반환
    public static List<String> getAllCategories() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toList();
    }
}
