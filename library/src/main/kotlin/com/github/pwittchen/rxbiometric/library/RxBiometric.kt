package com.github.pwittchen.rxbiometric.library

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt.CryptoObject
import android.os.CancellationSignal
import java.util.concurrent.Executor

//TODO #1: implement empty methods
//TODO #2: apply better builder pattern
//TODO #3: create RxJava flowable
//TODO #4: test everything in Java and Kotlin

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

    @JvmStatic fun title(title: String): Companion {
      return this
    }

    @JvmStatic fun description(description: String): Companion {
      return this
    }

    @JvmStatic fun negativeButton(
      text: String,
      listener: DialogInterface.OnClickListener
    ): Companion {
      return this
    }

    @JvmStatic fun cancellationSignal(cancellationSignal: CancellationSignal): Companion {
      return this
    }

    @JvmStatic fun executor(executor: Executor): Companion {
      return this
    }

    @JvmStatic fun cryptoObject(cryptoObject: CryptoObject): Companion {
      return this
    }

    @JvmStatic fun authenticate() {
      //TODO: implement and return Flowable
    }
  }
}