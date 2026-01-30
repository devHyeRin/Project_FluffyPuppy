# 🐶 Project_FluffyPuppy

반려동물 쇼핑몰 웹 애플리케이션  
Spring Boot 기반 개인 프로젝트

---

## 💡 프로젝트 개요

**Project_FluffyPuppy**는  
반려동물 용품 구매 과정을 **회원 · 상품 · 장바구니 · 주문** 흐름으로 구현한  
**도메인 중심 쇼핑몰 웹 서비스**입니다.

단순 CRUD 구현에 그치지 않고,  
실제 쇼핑몰에서 발생하는 핵심 비즈니스 흐름을 이해하며  
**유지보수와 확장을 고려한 구조 설계**를 목표로 개발했습니다.

> “기능이 동작하는 코드”가 아니라  
> **“왜 이렇게 설계했는지 설명할 수 있는 코드”**를 만드는 것을 목표로 진행했습니다.

---

## 🎯 프로젝트 목표

### 1️⃣ 쇼핑몰 핵심 도메인 이해
- 회원, 상품, 장바구니, 주문이라는  
  쇼핑몰의 핵심 도메인을 중심으로 비즈니스 로직 설계 및 구현

### 2️⃣ 유지보수 가능한 구조 설계
- Controller / Service / Repository 역할 분리
- 기능 추가 및 수정 시 영향 범위를 최소화하는 구조 설계

### 3️⃣ 개인 프로젝트 기반 포트폴리오 구축
- 면접 시 **설계 의도와 구현 방식을 설명할 수 있는 프로젝트**를 목표로  
  코드 구조 및 문서 정리에 집중

---

## ⏱️ 프로젝트 정보

- 프로젝트 유형 : 개인 프로젝트
- 개발 환경 : Spring Boot 기반 웹 애플리케이션
- 프로젝트 상태 : 기능 구현 완료 후 리팩토링 및 구조 정리 진행 중

---

## 🚀 프로젝트 주요 기능

### 👤 회원 기능
- 회원 가입 / 로그인
- Spring Security 기반 인증 처리

### 🛍️ 상품 기능
- 상품 목록 조회
- 상품 상세 조회

### 🛒 장바구니
- 상품 담기
- 수량 변경
- 장바구니 목록 조회

### 📦 주문
- 주문 생성
- 주문 내역 조회
- 주문–상품 관계를 고려한 데이터 설계

---

## 🧱 기술 스택 및 개발 환경

### Backend
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- MyBatis
- Spring Security
- REST API

### Database
- MySQL

### Build & Tools
- Maven
- Git / GitHub
- IntelliJ IDEA

### Environment
- Windows

---

## 📂 프로젝트 구조
*(리팩토링 진행 중)*

```bash
src
 ├─ main
 │   ├─ java
 │   │   └─ com.fluffypuppy
 │   │       ├─ controller
 │   │       ├─ service
 │   │       ├─ repository
 │   │       └─ domain
 │   └─ resources
 │       ├─ templates
 │       └─ static
 └─ test
```

## 🗄️ DB 설계

- 회원, 상품, 장바구니, 주문 중심 테이블 설계

- 정규화를 고려한 관계 설정

- 주문–상품 간 다대다 관계를 고려한 구조 설계

> ERD 및 상세 테이블 구조는 정리 후 README에 추가 예정

## 🔍 개발 중 고민한 점

- 비즈니스 로직을 어디까지 Service 계층에서 책임질 것인가

- 도메인 중심 설계를 위해 Entity 역할을 어디까지 둘 것인가

- 추후 관리자 기능 및 결제 기능 확장을 고려한 구조 설계

- 개인 프로젝트이지만 협업을 가정한 코드 구조 유지

## 🚀 실행 방법
```bash
# 프로젝트 클론
git clone https://github.com/devHyeRin/Project_FluffyPuppy.git

# 실행
./mvnw spring-boot:run
```


## 👩‍💻 개발자

- 이혜린

- GitHub : https://github.com/devHyeRin

---