import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
	kotlin("plugin.allopen") version "1.9.23"
	kotlin("kapt") version "1.9.24"
}

group = "com.susuhan"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

dependencies {
	// Spring MVC
	implementation("org.springframework.boot:spring-boot-starter-web")

	// JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// DB - MySQL
	runtimeOnly("com.mysql:mysql-connector-j")

	// Querydsl
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("jakarta.annotation:jakarta.annotation-api")
	kapt("jakarta.persistence:jakarta.persistence-api")

	// Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")

	// Validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")

	// Reflect
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")

	// OpenFeign
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// Querydsl 설정
val querydslDirPath = "build/generated"

tasks.withType<JavaCompile> {
	options.generatedSourceOutputDirectory.set(file(querydslDirPath))
}

tasks.named("clean") {
	doLast {
		file(querydslDirPath).deleteRecursively()
	}
}