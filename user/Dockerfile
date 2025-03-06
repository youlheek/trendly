# 1단계 : 빌드 단계
FROM gradle:7.6-jdk17 as builder
WORKDIR /home/app
# 프로젝트 전체 소스 복사 (루트 기준)
COPY . .
# Gradle 빌드 실행 : user 모듈의 bootJar 생성
RUN gradle :user:bootJar --no-daemon

# 2단계 : 실행 단계
# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17-slim
# 빌드 결과물 복사 (user의 bootJar 결과물)
#터미널에서 C:\Users\LG\Desktop\trendly 디렉토리에서 docker build 명령어를 실행하면, 이 폴더가 빌드 컨텍스트의 루트가 됩니다.
COPY --from=builder /home/app/user/build/libs/*.jar user.jar
C:\Users\LG\Desktop\trendly\user

# 컨테이너 시작 시 실행할 명령어 지정
ENTRYPOINT ["java", "-DSpring.profiles.active=dev","-jar","user.jar"]

# 로컬의 JAR 파일을 컨테이너 안으로 복사, 복사한 파일의 이름은 user-api.jar
COPY build/libs/order-api.jar order-api.jar
# AWS 경로와 매핑하여 정확한 JAR 경로 지정
#COPY /var/lib/jenkins/workspace/cms-pipeline/order-api/build/libs/order-api.jar order-api.jar

ENTRYPOINT ["java", "-DSpring.profiles.active=dev","-jar","order-api.jar"]
