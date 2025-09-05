# SpringBoot ToDo_Restful_API_Application
Spring Boot ToDo 관리 API

## Project Info
인턴 기간 동안 학습한 형상 관리, 아키텍처등을 활용하여 Spring Boot Restful 앱을 제작합니다.

## Dev Duration
- 2025-06-08 ~ 진행중

## Dev env
- __JDK__ : 21
- __Framework__ : SpringBoot 3.4.6
- __DB__ : PostgreSQL
- __Build Tool__ : Gradle

---

# 기본 정보

## 시작 하기
이 문서는 ToDo 앱 API에 대한 전반적 이해를 돕는 것을 목적으로 합니다.
개발이 진행 될 경우 해당 Read Me 문서는 업데이트 될 수 있습니다.

```
❗   현재는 단순 API 이지만 이용자 구분 및 로그인 기능이 개발 완료될 경우 토큰이 필요합니다.
    2025-09-05일자로 JWT 토큰이 개발완료되었습니다.
    토큰 사용법은 SWAGGER 문서에서 확인하실 수 있습니다.
```

## 규칙
- 모든 API 요청을 전송하는 기본 URL은 ```Localhost:8080/```입니다.
- ToDo_Restful_API는 데이터베이스 리소스에 대한 ```GET```, ```POST```, ```PATCH```, ```DELETE``` 요청을 통해 대부분 작업을 수행하는 등 RESTFul 규칙을 따릅니다.
- 모든 API 요청과 응답 본문은 JSON 형식으로 인코딩 됩니다.
- 속성 이름은 ```Kebab Case```, ```Snake Case```가 __아닌__ ```Camel Case```로 작성되었습니다.
- 시간 값(날짜와 일시)은 TimeZone 정보가 제외되며 날짜정보는 일시를 제외한 날짜(YYYY-MM-DD)만 포함합니다.

## 코드 샘플과 SDK
Swagger를 통해 각 Endpoint의 Sample Request, Response가 표시됩니다.
Request는 cURL을 이용하여 표시됩니다.
샘플을 사용하면 연결을 개발할 때 쉽게 Copy, Paste가 가능합니다.

## 상태 코드

### 성공 코드
|HTTP 상태      |설명                         |
|---------------|----------------------------|
|200            |성공적으로 처리된 요청       |

## 오류 코드
|Http 상태명    |```code```             |```message```           |
|---------------|-----------------------|------------------------|
|400            |'Not_found'            |'Resource Not Found'    |



## 패치노트
- springdoc를 통한 Swagger를 사용중이며 각각의 API별 상세 사항 및 변경 사항이 마크업 언어로 작성합니다.
