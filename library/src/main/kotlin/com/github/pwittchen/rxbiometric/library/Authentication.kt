/*
 * Copyright (C) 2018 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.rxbiometric.library

import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import io.reactivex.CompletableEmitter

open class Authentication {
  fun createAuthenticationCallback(it: CompletableEmitter): AuthenticationCallback {
    return object : AuthenticationCallback() {
      override fun onAuthenticationSucceeded(result: AuthenticationResult) {
        it.onComplete()
      }

      override fun onAuthenticationFailed() {
        it.tryOnError(AuthenticationFail())
      }

      override fun onAuthenticationError(
        errorCode: Int,
        errorMessage: CharSequence
      ) {
        it.tryOnError(AuthenticationError(errorCode, errorMessage))
      }

//      override fun onAuthenticationHelp(
//        helpCode: Int,
//        helpMessage: CharSequence
//      ) {
//        it.tryOnError(AuthenticationHelp(helpCode, helpMessage))
//      }
    }
  }
}