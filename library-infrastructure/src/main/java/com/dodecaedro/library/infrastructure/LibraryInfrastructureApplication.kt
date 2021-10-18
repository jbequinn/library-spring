package com.dodecaedro.library.infrastructure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryInfrastructureApplication {
	fun main(args: Array<String>) {
		runApplication<LibraryInfrastructureApplication>(*args)
	}
}
