# 베이스 이미지로 OpenJDK 21 사용
FROM openjdk:17-jdk-slim

# 애플리케이션 작업 디렉토리 설정
WORKDIR /app

# JAR 파일을 컨테이너로 복사
COPY ./build/libs/*.jar app.jar

# 애플리케이션 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "app.jar"]