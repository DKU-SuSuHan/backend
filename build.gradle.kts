import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
}

group = "com.susuhan"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring MVC
	implementation("org.springframework.boot:spring-boot-starter-web")

	// JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// DB - MySQL
	runtimeOnly("com.mysql:mysql-connector-j")

	// Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")

	// Validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Reflect
	implementation("org.jetbrains.kotlin:kotlin-reflect")
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
