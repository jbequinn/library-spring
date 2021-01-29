plugins {
	id("com.dodecaedro.library.java-library-conventions")
}

dependencies {
	compileOnly("org.projectlombok:lombok:1.18.18")
	annotationProcessor("org.projectlombok:lombok:1.18.18")
	implementation("jakarta.persistence:jakarta.persistence-api:3.0.0")

	compileOnly("jakarta.transaction:jakarta.transaction-api:2.0.0")
	compileOnly("jakarta.validation:jakarta.validation-api:2.0.2")

	testCompileOnly("org.projectlombok:lombok:1.18.12")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.12")
}
