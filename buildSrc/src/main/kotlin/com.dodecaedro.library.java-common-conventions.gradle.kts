plugins {
	// Apply the java Plugin to add support for Java.
	java
}

java.sourceCompatibility = JavaVersion.VERSION_15

repositories {
	// Use JCenter for resolving dependencies.
	jcenter()

	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	// Use JUnit Jupiter API for testing.
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")

	testImplementation("org.mockito:mockito-core:3.5.10")
	testImplementation("org.assertj:assertj-core:3.17.2")
	testImplementation("org.mockito:mockito-junit-jupiter:3.5.10")

	// Use JUnit Jupiter Engine for testing.
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks.test {
	// Use junit platform for unit tests.
	useJUnitPlatform()

	// show standard out and standard error of the test JVM(s) on the console
	testLogging.showStandardStreams = true
}
