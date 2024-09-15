# 🙌 미니 프로젝트 숙박 예약 서비스 🙌

## 프로젝트 기간

■ 기간

- 2024.06.17 - 2024.07.05, 3주간

■ 참여인원

- FE 3명, BE 5명

---

## 👥팀원 구성

|                                                                                                                                                                               **김재민**                                                                                                                                                                                |                                                                                                                                                                             **이상권 (팀장)**                                                                                                                                                                             |                                                                                                                                                                               **천문기**                                                                                                                                                                                |                                                                                                                                                                                **하정훈**                                                                                                                                                                                |                                                                                                        **한보름**                                                                                                         |
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                                                                                                                                                         숙소, 좋아요, 로그, 이메일 알림                                                                                                                                                                          |                                                                                                                                                                                CI/CD                                                                                                                                                                                 |                                                                                                                                                                              예약하기, 예약조회                                                                                                                                                                              |                                                                                                                                                                       회원, 회원 가입, 로그인, Security                                                                                                                                                                        |                                                                                                        객실, 홍일점                                                                                                         |
| <img src="https://quickest-asterisk-75d.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F3ef8dbd9-414c-4cf5-813d-32ecb943cc67%2F2dbca82b-7c3c-4502-8501-e4d37c9f2bd8%2FUntitled.png?table=block&id=3d3ea1fa-5b0f-4e87-b382-70f054a0996a&spaceId=3ef8dbd9-414c-4cf5-813d-32ecb943cc67&width=920&userId=&cache=v2" height=150 width=250> | <img src="https://quickest-asterisk-75d.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F3ef8dbd9-414c-4cf5-813d-32ecb943cc67%2Ff264df27-5456-47e8-b61a-6a0b5d25f0fe%2FUntitled.png?table=block&id=dea6f74f-79f8-4788-90ba-9ccfeeafdbf0&spaceId=3ef8dbd9-414c-4cf5-813d-32ecb943cc67&width=580&userId=&cache=v2" height=150 width=250> | <img src="https://quickest-asterisk-75d.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F3ef8dbd9-414c-4cf5-813d-32ecb943cc67%2Fcebc16f2-a9dd-484e-ad61-643f56571cc5%2FIMG_8668.jpg?table=block&id=4bcf8240-cb53-40e4-948c-cf97ede1baf8&spaceId=3ef8dbd9-414c-4cf5-813d-32ecb943cc67&width=860&userId=&cache=v2" height=150 width=250> | <img src="https://quickest-asterisk-75d.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F3ef8dbd9-414c-4cf5-813d-32ecb943cc67%2F888a2b66-dbd4-478c-9e54-e0ca54daab89%2FUntitled.png?table=block&id=513c1af8-41c4-4c93-a6f4-5dcfec9c2e26&spaceId=3ef8dbd9-414c-4cf5-813d-32ecb943cc67&width=1240&userId=&cache=v2" height=150 width=250> | <img src="https://cdn.discordapp.com/attachments/1252087757308952717/1258712099522084915/image.png?ex=66890a7e&is=6687b8fe&hm=7f9acce7d1083aa403e2cdc01094e94a003bd3653fc15782e6508103542bd30c&" height=150 width=250> |

---

## 🧱 아키텍쳐

![architecture.jpeg](src/main/resources/images/architecture.jpeg)

---

## 구현 내용

