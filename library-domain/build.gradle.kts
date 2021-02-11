plugins {
	id("com.dodecaedro.library.java-library-conventions")
	kotlin("jvm") version "1.4.30"
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

	testCompileOnly("org.projectlombok:lombok:1.18.18")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.18")
}
