# 프로젝트 소개
**태양광 패널 연동 웹사이트 XOLAR**
- 라즈베리파이(태양광패널)과의 연동
    - 백엔드 요청을 통한 AWS IoT 사물 등록
    - 백엔드 요청을 통해 사물에 MQTT 메세지 송신
    - 해당 사물(라즈베리파이)가 MQTT 메세지 수신
- 태양광 패널 정보 제공
- 카카오/구글/네이버 로그인

# 기간/인원
- 2024.09 ~ 2024.12
- 프론트엔드 1인, 백엔드 1인, 기획자 2인
  
# 성과
- 2024 Ewha Engineering Capstone Design Contest 금상

# 시연 영상
[![Video Label](http://img.youtube.com/vi/eu0A3vWxr-Q/0.jpg)](https://youtu.be/eu0A3vWxr-Q)

# 기술 스택
|통합 개발 환경| IntelliJ             |
|---|--------------------------------|
|Spring Boot 버전| 3.3.4              |
|데이터베이스| AWS RDS(MySQL), Redis   |
|IoT 연동| AWS IoT Core               |
|배포| AWS EC2(Ubuntu),S3, CodeDeploy |
|Project 빌드 관리 도구| Gradle        |
|CI/CD 툴| Github Actions             |
|ERD 다이어그램 툴| ERD Cloud          |
|Java version| Java 17                |

# 기술 아키텍처
![아키텍처구조-GitHub용](https://github.com/user-attachments/assets/867fbec0-5d6e-4c3c-9785-8d6b5970df96)

# ERD
![XOLAR_ERD](https://github.com/user-attachments/assets/ba00e696-e9ab-4621-94bf-88b39a70d880)

# API명세서
![스크린샷 2025-01-25 145148](https://github.com/user-attachments/assets/76bc9988-08c6-42ce-bc8e-27f836469e8d)
![스크린샷 2025-01-25 145205](https://github.com/user-attachments/assets/e0fedd44-8519-4dfb-9e80-8aa5c1bf3dc4)
