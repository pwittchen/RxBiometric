package com.github.pwittchen.rxbiometric.library

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
import android.hardware.biometrics.BiometricPrompt.CryptoObject
import android.os.Build
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import java.util.concurrent.Executor

class RxBiometric {
  companion object {
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var negativeButtonText: String
    private lateinit var negativeButtonListener: DialogInterface.OnClickListener
    private lateinit var cancellationSignal: CancellationSignal
    private lateinit var executor: Executor

    @JvmStatic
    fun create(
      builder: RxBiometricBuilder
    ): Companion {
      this.title = builder.title
      this.description = builder.description
      this.negativeButtonText = builder.negativeButtonText
      this.negativeButtonListener = builder.negativeButtonListener
      this.cancellationSignal = builder.cancellationSignal
      this.executor = builder.executor
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

    @JvmStatic fun cancellationSignal(cancellationSignal: CancellationSignal): RxBiometricBuilder {
      return builder().cancellationSignal(cancellationSignal)
    }

    @JvmStatic fun executor(executor: Executor): RxBiometricBuilder {
      return builder().executor(executor)
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticate(context: Context): Completable {
      return Completable.create {
        createPrompt(context).authenticate(
          cancellationSignal,
          executor,
          createAuthenticationCallback(it)
        )
      }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticate(
      context: Context,
      cryptoObject: CryptoObject
    ): Completable {
      return Completable.create {
        createPrompt(context).authenticate(
          cryptoObject,
          cancellationSignal,
          executor,
          createAuthenticationCallback(it)
        )
      }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun createPrompt(context: Context): BiometricPrompt {
      return BiometricPrompt
        .Builder(context)
        .setTitle(title)
        .setDescription(description)
        .setNegativeButton(
          negativeButtonText,
          executor,
          negativeButtonListener
        )
        .build()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun createAuthenticationCallback(emitter: CompletableEmitter): AuthenticationCallback {
      return Authentication().createAuthenticationCallback(emitter)
    }
  }
}