package pwittchen.github.com.rxbiometric

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.pwittchen.rxbiometric.library.Preconditions
import com.github.pwittchen.rxbiometric.library.RxBiometric
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.button

//TODO: temporary solution for testing purposes to be extracted to the library
class MainActivity : AppCompatActivity() {

  @RequiresApi(28)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    if (!Preconditions.isAtLeastAndroidPie()) {
      showMessage("need at least Android Pie")
      return
    }

    if (!Preconditions.hasBiometricSupport(this)) {
      showMessage("no biometric support")
      return
    }

    val prompt = BiometricPrompt
        .Builder(this)
        .setTitle("title")
        .setDescription("description")
        .setNegativeButton(
            "cancel",
            mainExecutor,
            DialogInterface.OnClickListener { dialog, _ ->
              showMessage("cancel")
              dialog.dismiss()
            })
        .build()

    val cancellationSignal = CancellationSignal()
    cancellationSignal.setOnCancelListener {
      showMessage("cancellation signal")
    }

    button.setOnClickListener {
      prompt.authenticate(
          cancellationSignal,
          mainExecutor,
          object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
              showMessage("success")
            }

            override fun onAuthenticationFailed() {
              super.onAuthenticationFailed()
              showMessage("fail")
            }

            override fun onAuthenticationError(
              errorCode: Int,
              errString: CharSequence?
            ) {
              super.onAuthenticationError(errorCode, errString)
              showMessage("error")
            }

            override fun onAuthenticationHelp(
              helpCode: Int,
              helpString: CharSequence?
            ) {
              super.onAuthenticationHelp(helpCode, helpString)
              showMessage("help")
            }
          })
    }
  }

  fun showMessage(message: String) {
    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
        .show()
  }

}
