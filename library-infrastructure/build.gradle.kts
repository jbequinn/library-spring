plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
	id("net.saliman.cobertura")
	id("com.github.kt3k.coveralls")
	id("io.freefair.lombok")
}

configurations.implementation {
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
}

dependencies {
	implementation(project(":library-model"))
	implementation(project(":library-domain"))

	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jetty")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.flywaydb:flyway-core")

	runtimeOnly("org.postgresql:postgresql")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:elasticsearch")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("com.github.database-rider:rider-spring:1.44.0")
	testImplementation("org.assertj:assertj-core")
	testImplementation("io.rest-assured:rest-assured")
}
