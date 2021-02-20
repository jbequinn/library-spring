rootProject.name = "library-spring"

include(
	"library-model", "library-domain", "library-infrastructure"
)

pluginManagement {
	repositories {
		maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
	}
}
