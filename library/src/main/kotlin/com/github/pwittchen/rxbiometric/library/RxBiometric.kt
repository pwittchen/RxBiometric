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

    fun create(
      builder: Builder
    ): Companion {
      this.title = builder.title
      this.description = builder.description
      this.negativeButtonText = builder.negativeButtonText
      this.negativeButtonListener = builder.negativeButtonListener
      this.cancellationSignal = builder.cancellationSignal
      this.executor = builder.executor
      this.cryptoObject = builder.cryptoObject
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

    @JvmStatic fun cryptoObject(cryptoObject: CryptoObject): Builder {
      return builder().cryptoObject(cryptoObject)
    }

    @JvmStatic fun authenticate(): Completable {
      //TODO: implement and return Completable
      //TODO: implement onError situation
      return Completable.create { emitter -> emitter.onComplete() }
    }

    class Builder {
      internal lateinit var title: String
      internal lateinit var description: String
      internal lateinit var negativeButtonText: String
      internal lateinit var negativeButtonListener: DialogInterface.OnClickListener
      internal lateinit var cancellationSignal: CancellationSignal
      internal lateinit var executor: Executor
      internal lateinit var cryptoObject: CryptoObject

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

      fun cryptoObject(cryptoObject: CryptoObject): Builder {
        this.cryptoObject = cryptoObject
        return this
      }

      fun build(): Companion {
        return create(this)
      }
    }
  }
}