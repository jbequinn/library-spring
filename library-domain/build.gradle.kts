plugins {
	kotlin("jvm")
	id("net.saliman.cobertura")
	id("com.github.kt3k.coveralls")
	id("io.freefair.lombok")
}

dependencies {
	implementation(project(":library-model"))

	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("jakarta.persistence:jakarta.persistence-api")

	compileOnly("jakarta.transaction:jakarta.transaction-api")
	compileOnly("jakarta.validation:jakarta.validation-api")

	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.mockito:mockito-junit-jupiter")
	testImplementation("org.assertj:assertj-core")
}
