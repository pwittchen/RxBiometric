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

class Preconditions {
  companion object {
    @JvmStatic fun hasBiometricSupport(context: Context): Boolean {
      return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    @JvmStatic fun isAtLeastAndroidPie(): Boolean {
      return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    @JvmStatic fun canHandleBiometric(context: Context): Boolean {
      return hasBiometricSupport(context) && isAtLeastAndroidPie()
    }
  }
}