package pwittchen.github.com.rxbiometric

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.button

//TODO: temporary solution for testing purposes to be extracted to the library
class MainActivity : AppCompatActivity() {

  @RequiresApi(28)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)


    if (!isAtLeastAndroidPie()) {
      showMessage("need at least Android Pie")
      return
    }

    if (!hasBiometricSupport()) {
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

  @RequiresApi(VERSION_CODES.M)
  private fun hasBiometricSupport(): Boolean {
    val packageManager = this.packageManager
    return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
  }

  fun isAtLeastAndroidPie(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
  }

  fun showMessage(message: String) {
    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
        .show()
  }

}
