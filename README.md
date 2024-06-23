## About Repository 
협업 프로젝트 [ https://github.com/triptalk-4/triptalk-backend ] 포크 후 리팩토링 진행한 레포지토리

## Refactoring
- master : 메인 브랜치
- refactor/logstash : 로그스태시를 활용한 DB - 엘라스틱서치 동기화
- refactor/oauth : 소셜로그인 전략패턴 적용
- refactor/redis : MariaDB, ElasticSearch, Redis 에서 각 plannerTop6 조회 성능 테스트
- refactor/sync : Spring scheduler, Logstash 동기화 성능 테스트

<img width="100%" height="80%" src="https://github.com/triptalk-4/triptalk-backend/assets/81555158/a9a5ce4e-895c-4239-8208-444415413887"></img>

## About Project
<pre><code>"Triptalk" 은 사용자들이 자신의 여행 일정을 타임 라인 형식으로 손쉽게 공유할 수 있는 환경을 제공합니다. <br>
이를 통해 다른 사용자들은 더 편리하게 여행 일정을 확인하고, 댓글을 통해 다른 여행객들과 소통하며 가치 있는 정보를 교환할 수 있습니다. <br>
우리의 목표는 사용자들이 여행을 검색할 수 있는 다양한 방법을 구현하고, 후기를 공유하며, 소셜 로그인 기능을 통해 간편하게 접근할 수 있는 서비스입니다.
</code></pre>

- MVC패턴 적용 및 Swagger로 문서화
- 캐싱을 활용한 이메일인증 구현
- JWT 사용자 인증과 OAuth2를 활용한 소셜 로그인 구현
- RESTful API 일정 기능 구현
- ElasticSearch 검색 및 필터 기능
- 지도를 활용한 위치기반 여행지 검색 기능


## Architecture
<img width="100%" alt="architecture" src="https://github.com/triptalk-4/triptalk-backend/assets/129822965/8afe589b-6f38-4018-a7c6-45f4dac94f13">

## ERD
<img width=100% src="https://github.com/triptalk-4/triptalk-backend/assets/129822965/eb03b565-5dca-4a3f-a130-e3b42e7e1fa7">

## Skills
<img src="https://img.shields.io/badge/java-F46D01?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DA252?style=for-the-badge&logo=SpringBoot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JPA-418813?style=for-the-badge&logo=Spring Data JPA&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-569A31?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/QueryDSL-2E77BC?style=for-the-badge&logo=QueryDSL&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-173B3F?style=for-the-badge&logo=Gradle&logoColor=white"> <img src="https://img.shields.io/badge/Junit5-EF443B?style=for-the-badge&logo=Junit5&logoColor=white"> <img src="https://img.shields.io/badge/Mokito-01FF95?style=for-the-badge&logo=Mokito&logoColor=white"> <img src="https://img.shields.io/badge/MariaDB-1D2D35?style=for-the-badge&logo=MariaDB&logoColor=white"> <img src="https://img.shields.io/badge/Redis-E01F3D?style=for-the-badge&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/ElasticSearch-EAB300?style=for-the-badge&logo=ElasticSearch&logoColor=white"> <img src="https://img.shields.io/badge/RDS-137CBD?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/S3-DC2829?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/EC2-FF5100?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/Route 53-FF5722?style=for-the-badge&logo=amazonroute53&logoColor=white"> <img src="https://img.shields.io/badge/CodeDeploy-58A616?style=for-the-badge&logo=CodeDeploy&logoColor=white"> <img src="https://img.shields.io/badge/GitHub-000000?style=for-the-badge&logo=GitHub&logoColor=white"> <img src="https://img.shields.io/badge/Git Action-1D9BF0?style=for-the-badge&logo=githubactions&logoColor=white"> <img src="https://img.shields.io/badge/smtp-5BC4EE?style=for-the-badge&logo=smtp&logoColor=white"> <img src="https://img.shields.io/badge/oauth2-3333FF?style=for-the-badge&logo=oauth2&logoColor=white"> <img src="https://img.shields.io/badge/json web token-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"> <img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"> <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
