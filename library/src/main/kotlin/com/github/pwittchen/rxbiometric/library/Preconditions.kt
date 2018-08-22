package com.github.pwittchen.rxbiometric.library

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import io.reactivex.Single

class Preconditions {
  companion object {
    @JvmStatic fun hasBiometricSupport(context: Context): Boolean {
      return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    @JvmStatic fun hasBiometricSupportRx(context: Context): Single<Boolean> {
      return Single.just(hasBiometricSupport(context))
    }

    @JvmStatic fun isAtLeastAndroidPie(): Boolean {
      return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    @JvmStatic fun isAtLeastAndroidPieRx(): Single<Boolean> {
      return Single.just(isAtLeastAndroidPie())
    }
  }
}