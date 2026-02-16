# 🐶 Project_FluffyPuppy

Spring Boot 기반 반려동물 용품 쇼핑몰 웹 애플리케이션
기존 CRUD 중심 프로젝트를 **도메인 구조 재설계 및 인증 체계 개선을 목표로 전면 리팩토링한 프로젝트**입니다.

> “동작하는 코드”에서 끝나는 것이 아니라  
> **유지보수 가능한 구조와 설계 의도를 설명할 수 있는 코드**를 만드는 것을 목표로 개선했습니다.
> 
---

## 📌 프로젝트 개요

**Project_FluffyPuppy**는  
회원 · 상품 · 장바구니 · 주문 흐름을 기반으로 구현한 쇼핑몰 웹 서비스입니다.

배포까지 학습한 내용을 적용하여 **전체 구조를 재설계하고 리팩토링**했습니다.

---

## 🔥 리팩토링 진행한 이유?
기존 프로젝트의 문제점은 다음과 같았습니다.

### ❌ 1. 회원 체계 혼재
- 로컬 회원과 SNS 회원이 분리된 구조
- 인증 객체와 도메인 모델 간 의존성 증가
- 유지보수 어려움

### ❌ 2. 장바구니/주문이 사용자 기준으로 보장되지 않음
- 사용자별 Cart가 명확히 분리되지 않음
- 데이터 정합성 문제 발생 가능성

### ❌ 3. 프론트엔드 구조 문제
- HTML 내부에 CSS/JavaScript가 혼합
- fragment 기반 구조 미적용
- 공통 레이아웃 구조 부재

### ❌ 4. 패키지 구조 비체계적 구성
- 기능 단위 응집도 부족
- 유지보수 시 영향 범위 파악 어려움

### ❌ 5. 이메일 인증 서비스 구조 문제
- 책임 분리 미흡
- 예외 처리 부족

---

## 🚀 리팩토링 핵심 개선 사항

## 🔥 Refactoring Summary

### 1️⃣ 회원 체계 통합 및 인증 구조 재설계
- 로컬 회원 / SNS 회원 단일 테이블 통합
- Provider / Role 필드 추가
- Spring Security 기반 인증 구조 재설계
- CustomOAuth2UserService 적용
- 인증 객체와 도메인 의존성 정리
- 회원 통합에 따른 Cart / Order 로직 수정

👉 인증 흐름을 명확히 분리하고 **유지보수 가능**하도록 개선

---

### 2️⃣ 장바구니 도메인 개선

- 사용자 기준 Cart 1:1 보장
- 서비스 계층 책임 명확화
- DTO 구조 개선
- OutOfStockException 예외 처리 추가
- SNS 사용자 장바구니 로직 수정

👉 데이터 정합성과 트랜잭션 안정성 강화

---

### 3️⃣ 주문 도메인 개선

- 주문 엔티티 구조 개선
- 주문 취소 로직 재정비
- QueryDSL 기반 조회 쿼리 개선
- 트랜잭션 범위 명확화

👉 조회 성능 및 비즈니스 로직 명확화

---

### 4️⃣ 공지사항 도메인 개선

- Repository Custom 분리
- 검색 필터 로직 개선
- 서비스 / 컨트롤러 책임 정리

---

### 5️⃣ 프론트엔드 구조 개선

- HTML / CSS / JS 완전 분리
- fragment 기반 layout 구조 적용
- 공통 Header / Footer 구조화
- 반응형 레이아웃 적용 (rem 단위 통일)
- 헤더 장바구니 수량 실시간 연동

👉 UI 재사용성과 유지보수성 개선

---

## 🌿 Branch Strategy

도메인 단위 브랜치 전략을 적용하여  
**기능 단위 Pull Request 방식**으로 리팩토링을 진행했습니다.

```
refactor/cart-structure
refactor/member
refactor/orders
refactor/notice
refactor/header-ui
style/responsive-layout
update/item-status
```

👉 기능 단위 커밋 및 PR 관리로 변경 이력 추적 가능하도록 구성

---

## 🧱 기술 스택

### Backend
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- OAuth2
- QueryDSL
- REST API

### Frontend
- Thymeleaf
- HTML5
- CSS3 
- JavaScript

### Database
- MySQL

### DevOps (예정)
- AWS EC2
- Docker
- GitHub Actions (CI/CD)

---

## 📂 현재 프로젝트 구조 (리팩토링 후)

```bash
src
 ├─ main
 │   ├─ java
 │   │   └─ com.fluffypuppy.shop
 │   │       ├─ config
 │   │       ├─ controller
 │   │       ├─ service
 │   │       ├─ repository
 │   │       ├─ entity
 │   │       ├─ dto
 │   │       ├─ exception
 │   │       └─ constant
 │   └─ resources
 │       ├─ templates
 │       │   ├─ cart
 │       │   ├─ item
 │       │   ├─ member
 │       │   ├─ notice
 │       │   ├─ order
 │       │   ├─ layouts
 │       │   └─ fragments
 │       └─ static
 │           ├─ css
 │           ├─ js
 │           └─ img
```

## 🚀 배포 이후 적용 예정 구조 (도메인 중심 패키지 구조)
```bash
shop
 ├── global
 │    ├── config
 │    ├── exception
 │    └── security
 │
 ├── member
 │    ├── controller
 │    ├── service
 │    ├── repository
 │    ├── dto
 │    └── entity
 │
 ├── item
 ├── cart
 ├── order
 ├── notice
```

👉 도메인 단위 응집도를 높여 유지보수성과 확장성 개선 예정

---

## 🗄️ DB 설계
- 회원, 상품, 장바구니, 주문 중심 테이블 설계
- 정규화를 고려한 관계 설정
- 주문–상품 다대다 관계를 OrderItem 중간 테이블로 분리
- 회원 통합에 따른 Provider / Role 필드 추가

---

## 🖥️ 실행 화면

> 실행 화면 캡처 이미지를 여기에 추가 예정

- 메인 페이지
- 로그인 / SNS 로그인
- 장바구니
- 주문 내역
- 관리자 공지사항 관리

---

## 📈 프로젝트를 통해 성장한 점

- Spring Security 인증 구조를 직접 설계하며 내부 동작 이해도 향상
- 회원 통합 과정에서 도메인 의존성 정리 경험
- QueryDSL 기반 동적 조회 구현 경험
- 사용자 기준 데이터 정합성 보장 설계 경험
- 구조적 리팩토링 및 브랜치 전략 운영 경험
- 프론트엔드 레이아웃 구조화 경험

---

## 🚀 실행 방법
```bash
# 프로젝트 클론
git clone https://github.com/devHyeRin/Project_FluffyPuppy.git

# 실행
./mvnw spring-boot:run
```

---
## 📦 배포 계획

- AWS EC2 서버 배포
- Docker 기반 컨테이너 실행
- MySQL 컨테이너 구성
- GitHub Actions 기반 CI/CD 자동화 예정

---


## 👩‍💻 개발자

- 이혜린

- GitHub : https://github.com/devHyeRin

---