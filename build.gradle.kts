import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.0-M2" apply false
	id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
	id("net.saliman.cobertura") version "4.0.0" apply false
	id("com.github.kt3k.coveralls") version "2.10.2" apply false
	id ("org.jetbrains.kotlin.plugin.jpa") version "1.4.30-RC" apply false

	kotlin("jvm") version "1.4.31"
	kotlin("plugin.spring") version "1.4.30" apply false

	java
	idea
}

allprojects {

	group = "com.jbequinn.library"
	version = "1.0-SNAPSHOT"

	apply(plugin = "java")
	java.sourceCompatibility = JavaVersion.VERSION_15

	repositories {
		mavenCentral()
		maven { url = uri("https://repo.spring.io/milestone") }
	}

	subprojects {
		tasks.withType<KotlinCompile> {
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict")
				jvmTarget = "15"
			}
		}

		apply(plugin = "io.spring.dependency-management")
		configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
			imports {
				mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
				mavenBom("org.testcontainers:testcontainers-bom:1.15.2")
			}
		}

		tasks.withType<Test> {
			useJUnitPlatform()
			// show standard out and standard error of the test JVM(s) on the console
			testLogging.showStandardStreams = true
		}
	}
}
