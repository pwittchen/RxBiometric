package com.github.pwittchen.rxbiometric.library

import android.content.DialogInterface
import android.os.CancellationSignal
import com.github.pwittchen.rxbiometric.library.RxBiometric.Companion
import java.util.concurrent.Executor

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
    return RxBiometric.create(this)
  }
}