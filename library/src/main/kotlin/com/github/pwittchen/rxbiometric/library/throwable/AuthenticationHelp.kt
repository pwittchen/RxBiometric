package com.github.pwittchen.rxbiometric.library.throwable

data class AuthenticationHelp(
  val helpCode: Int,
  val helpMessage: CharSequence?
) : Throwable()