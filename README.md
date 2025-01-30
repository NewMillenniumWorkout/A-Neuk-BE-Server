# 아늑 (ANeuk): AI 기반 가짜 감정 중독 예방 및 감정 단어 확장 솔루션

![1](https://github.com/user-attachments/assets/0f15d485-f9c1-40ab-9677-d4a86e18f28f)

1. **일기 생성**  
   - AI챗봇과 하루를 돌아보는 대화를 통해 일기를 작성합니다.  

2. **감정 단어 추천 및 문장 재구성**  
   - 대화 및 일기를 기반으로 감정 단어를 추천받고 새로운 감정을 학습합니다.  
   - 선택된 감정을 반영하여 일기를 재구성합니다.  

3. **감정 도감**  
   - 총 414개의 감정 단어를 데이터화하여 사용자가 모은 감정을 시각적으로 확인할 수 있습니다.  

4. **감정 통계 제공**  
   - 최근 30일 동안 느꼈던 감정을 분석하여 통계 자료를 제공합니다.  

5. **추억 되돌아보기**  
   - 과거의 일기와 감정을 확인하며 자신의 감정 변화를 되돌아볼 수 있습니다.  

---

## 🌐 서비스 링크

🔗 **[서비스 바로가기](https://aneuk.dev-lr.com/)**

---

## 📄 개발 동기 및 목적

  ‘가짜 감정 중독’은 일상적인 감정 표현이 몇 가지 단어로 제한되며, 진짜 감정 대신 가짜 감정을 표현하는 데 익숙해지는 현상입니다. 많은 현대인들이 자신의 감정을 제대로 인식하거나 표현하지 못하며, 이는 감정인식 장애인 ‘감정불능증’과 관련이 있습니다. 연구에 따르면, 전체 인구의 약 10%가, 정신 및 신체적 질병 환자의 40~60%가 감정을 명확히 인식하거나 표현하지 못하고 있습니다. 
  
  한 연구의 조사결과, 한국의 청년 세대가 감정을 인식하고 표현하는 어려움 (DIDF) 부분에서 중장년 세대보다 유의미하게 높은 수치를 보였다는 결과도 있습니다. 이처럼 현대인들이 사회적 분위기와 기타 요소에 의해 자신의 감정 을 인식하고 표현하는 것에 어려움을 겪고 있다는 것을 알 수 있습니다. 이런 성향은 ‘감정불능증’, ‘가짜 감정 중독’으로 이어져서 자기 인식의 결여, 감정 조절 능력 저하 등 정신 건강에 문제를 일으킬 수 있습니다.   
  
  아늑은 다양한 감정 단어를 제시하여 이러한 문제를 해결하고자 합니다. 감정 단어는 실제 감정적 경험에 큰 영향을 미칩니다. 예를 들어, 타히티 사회에서는 ‘슬픔’이라는 단어가 없어 사람들이 슬픔을 명확히 인식하지 못하고 이를 질병으로 여겼습니다. 이는 감정을 표현하는 언어가 부족할 때 감정적 경험이 왜곡될 수 있음을 보여줍니다. 타히티와 다르게 한국어에는 434개의 감정 단어가 있지만, 현대인들이 사용하는 단어는 극히 제한적입니다. 
  
  이러한 배경에서 아늑은 AI 챗봇과의 대화를 통해 사용자가 자신의 진짜 감정을 인식하고 표현하도록 돕습니다. 이 서비스는 감정 표현에 어려움을 겪는 한국 청년층을 대상으로 하며, 일기 작성과 감정 단어 추천을 통해 사용자가 자신의 감정을 정확히 인식하면서 풍부하고 다채로운 감정적 경험을 할 수 있도록 돕는 것이 목표입니다.

---

## 🌟 프로젝트 소개  
기존 일기 앱은 단순히 텍스트 입력에 의존해 감정 표현이 제한적이고 정형화된 패턴에 머무르는 경우가 많습니다. 사용자는 자신의 감정을 명확히 인지하지 못하거나, 표현할 적절한 단어를 찾기 어려워 감정의 다양성을 경험하지 못하는 한계를 느낍니다. 또한, 감정적 경험을 시각적으로 정리하고 분석해주는 기능이 부족해 장기적인 자기 성찰이 어렵다는 문제점이 존재합니다. 

아늑은 이러한 문제를 다음과 같은 방법으로 해결합니다. 
1. LLM 모델을 활용해 사용자가 챗봇과 대화를 통해 일기를 쉽게 작성할 수 있는 환경을 제공합니다. 
2. 사용자에게 감정을 추천하고 선택하는 과정에서 자신의 내면을 깊이 이해할 수 있도록 돕습니다. 
3. 선택한 감정들로 일기를 재구성함으로써 감정 표현을 보다 세분화하고 자연스럽게 확장시킬 수 있습니다. 
4. 단어별 예문과 뜻을 제공해 사용자가 다양한 감정 단어를 학습하며 자기 표현력을 키울 수 있도록 돕습니다. 
5. 감정 단어들과 함께 그날의 사진을 일기와 함께 보여줌으로써 사용자 경험을 향상시킵니다. 
6. 사용한 감정 단어들에 대한 2가지 통계와 랜덤 일기 기능을 통해 과거의 감정을 되새
김으로써 자기 성찰의 기회를 제공합니다.

<div align="center">
  <img src=https://github.com/user-attachments/assets/064102c5-126f-4e3b-87fc-9e79fa4d0c9a width="800"/>
</div>
한국어 감정표현단어의 추출과 범주화라는 논문을 통해 데이터를 수집하고 정제하여 414
개의 감정단어 데이터베이스를 구축하였습니다. 감정 데이터베이스는 단순한 감정 목록이 아
닌, 감정의 세부 카테고리와 관련 예문 및 정의를 포함한 체계적인 구조로 이루어져 있습니다. 


<div align="center">
  <img src=https://github.com/user-attachments/assets/142f2747-f6b2-46cc-afe5-d763f12182b2 width="800"/>
</div>

또한, 과제 수행 과정에 GPT-4o mini 모델과 GPT-4o 모델을 적절히 체인의 형태로 조합
하여 활용했습니다. 채팅, 문장 분리, 감정 추천, 그리고 문장 재구성과 같은 반복적인 작업에
는 경량화된 GPT-4o mini 모델을 사용하여 효율성을 극대화했습니다. 반면, 일기 생성과 같은 가
장 중요한 핵심 작업에는 더 정교한 GPT-4o 모델을 사용해 높은 품질의 결과를 보장했습니다. 
이를 통해 비용을 최적화하고 시스템의 경제성과 성능을 모두 확보했습니다. 
또한, 아늑은 2가지 감정 통계를 제공합니다. 첫번째 감정통계는 최근 30일 동안 사용자가 
느낀 감정을 카테고리별로 분석하여 시각적으로 제공합니다. 두번째 감정도감은 사용자가 전체 
414개의 감정 중 어떤 감정을 경험했는지, 이를 카테고리별로 정리해 도감 형태로 보여줍니다.

---

## 🛠️ 개발 및 구현 방식  

### 기술 스택  
- **Frontend**: ![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB) ![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white) ![TailwindCSS](https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white) ![Context-API](https://img.shields.io/badge/Context--Api-000000?style=for-the-badge&logo=react)
- **Backend**: ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
- **Database**: ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
- **AI Server**: ![ChatGPT](https://img.shields.io/badge/chatGPT-74aa9c?style=for-the-badge&logo=openai&logoColor=white) ![FastAPI](https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi) ![LangChain](https://img.shields.io/badge/langchain-1C3C3C?style=for-the-badge&logo=LangChain&logoColor=white)
- **Deploy, CI/CD**: ![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

### 주요 구현  
1. **채팅 기반 일기 생성**
<div align="center">
  <img src=https://github.com/user-attachments/assets/e236b2bb-6d9f-4762-ad37-bee14592f855 width="500"/>
</div>

  - ChatGPT 기반의 대화 생성 및 일기 작성 알고리즘 설계  
  - 유저와 대화할 때 금지된 행동 및 핵심 가이드라인 설정  

3. **감정 추천 시스템**  
<div align="center">
  <img src=https://github.com/user-attachments/assets/cfce6f2d-4407-4b6e-a1f6-88192f06ffaf width="800"/>
</div>

   - 대분류 감정(11가지) 및 세부 감정(414개) 분류  
   - 감정 데이터 기반으로 문단별 감정을 추천
   - 선택한 감정을 기반으로 즉시 문장을 재구성   

5. **최종 일기 생성**  
<div align="center">
  <img src=https://github.com/user-attachments/assets/11d19ded-6596-4079-9bfa-45b20e93df0c width="800"/>
</div>

   - 재구성 된 문장들로 최종 일기 생성
   - 선택한 감정 단어, 뜻 그리고 예문을 확인 가능 

7. **감정 통계 및 도감**
<div align="center">
  <img src=https://github.com/user-attachments/assets/b0cadb24-88e2-4fb5-b223-df8535f76b0c width="800"/>
</div>

   - 감정 통계를 기반으로 사용자 맞춤형 데이터 시각화
   - 감정 도감을 통한 동기부여

### 아키텍처 

<div align="center">
  <img src=https://github.com/user-attachments/assets/354b211e-1681-40d6-8815-a63caaeea16a width="800"/>
</div>

---

## 📊 기대 효과  
- **일기 작성의 심리적 장벽 제거**: 사용자가 일기 작성을 편하게 느끼도록 유도  
- **감정적 인지 능력 강화**: 다양한 감정을 인식하고 학습  
- **정신적/신체적 건강 회복**: 감정 표현의 확장을 통해 정서적 건강 개선  

---

## 👥 팀 소개  
| 이름      | 역할         | GitHub                 | 주요 작업                      |
|-----------|--------------|----------------------------|------------------------------------|
| 김민식   | FE, Design  | [pius338](https://github.com/pius338) | UI/UX 설계 및 디자인, 프론트엔드 개발 |
| 장민석   | AI, Infra    | [minseok128](https://github.com/minseok128) | AI 서버 및 인프라 관리              |
| 김여진   | BE, DB       | [LUCETE012](https://github.com/LUCETE012) | 백엔드 및 데이터베이스 설계         |

