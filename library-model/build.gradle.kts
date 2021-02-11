plugins {
	id("com.dodecaedro.library.java-library-conventions")
	kotlin("jvm") version "1.4.30"
	id ("org.jetbrains.kotlin.plugin.jpa") version "1.4.30-RC"
}

dependencies {
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

	compileOnly("org.projectlombok:lombok:1.18.18")
	annotationProcessor("org.projectlombok:lombok:1.18.18")
	implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")

	testCompileOnly("org.projectlombok:lombok:1.18.18")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.18")
}
