plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "co.quest.xms"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("org.springframework.security:spring-security-crypto:6.3.3")
	implementation("jakarta.validation:jakarta.validation-api:3.1.0")
	implementation("io.jsonwebtoken:jjwt:0.12.6")
	implementation("com.stripe:stripe-java:28.2.0")

	compileOnly("org.projectlombok:lombok:1.18.34")

	annotationProcessor("org.projectlombok:lombok:1.18.34")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.18.0")
	testImplementation("org.springframework.kafka:spring-kafka-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
