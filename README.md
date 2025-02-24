# Trendly

**Trendly**는 실시간 트렌드를 기반으로 인기 키워드를 분석하고, 해당 키워드에 따른 토론방(채팅방)을 제공하는 SNS 플랫폼입니다. 회원은 이메일로 가입하며, 각 토론방에서는 닉네임을 사용해 참여하고, 메시지 및 댓글에 좋아요를 남길 수 있습니다.

## 주요 기능

- **실시간 인기 검색어 분석**
    - 구글 트렌드 및 기타 트렌드 서비스를 활용하여 실시간으로 인기 키워드를 추출합니다.
- **토론(채팅) 기능**
    - 인기 키워드를 클릭하면 해당 토론방(채팅방)으로 바로 이동합니다.
    - 각 토론방에서는 회원이 닉네임으로 참여하여 메시지와 댓글을 작성합니다.
    - 각 메시지와 댓글에 좋아요 기능이 적용됩니다.
- **회원 관리**
    - 이메일 기반 가입/로그인, 회원 탈퇴 시 닉네임과 채팅 내역은 삭제되지만 토론 메시지는 유지됩니다.
    - 각 회원은 토론방 별로 다른 닉네임을 설정할 수 있습니다.
- **푸시 알림 및 고객 문의**
    - 특정 이벤트 발생 시 푸시 알림을 제공하며, 구글폼을 통해 고객 문의 기능을 지원합니다.

## 기술 스택

- **Backend:** Spring Boot 3.x (현재 3.4.2 사용 예정)
    - Spring Data JPA, Spring Data JDBC, Spring Web, Spring Security, Spring Boot Starter Data Redis 등 
- **Database:** MySQL (trendly 데이터베이스)
    - 개발/테스트 시 H2 Database 사용 가능
- **Real-time Communication:** WebSocket + STOMP (토론방 실시간 채팅)
- **Documentation:** SpringDoc OpenAPI (Swagger UI 제공)
- **Build Tool:** Gradle (또는 Maven)
- **기타:** Lombok, Apache Commons Lang3 등

## 프로젝트 구조

프로젝트는 **멀티모듈** 구조로 설계되어 있으며, 각 모듈은 아래와 같이 역할별로 구분되어 있습니다:

- **common:** 공통 DTO, 예외 처리, 유틸리티 등
- **module-user:** 회원 관련 기능 (가입, 로그인, 닉네임 관리 등)
- **module-discussion:** 토론(채팅) 기능, 메시지, 댓글, 좋아요 처리
- **module-trending:** 인기 검색어 분석 및 크롤링 연동
- **module-websocket:** WebSocket, STOMP, 실시간 통신 인프라
- **module-batch:** (필요 시) 배치 작업 및 스케줄링

> ※ 각 모듈은 DDD 개념(도메인, 애플리케이션, 인프라, 인터페이스 계층)을 일부 반영하여 설계되었습니다.

## 개발 환경 설정

1. **MySQL 데이터베이스 생성**

   MySQL 서버에 접속 후 아래 명령어를 실행하여 `trendly` 데이터베이스를 생성합니다.
   ```sql
   CREATE DATABASE trendly;
