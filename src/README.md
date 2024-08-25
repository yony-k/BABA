## 프로젝트 개요

---

### 기술 스택
![java](https://img.shields.io/badge/Java-17-blue?logo=java)
![spring-boot](https://img.shields.io/badge/SpringBoot-3.2.2-grren?logo=springboot)


### 요구사항
[소셜 미디어 통합 Feed 서비스](https://www.notion.so/Feed-a419bee31618497db0c4d5c8486ef8a9?pvs=21)


### 팀 구성

| 이름  | 이메일 | 깃허브 | 역할                                     |
|-----| --- | --- |----------------------------------------|
| 유하진 | qwertygoov@naver.com | Hajin74 | 게시물 좋아요 API, 게시물 공유 API, 게시물 목록 조회 API |
| 김성은 | jinna0319@gmail.com | sung-silver | 통계 API                                 |
| 김연희 | kyh03179@gmail.com | yony-k | 사용자 회원가입 API, 사용자 가입승인 API             |
| 안소나 | objet_an@naver.com | sonaanweb | 게시물 상세 조회 API                          |
| 유서정 | bbwest0709@gmail.com | bbwest0709 | 사용자 로그인 API                            |


## 프로젝트 관리

---

### 일정

| 날짜 | 활동 | 설명 |
| --- | --- | --- |
| 24.08.20 ~ 24.08.21 | 협업 기초 마련  | 팀 구성, 팀 규칙, 컨벤션(commit, PR, merge, issue) 정립 |
| 24.08.22 ~ 24.08.24 | 요구사항 기능 개발 | 담당 기능 구현 |
| 24.08.25 ~ 24.08.26  | 테스트 코드 및 문서 작성 | 테스트 코드 작성, API 명세서, 프로젝트 페이지, README 작성 |
| 24.08.27 | 코드 리뷰 및 배포  | 최종 코드 리뷰 및 배포 |

### 이슈 관리
이미지 넣기

### 컨벤션
컨벤션 넣기

## 기술 문서

---

### API 명세서

[API 명세서 자세히보기](https://www.notion.so/API-197df8e5668f42baa79c96ffac873a47?pvs=21)

| API 명칭 | HTTP 메서드 | 엔드포인트 | 설명 |
| --- | --- | --- | --- |
| **사용자 회원가입** | POST | `/api/register` | 새로운 사용자를 등록합니다. |
| **사용자 로그인** | POST | `/api/login` | 사용자를 로그인시킵니다. |
| **사용자 로그아웃** | POST | `/api/logout`  | 사용자를 로그아웃시킵니다. |
| **게시물 목록 조회** | GET | `/api/posts` | 게시물 목록을 조회합니다. |
| **게시물 상세 조회** | GET | `/api/posts/{id}` | 특정 게시물의 상세 정보를 조회합니다. |
| **게시물 좋아요** | PUT | `/api/posts/{id}/like` | 게시물에 좋아요를 추가합니다. |
| **게시물 공유** | PUT | `/api/posts/{id}/share` | 게시물을 공유합니다. |
| **통계 조회** | GET | `/api/stats` | 게시물 통계 정보를 조회합니다. |

<details>
<summary><strong>ERD</strong></summary>
이미지 넣기
</details>

<details>
<summary><strong>디렉토리 구조</strong></summary>

```bash
/project-root
│
├── .github/                       # GitHub Actions 및 기타 설정
│   └── workflows/                 # CI/CD 파이프라인 정의 파일
│       └── ci-cd.yml
│
├── build/                         # 빌드 결과물
│
├── src/                           # 소스 코드
│   ├── main/                      # 메인 애플리케이션 코드
│   │   ├── java/                  # Java 소스 코드
│   │   │   └── com/               # 패키지 구조
│   │   │       └── example/       # 패키지 구조
│   │   │           └── myapp/     # 패키지 구조
│   │   │               ├── MyApplication.java
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               └── repository/
│   │   ├── resources/             # 리소스 파일
│   │   │   ├── application.properties
│   │   │   └── static/
│   │   │   └── templates/
│   │   └── webapp/                # 웹 애플리케이션 관련 리소스
│   │
│   ├── test/                      # 테스트 코드
│   │   ├── java/                  # Java 테스트 코드
│   │   │   └── com/               # 패키지 구조
│   │   │       └── example/       # 패키지 구조
│   │   │           └── myapp/     # 패키지 구조
│   │   │               ├── MyApplicationTests.java
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               └── repository/
│   │   └── resources/             # 테스트 리소스
│   │
├── Dockerfile                      # Docker 빌드 파일
├── .dockerignore                   # Docker 빌드 시 무시할 파일 목록
├── build.gradle                    # Gradle 빌드 파일
├── settings.gradle                 # Gradle 설정 파일
├── README.md                       # 프로젝트 설명 문서
├── .gitignore                      # Git 무시 파일 목록
└── .env                            # 환경 변수 파일

```
</details>

## 기능 구현

---

### 사용자 인증 시스템

- 사용자 회원가입 api
- 사용자 가입승인 api
- 사용자 로그인 api
- 비밀번호 암호화
- jwt token

### RESTful API

- 게시물 상세 api
- 게시물 목록 API
- 게시물 좋아요 api
- 게시물 공유 api

### 통계

- 기간 내 해당 해시태그가 포함된 게시물을 일자별로 제공
- 기간 내 해당 해시태그가 포함된 게시물을 시간별로 제공

## 트러블 슈팅

---

