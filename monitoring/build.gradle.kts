plugins {
    id("java")  //JVM build 관련 Task 등록
    id("org.springframework.boot") version "3.2.1"  //java plugin 추가 설정: bootJar 등등...
    id("io.spring.dependency-management") version "1.1.4"   //spring dependency manager 사용: 자동 version 관리
}

group = "com.app"
version = "0.0.1-SNAPSHOT"  //{project name}-{version}.jar

java {  //source & target Compatibility 설정 및 JVM 17없으면 설치 후 실행 보장
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")  //spring dependency manager 사용 해서 version 명시 안해도 된다.
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {      //Type이 Test인 모든 Task 설정
    useJUnitPlatform()
}