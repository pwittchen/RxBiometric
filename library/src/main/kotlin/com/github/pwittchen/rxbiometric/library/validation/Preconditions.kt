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
package com.github.pwittchen.rxbiometric.library.validation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.biometric.BiometricManager
import com.github.pwittchen.rxbiometric.library.AuthenticatorTypes

class Preconditions {
  companion object {
    @JvmStatic fun hasBiometricSupport(context: Context): Boolean {
      if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
          || context.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
          || context.packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)
      }
      return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
        || context.packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)
    }

    @JvmStatic fun canAuthenticateWith(context: Context, @AuthenticatorTypes authenticators: Int): Boolean {
      return BiometricManager.from(context).canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }
  }
}
