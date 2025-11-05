# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS builder

# 작업 디렉토리 생성 및 설정
WORKDIR /app

# build.gradle, gradlew, 그리고 필요한 파일 복사
COPY build.gradle /app/
COPY gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

# gradlew에 실행 권한 부여 및 Gradle로 프로젝트 빌드
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-jammy

# 작업 디렉토리 생성
WORKDIR /app

# 빌드 결과 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# 포트 노출
EXPOSE 7010

# 기존 ENTRYPOINT 리셋하고 Java 전체 경로로 실행
ENTRYPOINT []
CMD ["/opt/java/openjdk/bin/java", "-jar", "/app/app.jar"]
