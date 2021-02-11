package com.dodecaedro.library.domain.exception

class ActiveFinesException(message: String) : Exception(message)
class BorrowMaximumLimitException(message: String) : Exception(message)
class ExpiredBorrowException(message: String) : Exception(message)
