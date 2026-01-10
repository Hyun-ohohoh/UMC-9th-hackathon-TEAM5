# 🎮 경도팟 (Kyung-Do-Pot)

사용자의 실시간 위치를 기반으로 주변의 게임 방을 찾고, **경찰(경)과 도둑(도)**이 되어 긴장감 넘치는 추격전을 벌이는 위치 기반 실시간 게임 서비스입니다.

---

## 🏗 Server Architecture

본 서비스는 안정적인 트래픽 처리와 실시간 데이터 전송을 위해 다음과 같은 클라우드 인프라 구조를 갖추고 있습니다.

<p align="center">
  <img src="https://github.com/user-attachments/assets/c973d407-618f-4c93-a376-83fee7f41a04" alt="인프라 구조도" width="850">
</p>

### 💡 Infrastructure Details
* **Nginx (Reverse Proxy):** 443(HTTPS) 및 WSS 연결을 처리하며 SSL Termination을 수행하고 내부 8080 포트로 전달합니다.
* **WebSocket (STOMP):** 도둑 검거, 탈옥 등 게임 내 핵심 이벤트를 실시간으로 참여자들에게 중계합니다.
* **CI/CD:** **GitHub Actions**를 통해 메인 브랜치 푸시 시 자동 빌드 후 SSH/SCP를 통해 **AWS EC2**에 배포됩니다.
* **Persistence & Storage:** **AWS RDS (MySQL)**를 데이터베이스로 사용하며, **AWS S3**와 **Presigned URL**을 이용해 이미지를 안전하게 업로드합니다.

---

## 🛠 Tech Stack

### **Framework & Language**
* **Spring Boot:** 3.5.3
* **Java:** 21
* **Build Tool:** Gradle

### **Database & Infrastructure**
* **Database:** AWS RDS (MySQL 8.0), H2 (Local/Dev)
* **Infrastructure:** AWS EC2, Nginx, AWS S3
* **CI/CD:** GitHub Actions

### **Major Libraries**
* **Security:** Spring Security, JWT (jjwt 0.11.5)
* **Real-time:** Spring WebSocket (STOMP)
* **API Doc:** Springdoc OpenAPI (Swagger) 2.8.6
* **Utils:** Lombok, ModelMapper, Caffeine Cache

---

## ✨ Key Features

1. **위치 기반 주변 방 조회:** 사용자 현재 위치 기반 반경 3,000m 내 방 목록 조회 및 거리 계산
2. **실시간 게임 엔진:** 방장의 역할 배정 및 게임 상태(`WAITING` -> `PLAYING` -> `FINISHED`) 관리
3. **실시간 인터랙션:** 도둑 검거(Capture) 및 탈옥(Release) 이벤트의 실시간 WebSocket 중계
4. **도착 확인 시스템:** 방장 전용 참여자 현장 도착 체크 및 `VERIFIED` 상태 업데이트
5. **보안 이미지 처리:** S3 Presigned URL을 통한 다이렉트 이미지 업로드 및 조회

---

## 🌲 Development Convention

### **Branch Strategy**
* `main`: 배포 가능한 안정적인 코드가 유지되는 브랜치
* `develop`: 기능 개발의 기준이 되는 브랜치
* `feat/#[이슈번호]-title`: 기능 개발
* `fix/#[이슈번호]-title`: 버그 수정
* `refac/#[이슈번호]-title`: 리팩토링

### **Commit Message Format**
`[Type]/#[이슈번호]: [Description]`
* `feat`: 새로운 기능 추가
* `fix`: 버그 수정
* `docs`: 문서 변경
* `refactor`: 코드 리팩토링
* `chore`: 빌드 업무 수정, 패키지 매니저 설정 등

---

## 📂 Project Structure

```text
com.example.demo/
├── common/              # 공통 엔티티(BaseEntity) 및 표준 API 응답(ApiResponse) 정의
├── domain/              # 비즈니스 도메인 모듈
│   ├── room/            # 방 생성, 조회 및 주변 방 검색
│   ├── room_member/     # 참가자 관리, 팀 배정, 검거 및 탈옥 서비스
│   ├── room_game/       # 게임 상태 및 종료 조건 관리
│   └── user/            # 사용자 인증 및 세션 관리
└── global/              # 전역 설정
    ├── config/          # Swagger, S3, WebSocket, Security 설정
    ├── exception/       # 비즈니스 예외 처리 핸들러 및 에러 코드 정의
    ├── intercepter/     # Logging 및 JWT 인증 인터셉터
    └── infra/           # S3 서비스 등 외부 인프라 연동
