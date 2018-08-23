package com.github.pwittchen.rxbiometric.library

import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
import android.hardware.biometrics.BiometricPrompt.AuthenticationResult
import android.os.Build
import android.support.annotation.RequiresApi
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationHelp
import io.reactivex.CompletableEmitter

open class Authentication {
  @RequiresApi(Build.VERSION_CODES.P)
  fun createAuthenticationCallback(it: CompletableEmitter): AuthenticationCallback {
    return object : AuthenticationCallback() {
      override fun onAuthenticationSucceeded(result: AuthenticationResult?) {
        it.onComplete()
      }

      override fun onAuthenticationFailed() {
        it.tryOnError(AuthenticationFail())
      }

      override fun onAuthenticationError(
        errorCode: Int,
        errorMessage: CharSequence?
      ) {
        it.tryOnError(AuthenticationError(errorCode, errorMessage))
      }

      override fun onAuthenticationHelp(
        helpCode: Int,
        helpMessage: CharSequence?
      ) {
        it.tryOnError(AuthenticationHelp(helpCode, helpMessage))
      }
    }
  }
}