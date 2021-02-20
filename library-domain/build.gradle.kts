plugins {
	kotlin("jvm")
	id("net.saliman.cobertura")
	id("com.github.kt3k.coveralls")
}

dependencies {
	implementation(project(":library-model"))

	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation("jakarta.persistence:jakarta.persistence-api")

	compileOnly("jakarta.transaction:jakarta.transaction-api")
	compileOnly("jakarta.validation:jakarta.validation-api")

	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.mockito:mockito-junit-jupiter")
	testImplementation("org.assertj:assertj-core")
}
