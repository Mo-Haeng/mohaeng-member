import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")

	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("plugin.jpa")
}

allprojects {
	group = "com.mohang"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	apply(plugin = "kotlin")
	apply(plugin = "kotlin-spring")

	val springCloudVersion: String by project
	val mockkVersion: String by project
	val striktVersion: String by project

	dependencies {

		// Kotlin
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

		// Jackson
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("com.fasterxml.jackson.module:jackson-module-afterburner")

		// Spring Boot
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-aop")

		// Test
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("io.mockk:mockk:$mockkVersion")
		testImplementation("io.strikt:strikt-core:$striktVersion")

		// Annotation Processing
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

		// Logging
		implementation("io.github.microutils:kotlin-logging:1.12.5")
	}


	dependencyManagement {
		imports {
			mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
		}
	}


	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}