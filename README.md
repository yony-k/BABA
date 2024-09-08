# BABA
1. [프로젝트 개요](#1-프로젝트-개요)
2. [프로젝트 관리](#2-프로젝트-관리)
3. [기술 문서](#3-기술-문서)
4. [기능 구현](#4-기능-구현)
5. [트러블 슈팅](#5-트러블-슈팅)

</br>

## 1. 프로젝트 개요

### ⚙️기술 스택
![java](https://img.shields.io/badge/Java-17-blue?logo=java)
![spring-boot](https://img.shields.io/badge/SpringBoot-3.2.2-grren?logo=springboot)
![mysql](https://img.shields.io/badge/MySQL-latest-blue?logo=mysql)

### ✔️ 요구사항
[소셜 미디어 통합 Feed 서비스](https://www.notion.so/Feed-a419bee31618497db0c4d5c8486ef8a9?pvs=21)

### 👩🏻‍💻 팀 구성

| 이름  | 이메일 | 깃허브 | 역할                                     |
|-----| --- | --- |----------------------------------------|
| 유하진 | qwertygoov@naver.com | Hajin74 | 게시물 좋아요 API, 게시물 공유 API, 게시물 목록 조회 API |
| 김성은 | jinna0319@gmail.com | sung-silver | 통계 API                                 |
| 김연희 | kyh03179@gmail.com | yony-k | 사용자 회원가입 API, 사용자 가입승인 API             |
| 안소나 | objet_an@naver.com | sonaanweb | 게시물 상세 조회 API                          |
| 유서정 | bbwest0709@gmail.com | bbwest0709 | 사용자 로그인 API                            |

</br>

## 2. 프로젝트 관리


<details>
<summary><strong>일정</strong></summary>

| 날짜 | 활동 | 설명 |
| --- | --- | --- |
| 24.08.20 ~ 24.08.21 | 협업 기초 마련  | 팀 구성, 팀 규칙, 컨벤션(commit, PR, merge, issue) 정립 |
| 24.08.22 ~ 24.08.24 | 요구사항 기능 개발 | 담당 기능 구현 |
| 24.08.25 ~ 24.08.26  | 테스트 코드 및 문서 작성 | 테스트 코드 작성, API 명세서, 프로젝트 페이지, README 작성 |
| 24.08.27 | 코드 리뷰 및 배포  | 최종 코드 리뷰 및 배포 |

</details>

<details>
<summary><strong>이슈 관리</strong></summary>
![issue](https://github.com/user-attachments/assets/f74c4fb2-15ac-4a68-b040-5241ce8d1e29)

</details>

<details>
<summary><strong>컨벤션</strong></summary>

- **Branch**
    - **전략**

      | Branch Type | Description |
                              | --- | --- |
      | `dev` | 주요 개발 branch, `main`으로 merge 전 거치는 branch |
      | `feature` | 각자 개발할 branch, 기능 단위로 생성하기, 할 일 issue 등록 후 branch 생성 및 작업 |

    - **네이밍**
        - `{header}/#{issue number}`
        - 예) `feat/#1`

- **커밋 메시지 규칙**
    ```bash
    > [HEADER] : 기능 요약
    
    - [CHORE]: 내부 파일 수정
    - [FEAT] : 새로운 기능 구현
    - [ADD] : FEAT 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 시
    - [FIX] : 코드 수정, 버그, 오류 해결
    - [DEL] : 쓸모없는 코드 삭제
    - [DOCS] : README나 WIKI 등의 문서 개정
    - [MOVE] : 프로젝트 내 파일이나 코드의 이동
    - [RENAME] : 파일 이름의 변경
    - [MERGE]: 다른 브렌치를 merge하는 경우
    - [STYLE] : 코드가 아닌 스타일 변경을 하는 경우
    - [INIT] : Initial commit을 하는 경우
    - [REFACTOR] : 로직은 변경 없는 클린 코드를 위한 코드 수정
    
    ex) [FEAT] 게시글 목록 조회 API 구현
    ex) [FIX] 내가 작성하지 않은 리뷰 볼 수 있는 버그 해결
    ```

- **Issue**
    ```bash
    📱 Description
    <!-- 진행할 작업을 설명해주세요 -->
    
    📱 To-do
    <!-- 작업을 수행하기 위해 해야할 태스크를 작성해주세요 -->
    [ ] todo1
    
    📱 ETC
    <!-- 특이사항 및 예정 개발 일정을 작성해주세요 -->
    ```

- **PR**
    - **규칙**
        - branch 작업 완료 후 PR 보내기
        - 항상 local에서 충돌 해결 후 remote에 올리기
        - PR 후 디스코드에 공유하기
        - 당일 PR은 당일에 리뷰하기
        - 최소 2명 이상의 동의를 받으면 merge하기
        - review 반영 후, 본인이 merge
      ```bash
          > [MERGE] {브랜치이름}/{#이슈번호}
          ex) [MERGE] setting/#1
      ```
    - **Template**
      ```bash
      📱 Description
      <!-- 진행할 작업을 설명해주세요 -->
      
      📱 To-do
      <!-- 작업을 수행하기 위해 해야할 태스크를 작성해주세요 -->
      [ ] todo1
      
      📱 ETC
      <!-- 특이사항 및 예정 개발 일정을 작성해주세요 -->
      ```
</details>

</br>

## 3. 기술 문서

### 📄 API 명세서

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

## 4. 기능 구현

### ⭐ 사용자 인증 시스템

#### ✨ 사용자 회원가입(담당: 김연희)
- 회원가입시 작성한 사용자 정보 유효성 검증 기능 구현
- 이메일, 계정명 중복 검증 기능 구현
- Redis를 활용한 임시 회원가입 기능 구현
- 비밀번호 BCrypt 처리 기능 구현
- 가입승인 코드 일치 시 정식 회원가입 기능 구현
<details>
    <summary>구현 의도</summary>
    <div>
        <div><strong>유효성 검증 기능</strong></div>
        <div>사용자 정보를 컨트롤러에서 매개변수로 받을 때 자동으로 유효성 검증이 이루어지도록 @Valid 어노테이션을 사용하여 기능을 구현했습니다. 비밀번호의 제약조건이 기존 어노테이션으로 검증이 불가하다고 판단되어 커스텀 어노테이션을 구현했고 덕분에 코드의 재사용이 가능해져 다른 프로젝트에서도 사용할 수 있게 되었습니다.</div>
        <div><strong>임시 회원가입 기능</strong></div>
        <div>사용자가 가입승인 코드만 받고 화면에서 벗어날 경우를 가정하여 가입승인 코드를 입력할 때 또 다시 정보를 입력할 필요가 없도록 가입승인 코드를 발급할 때 사용자 정보를 redis에 임시로 저장하는 방법을 택했습니다.</div>
    </div>
</details>
<details>
    <summary>구현 코드</summary>
    <div>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/blob/dev/src/main/java/org/example/baba/controller/MemberController.java" target="_blank">MemberController</a></br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/blob/dev/src/main/java/org/example/baba/service/MemberService.java" target="_blank">MemberService</a></br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/blob/dev/src/main/java/org/example/baba/service/SafeStoreService.java" target="_blank">SafeStoreService</a></br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/blob/dev/src/main/java/org/example/baba/common/anotation/ValidPassword.java" target="_blank">ValidPassword</a></br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/blob/dev/src/main/java/org/example/baba/common/anotation/PasswordValidator.java" target="_blank">PasswordValidator</a>
    </div>
</details>

#### ✨ 사용자 가입승인(담당: 김연희)
- SMTP를 활용한 가입승인 코드 발송 기능 구현
- 사용자가 재발송한 가입승인 코드와 Redis 에 저장된 가입승인 코드 비교 기능 구현
<details>
    <summary>구현 코드</summary>
    <div>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/blob/dev/src/main/java/org/example/baba/service/MailService.java" target="_blank">MailService</a>
    </div>
</details>

#### ✨ 사용자 로그인(담당: 유서정)
- 스프링 시큐리티 + JWT 방식을 사용한 로그인 기능 구현
- 핸들러를 이용한 필터체인 내 예외처리
- refreshToken을 활용한 토큰 재발급 기능 구현

</br>

### ⭐ RESTful API

#### ✨ 게시물 상세 (담당: 안소나)
- 특정 게시글의 모든 필드 값을 확인할 수 있는 페이지
- API 호출 시, 해당 게시물 조회수가 1 증가하는 기능 구현
<details>
    <summary>구현 의도</summary>
    <div>
        <div><strong>제목 1</strong></div>
        <div>내용 1</div>
        <div><strong>제목 2</strong></div>
        <div>내용 2</div>
    </div>
</details>
<details>
    <summary>구현 코드</summary>
</details>

#### ✨ 게시물 목록 (담당: 유하진)
- 여러 쿼리 파라미터를 받아 게시물 목록을 조회합니다.
- 해시태그, 검색기준, 검색키워드, 정렬기준, 정렬방향, SNS 타입, 페이지 번호, 페이지 크기를 쿼리 파라미터로 받습니다.
<details>
    <summary>구현 의도</summary>
    <div>
        <div><strong>ORM 대신 쿼리 사용</strong></div>
        <div>여러 테이블을 JOIN 하고 동적 조건을 처리해야 하기 때문에 ORM 대신 직접 쿼리를 작성했습니다. 추후에는 QueryDSL 을 도입하여 동적 쿼리 로직을 리팩토링할 계획입니다.</div>
    </div>
</details>
<details>
    <summary>구현 코드</summary>
    <div>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/pull/57/files" target="_blank">게시물 목록 조회</a> </br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/pull/73/files" target="_blank">게시물 목록 조회 테스트 코드</a>
    </div>
</details>


#### ✨ 게시물 좋아요, 게시물 공유 (담당: 유하진)
- 게시물 좋아요 수를 1 증가하는 기능을 구현했습니다.
- 게시물 공유 수를 1 증가하는 기능을 구현했습니다.
<details>
    <summary>구현 의도</summary>
    <div>
        <div><strong>외부 API 호출 로직 단순화</strong></div>
        <div>게시물에 좋아요/공유를 누르면 원래 해당 SNS의 게시물 좋아요/공유 수를 증가시키는 API를 호출해야 합니다. 현재는 실제 서비스가 아니기 때문에 외부 API를 호출하지 않지만, 추후 실제 API 호출을 쉽게 추가할 수 있도록 설계되었습니다.</div></br>
        <strong>Runnable 을 사용한 후속 작업 처리</strong></div>
        <div>외부 API 호출 성공 후, 실행될 작업을 Runnable 로 명시하여 callApiProcess()에게 전달하였습니다. 이를 통해 후속 작업을 유연하게 처리할 수 있도록 설계하였습니다.</div></br>
    </div>
</details>
<details>
    <summary>구현 코드</summary>
    <div>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/pull/31/files" target="_blank">게시물 좋아요</a></br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/pull/71/files" target="_blank">게시물 좋아요 테스트 코드</a></br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/pull/36/files" target="_blank">게시물 공유</a></br>
        <a href="https://github.com/wanted-pre-onboarding-backend-team-7/BABA/pull/72/files" target="_blank">게시물 공유 테스트 코드</a></br>
    </div>
</details>

### ⭐ 통계(담당: 김성은)
- 기간 내 해당 해시태그가 포함된 게시물을 일자별로 제공
- 기간 내 해당 해시태그가 포함된 게시물을 시간별로 제공
<details>
    <summary>구현 의도</summary>
    <div>
        <div><strong>제목 1</strong></div>
        <div>내용 1</div>
        <div><strong>제목 2</strong></div>
        <div>내용 2</div>
    </div>
</details>
<details>
    <summary>구현 코드</summary>
    <div>
        <a href="클래스 주소" target="_blank">클래스 이름</a></br>
        <a href="클래스 주소" target="_blank">클래스 이름</a></br>
        <a href="클래스 주소" target="_blank">클래스 이름</a></br>
    </div>
</details>

</br>

## 5. 트러블 슈팅

👉[연관관계 매핑 vs ID 매핑](https://www.notion.so/vs-ID-33e806be404f4962a0d2cd569f4c7040)

👉[비밀번호 검증 조건 충돌](https://www.notion.so/6ce83c5e61254bbbab80a26ad6eea811)

👉[단일 분리 책임](https://www.notion.so/23b9a48b2e0b4428b26a9af89f743009)
