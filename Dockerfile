# 1단계: build (의존성 캐싱 적용)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 1. pom.xml만 먼저 복사해서 라이브러리(의존성)를 미리 다운로드
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. 소스 코드 복사 후 빌드 (이미 받은 라이브러리는 재사용됨)
COPY src ./src
RUN mvn clean package -DskipTests


# 2단계: run
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# pom.xml에서 설정한 .jar 파일 이름만 가져오기
COPY --from=build /app/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
