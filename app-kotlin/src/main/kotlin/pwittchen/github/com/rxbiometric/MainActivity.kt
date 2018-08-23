package pwittchen.github.com.rxbiometric

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.pwittchen.rxbiometric.library.RxBiometric
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationError
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationHelp
import com.github.pwittchen.rxbiometric.library.throwable.BiometricNotSupported
import com.github.pwittchen.rxbiometric.library.validation.RxPreconditions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.button

class MainActivity : AppCompatActivity() {

  private lateinit var disposable: Disposable

  @RequiresApi(Build.VERSION_CODES.P)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)


    button.setOnClickListener { _ ->
      disposable = RxPreconditions
        .canHandleBiometric(this)
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
              .cancellationSignal(CancellationSignal())
              .executor(mainExecutor)
              .build()
              .authenticate(this)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
          onComplete = { showMessage("authenticated!") },
          onError = {
            when (it) {
              is AuthenticationError -> showMessage("error")
              is AuthenticationFail -> showMessage("fail")
              is AuthenticationHelp -> showMessage("help")
              is BiometricNotSupported -> showMessage("biometric not supported")
              else -> showMessage("other error")
            }
          }
        )
    }
  }

  override fun onPause() {
    super.onPause()
    if (!disposable.isDisposed) {
      disposable.dispose()
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