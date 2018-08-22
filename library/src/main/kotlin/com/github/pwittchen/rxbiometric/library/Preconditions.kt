package com.github.pwittchen.rxbiometric.library

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class Preconditions {
  companion object {
    @JvmStatic fun hasBiometricSupport(context: Context): Boolean {
      return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    @JvmStatic fun isAtLeastAndroidPie(): Boolean {
      return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }
  }
}