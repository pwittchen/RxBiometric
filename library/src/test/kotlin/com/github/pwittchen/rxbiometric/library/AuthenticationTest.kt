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

import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
import android.hardware.biometrics.BiometricPrompt.AuthenticationResult
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationHelp
import io.kotlintest.specs.StringSpec
import io.reactivex.CompletableEmitter
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

class AuthenticationTest : StringSpec({
  val emitter: CompletableEmitter = mock(CompletableEmitter::class.java)
  val authentication: Authentication = spy(Authentication())
  val callback: AuthenticationCallback = authentication.createAuthenticationCallback(emitter)

  "shouldCompleteOnAuthenticationSucceeded" {
    // given
    val result: AuthenticationResult = mock(AuthenticationResult::class.java)

    // when
    callback.onAuthenticationSucceeded(result)

    // then
    verify(emitter).onComplete()
  }

  "shouldTryOnErrorOnAuthenticationFailed" {
    // when
    callback.onAuthenticationFailed()

    // then
    verify(emitter).tryOnError(any(AuthenticationFail::class.java))
  }

  "shouldTryOnErrorOnAuthenticationError" {
    // when
    callback.onAuthenticationError(1, "error occurred")

    // then
    verify(emitter).tryOnError(any(AuthenticationError::class.java))
  }

  "shouldTryOnErrorOnAuthenticationHelp" {
    // when
    callback.onAuthenticationHelp(2, "help needed")

    // then
    verify(emitter).tryOnError(any(AuthenticationHelp::class.java))
  }
})