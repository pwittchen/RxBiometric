package com.github.pwittchen.rxbiometric.library.validation

import android.content.Context
import io.reactivex.Single

class RxPreconditions {
  companion object {

    @JvmStatic fun hasBiometricSupport(context: Context): Single<Boolean> {
      return Single.just(Preconditions.hasBiometricSupport(context))
    }

    @JvmStatic fun isAtLeastAndroidPie(): Single<Boolean> {
      return Single.just(Preconditions.isAtLeastAndroidPie())
    }

    @JvmStatic fun canHandleBiometric(context: Context): Single<Boolean> {
      return hasBiometricSupport(context).flatMap { it ->
        if (it) isAtLeastAndroidPie()
        else Single.just(false)
      }
    }
  }
}