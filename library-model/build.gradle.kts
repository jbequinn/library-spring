plugins {
	kotlin("jvm")
	id ("org.jetbrains.kotlin.plugin.jpa")
}

dependencies {
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	compileOnly("org.projectlombok:lombok:1.18.20")
	annotationProcessor("org.projectlombok:lombok:1.18.20")
	implementation("jakarta.persistence:jakarta.persistence-api")

	testCompileOnly("org.projectlombok:lombok:edge-SNAPSHOT")
	testAnnotationProcessor("org.projectlombok:lombok:edge-SNAPSHOT")
}
