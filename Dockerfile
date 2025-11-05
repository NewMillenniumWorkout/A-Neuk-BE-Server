# 1. Java 17의 Eclipse Temurin 이미지 사용 (OpenJDK 공식 후속)
FROM eclipse-temurin:17-jdk-jammy

# 2. 작업 디렉토리 생성 및 설정
WORKDIR /app

# 3. build.gradle, gradlew, 그리고 필요한 파일 복사
COPY build.gradle /app/
COPY gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

# 4. gradlew에 실행 권한 부여 및 Gradle로 프로젝트 빌드
RUN chmod +x ./gradlew
RUN ./gradlew bootJar
RUN mv build/libs/*.jar build/libs/app.jar

# 5. 빌드 결과를 어플리케이션으로 설정
EXPOSE 7010
ENTRYPOINT ["java", "-jar", "build/libs/app.jar"]
