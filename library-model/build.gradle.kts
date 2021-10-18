plugins {
	kotlin("jvm")
	id ("org.jetbrains.kotlin.plugin.jpa")
}

dependencies {
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("jakarta.persistence:jakarta.persistence-api")
}
