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

import android.content.DialogInterface
import android.os.Build
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.fragment.app.FragmentActivity
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import java.util.concurrent.Executor

class RxBiometric {
  companion object {
    private lateinit var title: String
    private lateinit var description: String
    private var negativeButtonText: String? = null
    private var deviceCredentialAllowed: Boolean? = null
    private var negativeButtonListener: DialogInterface.OnClickListener? = null
    private var confirmationRequired: Boolean = false
    private var allowedAuthenticators: Int? = null
    private lateinit var executor: Executor
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @JvmStatic
    fun create(
      builder: RxBiometricBuilder
    ): Companion {
      this.title = builder.title
      this.description = builder.description
      this.negativeButtonText = builder.negativeButtonText
      this.negativeButtonListener = builder.negativeButtonListener
      this.deviceCredentialAllowed = builder.deviceCredentialAllowed
      this.allowedAuthenticators = builder.allowedAuthenticators
      this.confirmationRequired = builder.confirmationRequired
      this.executor = builder.executor
      val promptBuilder = BiometricPrompt.PromptInfo.Builder()
        .setConfirmationRequired(confirmationRequired)
        .setTitle(title)
        .setDescription(description)

      deviceCredentialAllowed?.let {
        promptBuilder.setDeviceCredentialAllowed(it)
      }

      allowedAuthenticators?.let {
        promptBuilder.setAllowedAuthenticators(it)
      }

      negativeButtonText?.let {
        promptBuilder.setNegativeButtonText(it)
      }
      this.promptInfo = promptBuilder.build()
      return this
    }

    @JvmStatic
    fun builder(): RxBiometricBuilder {
      return RxBiometricBuilder()
    }

    @JvmStatic
    fun title(title: String): RxBiometricBuilder {
      return builder().title(title)
    }

    @JvmStatic
    fun deviceCredentialAllowed(enable: Boolean): RxBiometricBuilder {
      if (Build.VERSION.SDK_INT < 30) return builder().deviceCredentialAllowed(enable)
      return builder()
    }

    @JvmStatic
    fun confirmationRequired(enable: Boolean): RxBiometricBuilder {
      return builder().confirmationRequired(enable)
    }

    @JvmStatic
    fun allowedAuthenticators(value: Int): RxBiometricBuilder {
      if (Build.VERSION.SDK_INT >= 29) return builder().allowedAuthenticators(value)
      return builder()
    }

    @JvmStatic
    fun description(description: String): RxBiometricBuilder {
      return builder().description(description)
    }

    @JvmStatic
    fun negativeButtonText(text: String): RxBiometricBuilder {
      return builder().negativeButtonText(text)
    }

    @JvmStatic
    fun negativeButtonListener(listener: DialogInterface.OnClickListener): RxBiometricBuilder {
      return builder().negativeButtonListener(listener)
    }

    @JvmStatic
    fun executor(executor: Executor): RxBiometricBuilder {
      return builder().executor(executor)
    }

    @JvmStatic
    fun authenticate(activity: FragmentActivity): Completable {
      return Completable.create { emitter ->
        createPrompt(activity, emitter).authenticate(promptInfo)
      }
    }

    @JvmStatic
    fun authenticate(
      activity: FragmentActivity,
      cryptoObject: CryptoObject
    ): Completable {
      return Completable.create { emitter ->
        createPrompt(activity, emitter).authenticate(
          promptInfo,
          cryptoObject
        )
      }
    }

    fun createPrompt(activity: FragmentActivity, emitter: CompletableEmitter): BiometricPrompt {
      return BiometricPrompt(activity, executor, createAuthenticationCallback(emitter))
    }

    fun createAuthenticationCallback(emitter: CompletableEmitter): AuthenticationCallback {
      return Authentication().createAuthenticationCallback(emitter)
    }
  }
}