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
package pwittchen.github.com.rxbiometric

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.github.pwittchen.rxbiometric.library.RxBiometric
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.BiometricNotSupported
import com.github.pwittchen.rxbiometric.library.validation.RxPreconditions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.button

class MainActivity : AppCompatActivity() {

  private var disposable: Disposable? = null

  @RequiresApi(Build.VERSION_CODES.P)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)


    button.setOnClickListener {
      disposable =
        RxPreconditions
          .hasBiometricSupport(this)
          .flatMapCompletable {
            if (!it) Completable.error(BiometricNotSupported())
            else
              RxBiometric
                .title("title")
                .description("description")
                .negativeButtonText("cancel")
                .negativeButtonListener(DialogInterface.OnClickListener { _, _ ->
                  showMessage("cancel")
                })
                .executor(ActivityCompat.getMainExecutor(this@MainActivity))
                .build()
                .authenticate(this)
          }
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeBy(
            onComplete = { showMessage("authenticated!") },
            onError = {
              when (it) {
                is AuthenticationError -> showMessage("error: ${it.errorCode} ${it.errorMessage}")
                is AuthenticationFail -> showMessage("fail")
                else -> {
                  showMessage("other error")
                }
              }
            }
          )
    }
  }

  override fun onPause() {
    super.onPause()
    disposable?.let {
      if (!it.isDisposed) {
        it.dispose()
      }
    }
  }

  private fun showMessage(message: String) {
    Toast
      .makeText(
        this@MainActivity,
        message,
        Toast.LENGTH_SHORT
      )
      .show()
  }
}