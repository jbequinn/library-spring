plugins {
	id("com.dodecaedro.library.java-application-conventions")
	id("net.saliman.cobertura") version "4.0.0"
	id("com.github.kt3k.coveralls") version "2.10.2"
	id("org.springframework.boot") version "2.4.0-RC1"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
}

dependencies {
	implementation(project(":library-domain"))

	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.flywaydb:flyway-core")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("org.postgresql:postgresql")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:elasticsearch")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("com.github.database-rider:rider-spring:1.16.1")
	testImplementation("org.assertj:assertj-core")
	testImplementation("io.rest-assured:rest-assured")
}

extra["testcontainersVersion"] = "1.15.1"

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
	}
}
