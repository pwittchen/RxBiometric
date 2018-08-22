package com.github.pwittchen.rxbiometric.library.throwable

data class AuthenticationError(
  val errorCode: Int,
  val errorMessage: CharSequence?
) : Throwable()