package com.github.pwittchen.rxbiometric.library

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt.CryptoObject
import android.os.CancellationSignal
import io.reactivex.Completable
import java.util.concurrent.Executor

//TODO #1: implement empty methods
//TODO #2: apply better builder pattern
//TODO #3: create RxJava flowable
//TODO #4: test everything in Java and Kotlin
//TODO #5: write unit tests

/*

API I want to achieve:

RxBiometric
  .title("title")
  .description("description")
  .negativeButton(...)
  .cancellationSignal(...)
  .executor(...)
  .cryptoObject(...) <- this is optional
  .authenticate()
  .subscribe { ... }

*/

class RxBiometric {

  companion object {

    private lateinit var title: String
    private lateinit var description: String
    private lateinit var negativeButtonText: String
    private lateinit var negativeButtonListener: DialogInterface.OnClickListener
    private lateinit var cancellationSignal: CancellationSignal
    private lateinit var executor: Executor
    private lateinit var cryptoObject: CryptoObject

    fun RxBiometric(
      title: String,
      description: String,
      negativeButtonText: String,
      negativeButtonListener: DialogInterface.OnClickListener,
      cancellationSignal: CancellationSignal,
      executor: Executor,
      cryptoObject: CryptoObject
    ) {
      this.title = title
      this.description = description
      this.negativeButtonText = negativeButtonText
      this.negativeButtonListener = negativeButtonListener
      this.cancellationSignal = cancellationSignal
      this.executor = executor
      this.cryptoObject = cryptoObject
    }

    //FIXME: methods below are not ready yet - check valid better builder pattern
    @JvmStatic fun title(title: String): Companion {
      this.title = title
      return this
    }

    @JvmStatic fun description(description: String): Companion {
      this.description = description
      return this
    }

    @JvmStatic fun negativeButton(
      text: String,
      listener: DialogInterface.OnClickListener
    ): Companion {
      this.negativeButtonText = text
      this.negativeButtonListener = listener
      return this
    }

    @JvmStatic fun cancellationSignal(cancellationSignal: CancellationSignal): Companion {
      this.cancellationSignal = cancellationSignal
      return this
    }

    @JvmStatic fun executor(executor: Executor): Companion {
      this.executor = executor
      return this
    }

    @JvmStatic fun cryptoObject(cryptoObject: CryptoObject): Companion {
      this.cryptoObject = cryptoObject
      return this
    }

    @JvmStatic fun authenticate(): Completable {
      //TODO: implement and return Completable
      //TODO: implement onError situation
      return Completable.create { emitter -> emitter.onComplete() }
    }
  }
}