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
import io.reactivex.CompletableEmitter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

class AuthenticationTest {
  private lateinit var emitter: CompletableEmitter
  private lateinit var authentication: Authentication
  private lateinit var callback: AuthenticationCallback

  @Before fun setUp() {
    emitter = mock(CompletableEmitter::class.java)
    authentication = spy(Authentication())
    callback = authentication.createAuthenticationCallback(emitter)
  }

  @Test fun shouldCompleteOnAuthenticationSucceeded() {
    // given
    val result: AuthenticationResult = mock(AuthenticationResult::class.java)

    // when
    callback.onAuthenticationSucceeded(result)

    // then
    verify(emitter).onComplete()
  }

  @Test fun shouldTryOnErrorOnAuthenticationFailed() {
    // when
    callback.onAuthenticationFailed()

    // then
    verify(emitter).tryOnError(any(AuthenticationFail::class.java))
  }

  @Test fun shouldTryOnErrorOnAuthenticationError() {
    // when
    callback.onAuthenticationError(1, "error occurred")

    // then
    verify(emitter).tryOnError(any(AuthenticationError::class.java))
  }

  @Test fun shouldTryOnErrorOnAuthenticationHelp() {
    // when
    callback.onAuthenticationHelp(2, "help needed")

    // then
    verify(emitter).tryOnError(any(AuthenticationHelp::class.java))
  }
}