- ■ [요구사항](https://drive.google.com/file/d/1A6jWwX5rhc4_OXAa8RYKm9JgU7vzTmiR/view?usp=sharing) 필수 기능
    - 회원
        - 회원 가입, 로그인
    - 숙소
        - 카테고리 별 숙소 조회
    - 객실
        - 객실 전체 조회
        - 객실 상세 조회
    - 예약
        - 예약하기
        - 예약 내역 조회
- 예약 시 이메일 전송 서비스
- 좋아요, 좋아요 목록 조회

---

## 🔨기술 스택 및 도구

- **■ Develop**
    - Java 17
    - Spring boot 3.3.0
    - MySQL
    - Spring security
    - Java-jwt
    - Swagger


- **■ Test**
    - Junit
    - mockito
    - h2

---

## ERD

![erd.png](src/main/resources/images/erd.png)

---

## API List

- ### [swagger](http://15.165.4.237/swagger-ui/index.html#/)

- ### API List

  ![apiList.png](src/main/resources/images/api-list/apiList.png)

---

## 결과물

- **구현 사이트** : [wanderlust](https://wanderlust-mini4.netlify.app/)

---

### ■ 회원 가입

![signUp.png](src/main/resources/images/api-list/signUp.png)

### ■ 로그인

![login.png](src/main/resources/images/api-list/login.png)

### ■ 숙소 조회

![accommodationReadAll.png](src/main/resources/images/api-list/accommodationReadAll.png)

### ■ 객실 리스트 조회

![productReadAll.png](src/main/resources/images/api-list/productReadAll.png)

### ■ 객실 상세 조회

![productDetail.png](src/main/resources/images/api-list/productDetail.png)

### ■ 좋아요 클릭

![likeClick.png](src/main/resources/images/api-list/likeClick.png)

### ■ 좋아요 목록 조회

![likeReadAll.png](src/main/resources/images/api-list/likeReadAll.png)

### ■ 예약하기

![reservation.png](src/main/resources/images/api-list/reservation.png)

### ■ 이메일 알림

![emailAlert.png](src/main/resources/images/api-list/emailAlert.png)

### ■ 예약 내역 조회

![reservationHistory.png](src/main/resources/images/api-list/reservationHistory.png)

### ■ 로그

![log.png](src/main/resources/images/api-list/log.png)

---

## Directory Structure

```bash
Project
│  .gitignore
│  build.gradle
│  Dockerfile
│  gradlew
│  gradlew.bat
│  README.md
│  settings.gradle
│
├─.github
│  └─workflows
│          ci.yml
│          deploy.yml
│
├─.gradle
│  ├─8.8
│  │  │  gc.properties
│  │  │
│  │  ├─checksums
│  │  │      checksums.lock
│  │  │
│  │  ├─dependencies-accessors
│  │  │      gc.properties
│  │  │
│  │  ├─executionHistory
│  │  │      executionHistory.lock
│  │  │
│  │  ├─expanded
│  │  ├─fileChanges
│  │  │      last-build.bin
│  │  │
│  │  ├─fileHashes
│  │  │      fileHashes.lock
│  │  │
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  │      buildOutputCleanup.lock
│  │      cache.properties
│  │
│  └─vcs-1
│          gc.properties
│
├─.idea
│  │  .name
│  │  compiler.xml
│  │  gradle.xml
│  │  jarRepositories.xml
│  │  jpa.xml
│  │  misc.xml
│  │  modules.xml
│  │  vcs.xml
│  │  workspace.xml
│  │
│  └─modules
│          Travel-Mini.main.iml
│
├─gradle
│  └─wrapper
│          gradle-wrapper.jar
│          gradle-wrapper.properties
│
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─travel
    │  │          │  KdtBe8MiniProjectApplication.java
    │  │          │
    │  │          ├─domain
    │  │          │  ├─accommodation
    │  │          │  │  ├─controller
    │  │          │  │  │      AccommodationController.java
    │  │          │  │  │
    │  │          │  │  ├─dto
    │  │          │  │  │  └─response
    │  │          │  │  │          AccommodationDetailListResponse.java
    │  │          │  │  │          AccommodationImageResponse.java
    │  │          │  │  │          AccommodationOptionResponse.java
    │  │          │  │  │          AccommodationResponse.java
    │  │          │  │  │
    │  │          │  │  ├─entity
    │  │          │  │  │      Accommodation.java
    │  │          │  │  │      AccommodationImage.java
    │  │          │  │  │      AccommodationOption.java
    │  │          │  │  │
    │  │          │  │  ├─repository
    │  │          │  │  │      AccommodationRepository.java
    │  │          │  │  │
    │  │          │  │  └─service
    │  │          │  │          AccommodationService.java
    │  │          │  │
    │  │          │  ├─email
    │  │          │  │  └─service
    │  │          │  │          EmailService.java
    │  │          │  │
    │  │          │  ├─like
    │  │          │  │  ├─controller
    │  │          │  │  │      LikeController.java
    │  │          │  │  │
    │  │          │  │  ├─dto
    │  │          │  │  │  ├─request
    │  │          │  │  │  │      LikeRequest.java
    │  │          │  │  │  │
    │  │          │  │  │  └─response
    │  │          │  │  │          LikeResponse.java
    │  │          │  │  │
    │  │          │  │  ├─entity
    │  │          │  │  │      Like.java
    │  │          │  │  │
    │  │          │  │  ├─repository
    │  │          │  │  │      LikeRepository.java
    │  │          │  │  │
    │  │          │  │  └─service
    │  │          │  │          LikeService.java
    │  │          │  │
    │  │          │  ├─member
    │  │          │  │  ├─controller
    │  │          │  │  │      MemberController.java
    │  │          │  │  │
    │  │          │  │  ├─dto
    │  │          │  │  │  ├─request
    │  │          │  │  │  │      LoginRequest.java
    │  │          │  │  │  │      SignupRequest.java
    │  │          │  │  │  │
    │  │          │  │  │  └─response
    │  │          │  │  │          LoginDto.java
    │  │          │  │  │          LoginResponse.java
    │  │          │  │  │          MemberResponse.java
    │  │          │  │  │
    │  │          │  │  ├─entity
    │  │          │  │  │      Member.java
    │  │          │  │  │
    │  │          │  │  ├─repository
    │  │          │  │  │      MemberRepository.java
    │  │          │  │  │
    │  │          │  │  └─service
    │  │          │  │          MemberService.java
    │  │          │  │
    │  │          │  ├─product
    │  │          │  │  ├─controller
    │  │          │  │  │      AuthProductController.java
    │  │          │  │  │      ProductController.java
    │  │          │  │  │
    │  │          │  │  ├─dto
    │  │          │  │  │  └─response
    │  │          │  │  │          ProductDetailResponse.java
    │  │          │  │  │          ProductImageResponse.java
    │  │          │  │  │          ProductOptionResponse.java
    │  │          │  │  │          ProductResponse.java
    │  │          │  │  │          ProductSimpleResponse.java
    │  │          │  │  │
    │  │          │  │  ├─entity
    │  │          │  │  │      Product.java
    │  │          │  │  │      ProductImage.java
    │  │          │  │  │      ProductInfoPerNight.java
    │  │          │  │  │      ProductOption.java
    │  │          │  │  │
    │  │          │  │  ├─repository
    │  │          │  │  │      ProductInfoPerNightRepository.java
    │  │          │  │  │      ProductRepository.java
    │  │          │  │  │
    │  │          │  │  └─service
    │  │          │  │          ProductService.java
    │  │          │  │
    │  │          │  └─reservations
    │  │          │      ├─controller
    │  │          │      │      ReservationController.java
    │  │          │      │
    │  │          │      ├─dto
    │  │          │      │  ├─request
    │  │          │      │  │      ReservationRequest.java
    │  │          │      │  │
    │  │          │      │  └─response
    │  │          │      │          ReservationHistoryListResponse.java
    │  │          │      │          ReservationHistoryResponse.java
    │  │          │      │          ReservationResponse.java
    │  │          │      │
    │  │          │      ├─entity
    │  │          │      │      Reservation.java
    │  │          │      │
    │  │          │      ├─repository
    │  │          │      │      ReservationRepository.java
    │  │          │      │
    │  │          │      └─service
    │  │          │              ReservationService.java
    │  │          │
    │  │          └─global
    │  │              ├─annotation
    │  │              │      TokenMemberId.java
    │  │              │
    │  │              ├─aspect
    │  │              │      LoggingAspect.java
    │  │              │
    │  │              ├─config
    │  │              │      AsyncConfig.java
    │  │              │      CacheConfig.java
    │  │              │      ObjectMapperConfig.java
    │  │              │      SecurityConfig.java
    │  │              │      SwaggerConfig.java
    │  │              │      WebConfig.java
    │  │              │
    │  │              ├─decorator
    │  │              │      MailTaskDecorator.java
    │  │              │
    │  │              ├─exception
    │  │              │  │  AccommodationException.java
    │  │              │  │  AuthException.java
    │  │              │  │  EmailException.java
    │  │              │  │  MemberException.java
    │  │              │  │  ProductException.java
    │  │              │  │  ReservationsException.java
    │  │              │  │
    │  │              │  ├─handler
    │  │              │  │      AsyncExceptionHandler.java
    │  │              │  │      GlobalExceptionHandler.java
    │  │              │  │      TravelApiExceptionHandler.java
    │  │              │  │
    │  │              │  └─type
    │  │              │          ErrorType.java
    │  │              │
    │  │              ├─interceptor
    │  │              │      AuthorizationInterceptor.java
    │  │              │
    │  │              ├─jwt
    │  │              │      JwtProvider.java
    │  │              │      JwtTokenUtility.java
    │  │              │
    │  │              ├─model
    │  │              │  └─entity
    │  │              │          TimeStamp.java
    │  │              │
    │  │              ├─resolver
    │  │              │      TokenMemberIdResolver.java
    │  │              │
    │  │              └─util
    │  │                      DateValidationUtil.java
    │  │
    │  └─resources
    │      │  application-prod.yml
    │      │  application.yml
    │      │  logback-spring-dev.xml
    │      │  logback-spring-prod.xml
    │      │  logback-spring.xml
    │      │
    │      └─templates
    │              reservation-confirmation.html
    │
    └─test
        ├─java
        │  └─com
        │      └─travel
        │          │  KdtBe8MiniProjectApplicationTests.java
        │          │
        │          ├─domain
        │          │  ├─accommodation
        │          │  │  ├─controller
        │          │  │  │      AccommodationControllerTest.java
        │          │  │  │
        │          │  │  ├─repository
        │          │  │  │      AccommodationRepositoryTest.java
        │          │  │  │
        │          │  │  └─service
        │          │  │          AccommodationServiceTest.java
        │          │  │
        │          │  ├─like
        │          │  │  ├─controller
        │          │  │  │      LikeControllerTest.java
        │          │  │  │
        │          │  │  ├─repository
        │          │  │  │      LikeRepositoryTest.java
        │          │  │  │
        │          │  │  └─service
        │          │  │          LikeServiceTest.java
        │          │  │
        │          │  ├─member
        │          │  │  ├─controller
        │          │  │  │      MemberControllerTest.java
        │          │  │  │
        │          │  │  ├─repository
        │          │  │  │      MemberRepositoryTest.java
        │          │  │  │
        │          │  │  └─service
        │          │  │          MemberServiceTest.java
        │          │  │
        │          │  ├─product
        │          │  │  ├─controller
        │          │  │  │      ProductControllerTest.java
        │          │  │  │
        │          │  │  ├─repository
        │          │  │  │      ProductInfoPerNightRepositoryTest.java
        │          │  │  │      ProductRepositoryTest.java
        │          │  │  │
        │          │  │  └─service
        │          │  │          ProductServiceTest.java
        │          │  │
        │          │  └─reservations
        │          │      ├─controller
        │          │      │      ReservationControllerTest.java
        │          │      │
        │          │      ├─repository
        │          │      │      ReservationRepositoryTest.java
        │          │      │
        │          │      └─service
        │          │              ReservationServiceTest.java
        │          │
        │          └─script
        │                  concurrentTest.js
        │                  script.js
        │
        └─resources
                application.yml

```

# 트러블 슈팅

### 이메일 전송 속도

- 동기 방식으로 이메일 전송 시 1건당 약 6초의 소요시간 발생
    - @Async을 통해 비동기 방식으로 변경하여 약 1초대로 단축 (약 83.33%)

  ![troubleShooting_async.png](src/main/resources/images/trobule-shooting/troubleShooting_async.png)

    - 수정 전
      ![troubleShooting_emailSendBefore.png](src/main/resources/images/trobule-shooting/troubleShooting_emailSendBefore.png)

    - 수정 후
      ![troubleShooting_emailSendAfter.png](src/main/resources/images/trobule-shooting/troubleShooting_emailSendAfter.png)

---

### 숙소 조회 N+1

- Fetch Join + Lazy + BatchSize 로 쿼리문 단축 → BatchSize 어노테이션을 사용하면 오히려 성능이 저하되는 이슈가 발생.
    - 이유는 모든 숙소의 데이터를 다 가져오게되어 불필요한 쿼리가 추가됨
    - accommodation 엔티티에서 BatchSize 어노테이션을 제거하여 해결

      ![troubleShooting_accommodationN+1_1.png](src/main/resources/images/trobule-shooting/troubleShooting_accommodationN+1_1.png)

      ![troubleShooting_accommodationN+1_2.png](src/main/resources/images/trobule-shooting/troubleShooting_accommodationN+1_2.png)

- 성능문제
    - 캐싱 → 인메모리 데이터베이스에 캐싱하여 조회 속도를 개선
      ![troubleShooting_cashing.png](src/main/resources/images/trobule-shooting/troubleShooting_cashing.png)
