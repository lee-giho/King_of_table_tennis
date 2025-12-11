<img src="https://capsule-render.vercel.app/api?type=waving&height=250&color=0:ff7eb3,100:87CEEB&text=탁구왕&fontSize=60&fontAlignY=30&animation=fadeIn&rotate=0&desc=실시간%20방송%20기능을%20갖춘%20탁구%20경기%20매칭%20애플리케이션%20&descSize=30&reversal=false&fontColor=ffffff" style="width: 120%;">

# 목차
- [🚀 프로젝트 소개 🚀](#-프로젝트-소개-)
  - [프로젝트 개요 및 배경](#프로젝트-개요-및-배경)
  - [프로젝트 목표](#프로젝트-목표)
- [🛠 프로젝트 설계 🏗](#-프로젝트-설계-)
  - [기술 스택](#기술-스택)
    - [✔ Frond-End](#-frond-end)
    - [✔ Back-End](#-back-end)
    - [✔ Database](#-database)
    - [✔ Cloud](#-cloud)
    - [✔ DevOps](#-devops)
    - [✔ Dev tools](#-dev-tools)
  - [🏗 시스템 아키텍처 🏛](#-시스템-아키텍처-)
  - [Spring Boot 요청 처리 흐름 (with JWT Authentication \& Exception Handling)](#spring-boot-요청-처리-흐름-with-jwt-authentication--exception-handling)
    - [주요 흐름](#주요-흐름)
    - [특징](#특징)
  - [🛠 ERD 🗂](#-erd-)
- [📈 서버 안정성 개선](#-서버-안정성-개선)
- [🛠 핵심 기능 🏗](#-핵심-기능-)
  - [주요 내용](#주요-내용)
    - [경기 매칭 시스템](#경기-매칭-시스템)
    - [경기 실시간 방송(WebRTC)](#경기-실시간-방송webrtc)
    - [1:1 채팅(WebSocket)](#11-채팅websocket)
    - [탁구 정보 커뮤니케이션(게시글 \& 댓글)](#탁구-정보-커뮤니케이션게시글--댓글)
    - [랭킹](#랭킹)
    - [전적](#전적)
  - [주요 기능 영상](#주요-기능-영상)


<br><br>

# 🚀 프로젝트 소개 🚀  

## 프로젝트 개요 및 배경
탁구 동아리에서 탁구를 자주 치며 자연스럽게 불편함이 생겼었습니다.<br>
누가 어떤 시간에 경기를 원하는지 알기 어렵고, 동아리 채팅방에서는 경기 모집 메시지가 금방 묻혀버렸습니다.<br>
경기 상황을 공유하거나 근처에 있지 않으면 볼 수 없고, 자신의 실력 수준을 확인하기도 쉽지 않았습니다.<br>

- 경기 상대를 편하게 구하고 싶다.
- 내 실력이 어느 정도인지 알고 싶다.
- 경기 장면을 다른 사람들과 공유하고 싶다.
- 탁구에 관한 정보를 쉽게 보고 싶다.

탁구 경기 매칭부터 실시간 방송, 채팅, 탁구 관련 정보 커뮤니티까지,<br>
탁구를 즐기는 사용자들이 더 편하게 소통하고 경기할 수 있는 기능을 구현하는 것을 목표로 했습니다.
<br>

## 프로젝트 목표
- 탁구 경기 등록 및 참가
  - 누구나 원하는 시간/장소/방식을 선택해 경기 등록
  - 다른 사용자는 실시간으로 모집 현황을 확인하고 참가 신청 가능
- 실시간 탁구 경기 방송(WebRTC)
  - 다른 모든 사용자들이 실시간으로 관전 가능
- 채팅(WebSocket + STOMP)
  - 1:1 채팅을 통한 소통
- 탁구 정보 커뮤니티(게시글 & 댓글)
  - 자유와 기술, 장비 카테고리로 탁구 관련 정보 공유
- 랭킹 시스템
  - 경기 결과 기반
  - 사용자 실력 비교 및 동기 부여

<br><br>

# 🛠 프로젝트 설계 🏗
## 기술 스택

### ✔ Frond-End
<div>
 <img src="https://img.shields.io/badge/Flutter-%2302569B.svg?style=for-the-badge&logo=Flutter&logoColor=white"/>
</div>

### ✔ Back-End
<div>
 <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/>
</div>

### ✔ Database
<div>
 <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
 <img src="https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white"/>
 <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
</div>

### ✔ Cloud
<div>
 <img src="https://img.shields.io/badge/AWS(EC2, RDS, SES)-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white"/>
 <img src="https://img.shields.io/badge/MongoDB Atlas-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white"/>
</div>

### ✔ DevOps
<div>
 <img src="https://img.shields.io/badge/GitHub Actions-2088FF.svg?style=for-the-badge&logo=githubactions&logoColor=white">
 <img src="https://img.shields.io/badge/Docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
 <img src="https://img.shields.io/badge/Docker Compose-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
</div>

### ✔ Dev tools
<div>
 <img src="https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white">
 <img src="https://img.shields.io/badge/IntelliJ-000000.svg?style=for-the-badge&logo=intellijidea&logoColor=white">
 <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>
 <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/>
</div>

<br>

## 🏗 시스템 아키텍처 🏛  
<div align="center">
 <img width="969" height="651" alt="kott_architecture drawio" src="https://github.com/user-attachments/assets/19c46c85-9472-4eff-8da4-ab8018a11cc0" />
</div>

<br>

## Spring Boot 요청 처리 흐름 (with JWT Authentication & Exception Handling)
<div align="center">
 <img width="868" height="601" alt="request-response flow drawio" src="https://github.com/user-attachments/assets/34dfd7b5-5255-419a-bda1-0326cd3b9c84" />
</div>

위의 다이어그램은 클라이언트가 API 요청을 보낸 후,
JWT 인증 필터 -> DispatcherServlet -> Controller -> JSON 직렬화 -> 응답 반환으로 이어지는 전체 요청 처리 과정을 나타냅니다.

### 주요 흐름
1. 클라이언트가 HTTP 요청을 전송하면 Servlet Filter Chain이 먼저 실행됩니다.
2. JWTFilter는 Authorization 헤더를 검사합니다.
    - 토근 X -> 익명 요청으로 통과
    - 형식 오류 및 만료 -> 필터 단계에서 바로 ErrorResponse 반환
    - 정상 토큰 -> 인증 정보(SecurityContext) 저장 후 Request 진행
3. 필터 체인을 통과한 요청이 DispatcherServlet으로 전달됩니다.
4. DispatcherServlet은 다음 단계로 요청을 위임합니다.
    - HandlerMapping -> 어떤 Controller 메서드가 처리할지 조회
    - HandlerAdapter -> 실행 가능한 형태로 매핑
    - Controller 실행
5. Controller가 반환한 DTO 또는 ResponseEntity가 HttpMessageConverter를 통해 JSON으로 직렬화됩니다.
6. 만들어진 HTTP 응답이 클라이언트에게 반환됩니다.
7. Controller 내부에서 예외 발생 시 GlobalExceptionHandler(@RestControllerAdvice)가 처리하여 일관된 ErrorResponse를 반환합니다.

### 특징
- 인증 실패는 필터 단계에서 DispatcherServlet을 거치지 않고 즉시 응답
- 비즈니스 예외는 Controller -> GlobalExceptionHandler 흐름에서 처리
- 정상 요청만 DispatcherServlet 이후의 MVC 흐름 진행
<br>

## 🛠 ERD 🗂  
<table>
  <!-- 1st Row -->
  <tr>
    <td align="center"><b>👤 User Domain ERD</b></td>
    <td align="center"><b>🏓 Game Domain ERD</b></td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/70481f2a-3034-4ba7-b49f-a2e640556b8a" width="420">
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/89fa5a18-6a51-4ef6-8a5f-99921fb92e69" width="420">
    </td>
  </tr>

  <!-- 2nd Row -->
  <tr>
    <td align="center"><b>💬 Post & Chat Domain ERD</b></td>
    <td align="center"><b></b></td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/1f950e9b-dcde-497b-9779-d9e81d899da7" width="420">
    </td>
    <td align="center">
      <!-- 비워둘 수도 있고, 설명 넣을 수도 있음 -->
    </td>
  </tr>
</table>

<br><br>

# 📈 서버 안정성 개선
Spring Boot 프로젝트 배포 후 EC2 t3.micro(1GB RAM) 환경에서 다음과 같은 메모리 부족 문제가 반복적으로 발생했습니다.
- Spring Boot 애플리케이션이 OOM(Out Of Memory)으로 종료
- docker pull 과정에서 시스템 메모리 부족
- SSH 응답 지연

이를 해결하기 위해 EC2 인스턴스에 Swap Memory(2GB)를 추가로 구성했습니다.
```bash
sudo dd if=/dev/zero of=/swapfile bs=128M count=16
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
```

Swap Memory 활성화 후 다음과 같은 개선 효과를 확인했습니다.
- Spring Boot 애플리케이션의 메모리 안정성 향상
- SSH 응답 지연 감소 및 전반적인 서버 안정성 향상

<table>
  <tr>
    <td align="center"><b>기존</b></td>
    <td align="center">
      <img width="1021" height="77" alt="before" src="https://github.com/user-attachments/assets/d54b3ec3-d8d4-41c2-9cc9-6f6edf0e8aa0" />
    </td>
  </tr>
  <tr>
    <td align="center"><b>Swap Memory 후</b></td>
    <td align="center">
      <img width="1007" height="72" alt="after" src="https://github.com/user-attachments/assets/4a061ec8-212f-470a-a7e7-16b1903a6010" />
    </td>
  </tr>
</table>

<br><br>

# 🛠 핵심 기능 🏗  
## 주요 내용  

### 경기 매칭 시스템
탁구 경기를 원하는 사용자들이 장소·시간·세트 수·상대 선택 방식 등을 선택하여 경기를 생성하고,
다른 사용자가 매칭 요청을 보낼 수 있는 시스템입니다.
- 주요 기능
  - 상태 기반 매칭(모집중 -> 경기 대기 중 -> 경기중 -> 경기 종료/만료)
  - 선착순과 선택을 통해 상대방 모집
  - 경기 종료 시 자동 기록 저장 및 사용자 랭킹 반영
  - 경기 종료 후 상대방과 경기 내용에 대한 리뷰 작성 가능
 
### 경기 실시간 방송(WebRTC)
경기 중인 사용자가 자신의 카메라 화면을 WebRTC 기반으로 실시간 송출하고,
다른 사요자는 라이브로 해당 경기를 시청할 수 있습니다.
- 주요 기능
  - 방송 방 생성, 입장, 종료
  - 카메라 전환(전면 <-> 후면) 및 음소거
  - 경기 진행 중 실시간 점수, 자리 업데이트

### 1:1 채팅(WebSocket)
친구와 실시간 텍스트 채팅 기능을 제공합니다.
- 주요 기능
  - STOMP WebSocket을 이용한 실시간 채팅
  - Flutter에서 실시간 메시지 수진 및 읽음 UI 반영
  - 서버에서는 메시지 저장 후 해당 방에 실시간으로 전송

### 탁구 정보 커뮤니케이션(게시글 & 댓글)
사용자들이 탁구 관련 정보를 공유하고 소통할 수 있는 커뮤니티 기능입니다.
자유, 기술, 용품 카테고리를 주제로 자유롭게 게시물을 작성할 수 있습니다.
- 주요 기능
  - 게시글 작성/수정/삭제/조회 가능
  - 제목과 내용 기반 검색 가능
  - 댓글을 통한 사용자 간 소통
  - 최신순, 오래된순 정렬 기능
 
### 랭킹
승리·패배 기록 및 승률을 기반으로 사용자 랭킹을 계산합니다.
- 주요 기능
  - 승률순과 승리수순 랭킹을 따로 계산 보여줌

### 전적
사용자의 경기 결과와 전체 및 최근 경기에 대한 승률을 확인할 수 있습니다.
- 주요 기능
  - 경기별 상세 정보 제공(세트 점수, 상대방 정보, 날짜, 장소)
  - 전체 및 최근 10경기 통계
  - 검색을 통한 다른 사용자 전적 확인

## 주요 기능 영상
| **회원가입 & 로그인 & ID/PW 찾기** | **경기 등록/참여 & 방송** | **게시물** |
| :---: | :---: | :---: |
| ![phone_register_login](https://github.com/user-attachments/assets/6a73c909-a40b-4758-875e-3c174bdd0811) | ![game_register_broadcast](https://github.com/user-attachments/assets/6b97a524-4b1b-404e-82a6-805e969f5148) | ![post](https://github.com/user-attachments/assets/f16dbdfa-82b5-4fbd-adf0-56f3118ef23b) |
| **관련 영상** | **랭킹** | **전적** |
| ![video](https://github.com/user-attachments/assets/24eaa133-8cf1-4544-bf3a-8e63c4a149dc) | ![ranking](https://github.com/user-attachments/assets/0606d35c-7960-49d5-8502-3f168cd4dd4f) | ![game_history](https://github.com/user-attachments/assets/1cc1272c-3d29-45e3-ae89-cc4d134be193) |
| **친구 & 채팅** | **마이페이지** | |
| ![chatting](https://github.com/user-attachments/assets/3d168809-5e0d-440c-816f-49a45cce011a) | ![mypage](https://github.com/user-attachments/assets/8ca1de91-c5fe-4c79-84da-b8b1192efd80) |  |
