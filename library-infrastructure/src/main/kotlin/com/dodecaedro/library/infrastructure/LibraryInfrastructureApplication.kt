package com.dodecaedro.library.infrastructure

import com.dodecaedro.library.domain.service.BorrowService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

@SpringBootApplication
open class LibraryInfrastructureApplication {

//	val myBeans = beans {
//		bean<BorrowService>()
//	}
//
//	val context = GenericApplicationContext().apply {
//		myBeans.initialize(this)
//		refresh()
//	}

	fun main(args: Array<String>) {
		runApplication<LibraryInfrastructureApplication>(*args)
	}
}
