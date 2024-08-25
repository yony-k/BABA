## 프로젝트 개요

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

</br>

## 프로젝트 관리

### 일정
| 날짜 | 활동 | 설명 |
| --- | --- | --- |
| 24.08.20 ~ 24.08.21 | 협업 기초 마련  | 팀 구성, 팀 규칙, 컨벤션(commit, PR, merge, issue) 정립 |
| 24.08.22 ~ 24.08.24 | 요구사항 기능 개발 | 담당 기능 구현 |
| 24.08.25 ~ 24.08.26  | 테스트 코드 및 문서 작성 | 테스트 코드 작성, API 명세서, 프로젝트 페이지, README 작성 |
| 24.08.27 | 코드 리뷰 및 배포  | 최종 코드 리뷰 및 배포 |

</br>

<details>
<summary><strong>이슈 관리</strong></summary>
![issue](https://github.com/user-attachments/assets/f74c4fb2-15ac-4a68-b040-5241ce8d1e29)

</details>

<details>
<summary><strong>컨벤션</strong></summary>

</details>

</br>

## 기술 문서

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

</br>

<details>
<summary><strong>ERD</strong></summary>
![BABA](https://github.com/user-attachments/assets/04cf41ff-91ac-4f11-a125-c6ca30743947)
</details>

<details>
<summary><strong>디렉토리 구조</strong></summary>

```bash
BABA
├── main
│   ├── java
│   │   └── org
│   │       └── example
│   │           └── baba
│   │               ├── BabaApplication.java
│   │               ├── common
│   │               │   ├── anotation
│   │               │   │   ├── PasswordValidator.java
│   │               │   │   └── ValidPassword.java
│   │               │   ├── config
│   │               │   │   ├── QueryDSLConfig.java
│   │               │   │   ├── RedisConfig.java
│   │               │   │   ├── SecurityConfig.java
│   │               │   │   ├── WebClientConfig.java
│   │               │   │   └── dsl
│   │               │   │       └── JwtFilterDsl.java
│   │               │   ├── entity
│   │               │   │   └── BaseTimeEntity.java
│   │               │   ├── enums
│   │               │   │   ├── StatisticsType.java
│   │               │   │   └── StatisticsValue.java
│   │               │   ├── property
│   │               │   │   └── YamlPropertySourceFactory.java
│   │               │   ├── redis
│   │               │   │   └── RedisRepository.java
│   │               │   ├── security
│   │               │   │   ├── controller
│   │               │   │   │   └── AuthController.java
│   │               │   │   ├── details
│   │               │   │   │   ├── AuthUser.java
│   │               │   │   │   └── MemberDetailService.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── LoginDto.java
│   │               │   │   │   └── MemberInfo.java
│   │               │   │   ├── filter
│   │               │   │   │   ├── JwtAuthenticationFilter.java
│   │               │   │   │   └── JwtVerificationFilter.java
│   │               │   │   ├── handler
│   │               │   │   │   ├── AuthenticationEntryPointHandler.java
│   │               │   │   │   ├── AuthenticationFailureCustomHandler.java
│   │               │   │   │   ├── LogoutSuccessCustomHandler.java
│   │               │   │   │   └── VerificationAccessDeniedHandler.java
│   │               │   │   └── service
│   │               │   │       └── AuthService.java
│   │               │   └── utils
│   │               │       ├── cookie
│   │               │       │   ├── CookieProperties.java
│   │               │       │   └── CookieUtils.java
│   │               │       ├── jwt
│   │               │       │   ├── JwtProperties.java
│   │               │       │   └── JwtProvider.java
│   │               │       └── translator
│   │               │           └── ObjectMapperUtils.java
│   │               ├── controller
│   │               │   ├── MemberController.java
│   │               │   ├── PostController.java
│   │               │   ├── StatisticsController.java
│   │               │   └── dto
│   │               │       ├── request
│   │               │       │   └── RegisterDTO.java
│   │               │       └── response
│   │               │           ├── PostDetailResponseDto.java
│   │               │           └── PostSimpleResponseDto.java
│   │               ├── domain
│   │               │   ├── ApprovalCode.java
│   │               │   ├── HashTag.java
│   │               │   ├── Member.java
│   │               │   ├── Post.java
│   │               │   ├── PostHashTagMap.java
│   │               │   ├── Register.java
│   │               │   └── enums
│   │               │       ├── MemberRole.java
│   │               │       └── SNSType.java
│   │               ├── exception
│   │               │   ├── CustomException.java
│   │               │   ├── CustomExceptionHandler.java
│   │               │   └── exceptionType
│   │               │       ├── AuthorizedExceptionType.java
│   │               │       ├── CommonExceptionType.java
│   │               │       ├── ExceptionType.java
│   │               │       ├── PostExceptionType.java
│   │               │       ├── RegisterExceptionType.java
│   │               │       ├── StatisticsExceptionType.java
│   │               │       └── UserExceptionType.java
│   │               ├── repository
│   │               │   ├── ApprovalCodeRepository.java
│   │               │   ├── MemberRepository.java
│   │               │   ├── PostRepository.java
│   │               │   ├── RegisterRepository.java
│   │               │   └── StatisticsRepository.java
│   │               └── service
│   │                   ├── MemberService.java
│   │                   ├── PostService.java
│   │                   └── StatisticsService.java
│   └── resources
│       ├── application.yml
│       └── data.sql
└── test
    └── java
        └── org
            └── example
                └── baba
                    ├── BabaApplicationTests.java
                    ├── common
                    │   ├── RedisTest.java
                    │   └── ValidAnotaionTest.java
                    └── service
                        ├── PostServiceTest.java
                        └── RegisterServiceTest.java
```
</details>

</br>

## 기능 구현

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

</br>

## 트러블 슈팅
