## 베이스 이미지로 OpenJDK 21 사용
#FROM openjdk:17-jdk-slim
#
## 애플리케이션 작업 디렉토리 설정
#WORKDIR /app
#
## JAR 파일을 컨테이너로 복사
#COPY ./build/libs/*.jar app.jar
#
## 애플리케이션 실행 명령어 설정
#ENTRYPOINT ["java", "-DSpring.profiles.active=dev", "-jar", "app.jar"]

# 1단계 : 빌드 단계
FROM gradle:7.6-jdk17 as builder

WORKDIR /home/app
# 프로젝트 전체 소스 복사 (루트 기준)
COPY . .
# Gradle 빌드 실행 모듈의 bootJar 생성
RUN gradle bootJar --no-daemon

# 2단계 : 실행 단계
# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17-slim

# 빌드 결과물 복사
COPY docker-compose.yml /app/docker-compose.yml
COPY .env /app/.env
COPY --from=builder /home/app/build/libs/*.jar trendly.jar

# 컨테이너 시작 시 실행할 명령어 지정
ENTRYPOINT ["java", "-DSpring.profiles.active=dev","-jar","trendly.jar"]
