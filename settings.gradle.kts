pluginManagement {
	repositories {
		maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "org.springframework.boot") {
				useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
			}
		}
	}
}
rootProject.name = "library-spring"

include("library-domain", "library-infrastructure")
