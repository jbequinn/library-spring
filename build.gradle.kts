subprojects {
	group = "com.dodecaedro.library"
	version = "1.0"

	repositories {
		mavenCentral()
		maven ("https://repo.spring.io/milestone")
	}

	plugins.withType<JavaPlugin>().configureEach {
		configure<JavaPluginExtension> {
			modularity.inferModulePath.set(true)
		}
	}

	tasks.withType<Test>().configureEach {
		useJUnitPlatform()
	}
}