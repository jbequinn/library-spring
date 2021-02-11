plugins {
	id("com.dodecaedro.library.java-library-conventions")
	id("org.jetbrains.kotlin.jvm") version "1.4.20-RC"
	id ("org.jetbrains.kotlin.plugin.jpa") version "1.4.20-RC"
}

dependencies {
	implementation(project(":library-model"))

	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

	compileOnly("org.projectlombok:lombok:1.18.18")
	annotationProcessor("org.projectlombok:lombok:1.18.18")
	implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")

	compileOnly("jakarta.transaction:jakarta.transaction-api:1.3.3")
	compileOnly("jakarta.validation:jakarta.validation-api:2.0.2")

	testCompileOnly("org.projectlombok:lombok:1.18.12")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.12")
}
