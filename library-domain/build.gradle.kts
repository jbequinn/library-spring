plugins {
	id("java-library")
}

dependencies {
	compileOnly("org.projectlombok:lombok:1.18.12")
	annotationProcessor("org.projectlombok:lombok:1.18.12")
	implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")

	compileOnly("jakarta.transaction:jakarta.transaction-api:1.3.3")
	compileOnly("jakarta.validation:jakarta.validation-api:2.0.2")

	testCompileOnly("org.projectlombok:lombok:1.18.12")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.12")

	// Use JUnit Jupiter API for testing.
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")

	testImplementation("org.mockito:mockito-core:3.5.10")
	testImplementation("org.assertj:assertj-core:3.17.2")
	testImplementation("org.mockito:mockito-junit-jupiter:3.5.10")

	// Use JUnit Jupiter Engine for testing.
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}
