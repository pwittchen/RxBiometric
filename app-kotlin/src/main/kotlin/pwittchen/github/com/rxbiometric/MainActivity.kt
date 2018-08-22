package pwittchen.github.com.rxbiometric

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.pwittchen.rxbiometric.library.RxBiometric
import com.github.pwittchen.rxbiometric.library.validation.RxPreconditions
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationFail
import com.github.pwittchen.rxbiometric.library.throwable.AuthenticationHelp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.button

//TODO #1: test it
//TODO #2: remove commented code
class MainActivity : AppCompatActivity() {

  @RequiresApi(Build.VERSION_CODES.P)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

//    if (!Preconditions.isAtLeastAndroidPie()) {
//      showMessage("need at least Android Pie")
//      return
//    }
//
//    if (!Preconditions.hasBiometricSupport(this)) {
//      showMessage("no biometric support")
//      return
//    }

//    val prompt = BiometricPrompt
//      .Builder(this)
//      .setTitle("title")
//      .setDescription("description")
//      .setNegativeButton(
//        "cancel",
//        mainExecutor,
//        DialogInterface.OnClickListener { _, _ ->
//          showMessage("cancel")
//        })
//      .build()
//
    val cancellationSignal = CancellationSignal()
    cancellationSignal.setOnCancelListener {
      showMessage("cancellation signal")
    }
//
//    button.setOnClickListener {
//      prompt.authenticate(
//        cancellationSignal,
//        mainExecutor,
//        object : BiometricPrompt.AuthenticationCallback() {
//          override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
//            showMessage("success")
//          }
//
//          override fun onAuthenticationFailed() {
//            super.onAuthenticationFailed()
//            showMessage("fail")
//          }
//
//          override fun onAuthenticationError(
//            errorCode: Int,
//            errString: CharSequence?
//          ) {
//            super.onAuthenticationError(errorCode, errString)
//            showMessage("error")
//          }
//
//          override fun onAuthenticationHelp(
//            helpCode: Int,
//            helpString: CharSequence?
//          ) {
//            super.onAuthenticationHelp(helpCode, helpString)
//            showMessage("help")
//          }
//        })
//    }

    button.setOnClickListener { _ ->
      RxPreconditions
        .hasBiometricSupport(this)
        .flatMap { RxPreconditions.isAtLeastAndroidPie() }
        .flatMapCompletable {
          RxBiometric
            .title("title")
            .description("description")
            .negativeButtonText("cancel")
            .negativeButtonListener(DialogInterface.OnClickListener { _, _ ->
              showMessage("cancel")
            })
            .cancellationSignal(cancellationSignal)
            .executor(mainExecutor)
            .build()
            .authenticate(this)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
          onComplete = {
            showMessage("authenticated!")
          },
          onError = {
            when (it) {
              is AuthenticationFail -> showMessage("fail")
              is AuthenticationHelp -> showMessage("help")
              else -> showMessage("error")
            }
          }
        )

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