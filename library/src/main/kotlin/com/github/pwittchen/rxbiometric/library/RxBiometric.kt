package com.github.pwittchen.rxbiometric.library

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.hardware.biometrics.BiometricPrompt.AuthenticationResult
import android.os.Build
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationHelp
import io.reactivex.Completable
import java.util.concurrent.Executor

//TODO: add unit tests
class RxBiometric {

  companion object {

    private lateinit var title: String
    private lateinit var description: String
    private lateinit var negativeButtonText: String
    private lateinit var negativeButtonListener: DialogInterface.OnClickListener
    private lateinit var cancellationSignal: CancellationSignal
    private lateinit var executor: Executor

    @JvmStatic fun create(
      builder: Builder
    ): Companion {
      this.title = builder.title
      this.description = builder.description
      this.negativeButtonText = builder.negativeButtonText
      this.negativeButtonListener = builder.negativeButtonListener
      this.cancellationSignal = builder.cancellationSignal
      this.executor = builder.executor
      return this
    }

    @JvmStatic fun builder(): Builder {
      return Builder()
    }

    @JvmStatic fun title(title: String): Builder {
      return builder().title(title)
    }

    @JvmStatic fun description(description: String): Builder {
      return builder().description(description)
    }

    @JvmStatic fun negativeButtonText(text: String): Builder {
      return builder().negativeButtonText(text)
    }

    @JvmStatic fun negativeButtonListener(listener: DialogInterface.OnClickListener): Builder {
      return builder().negativeButtonListener(listener)
    }

    @JvmStatic fun cancellationSignal(cancellationSignal: CancellationSignal): Builder {
      return builder().cancellationSignal(cancellationSignal)
    }

    @JvmStatic fun executor(executor: Executor): Builder {
      return builder().executor(executor)
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

    @RequiresApi(Build.VERSION_CODES.P) @JvmStatic fun authenticate(context: Context): Completable {
      return Completable.create {
        createPrompt(context).authenticate(
          cancellationSignal,
          executor,
          object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: AuthenticationResult?) {
              super.onAuthenticationSucceeded(result)
              it.onComplete()
            }

            override fun onAuthenticationFailed() {
              super.onAuthenticationFailed()
              it.onError(AuthenticationFail())
            }

            override fun onAuthenticationError(
              errorCode: Int,
              errorMessage: CharSequence?
            ) {
              super.onAuthenticationError(errorCode, errorMessage)
              it.onError(AuthenticationError(errorCode, errorMessage))
            }

            override fun onAuthenticationHelp(
              helpCode: Int,
              helpMessage: CharSequence?
            ) {
              super.onAuthenticationHelp(helpCode, helpMessage)
              it.onError(AuthenticationHelp(helpCode, helpMessage))
            }
          })
      }
    }

    class Builder {
      internal lateinit var title: String
      internal lateinit var description: String
      internal lateinit var negativeButtonText: String
      internal lateinit var negativeButtonListener: DialogInterface.OnClickListener
      internal lateinit var cancellationSignal: CancellationSignal
      internal lateinit var executor: Executor

      fun title(title: String): Builder {
        this.title = title
        return this
      }

      fun description(description: String): Builder {
        this.description = description
        return this
      }

      fun negativeButtonText(negativeButtonText: String): Builder {
        this.negativeButtonText = negativeButtonText
        return this
      }

      fun negativeButtonListener(negativeButtonListener: DialogInterface.OnClickListener): Builder {
        this.negativeButtonListener = negativeButtonListener
        return this
      }

      fun cancellationSignal(cancellationSignal: CancellationSignal): Builder {
        this.cancellationSignal = cancellationSignal
        return this
      }

      fun executor(executor: Executor): Builder {
        this.executor = executor
        return this
      }

      fun build(): Companion {
        return create(this)
      }
    }
  }
}