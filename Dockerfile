# LEV 1. 빌드 환경
FROM eclipse-temurin:21-jdk-alpine AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 래퍼와 빌드 파일들 복사
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src src/

# 실행 권한 부여 및 빌드
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# LEV 2. 실행 환경 (경량화)
FROM eclipse-temurin:22-jre-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출 (Spring Boot 기본 8080)
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]