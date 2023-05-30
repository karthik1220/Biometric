package com.kartheek.biometric

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt

class MainActivity : AppCompatActivity() {
    private lateinit var cryptographyManager: CryptographyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cryptographyManager = CryptographyManagerImpl()
        showBiometricPromptForEncryption()

    }

    private fun showBiometricPromptForEncryption() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(this, ::encryptSecretInformation)
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }


    private fun encryptSecretInformation(authResult: BiometricPrompt.AuthenticationResult) {
        authResult.cryptoObject?.cipher?.apply {
            val encryptedData =  cryptographyManager.encryptData("token", this)
            }
        }

  /* private fun showBiometricPromptForDecryption() {
      ciphertextWrapper?.let { textWrapper ->
        val secretKeyName = getString(R.string.secret_key_name)
        val cipher = cryptographyManager.getInitializedCipherForDecryption(
            secretKeyName, textWrapper.initializationVector
        )
        biometricPrompt =
            BiometricPromptUtils.createBiometricPrompt(
                this,
                ::decryptServerTokenFromStorage
            )
        val promptInfo = BiometricPromptUtils.createPromptInfo(this)
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }
}*/

/*private fun decryptSecretInformation(authResult: BiometricPrompt.AuthenticationResult) {
    ciphertextWrapper?.let { textWrapper ->
        authResult.cryptoObject?.cipher?.let {
            val plaintext =
                cryptographyManager.decryptData(textWrapper.ciphertext, it)
        }
    }
}*/






private fun checkDeviceCanAuthenticateWithBiometrics() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showToast(getString(R.string.message_no_support_biometrics))
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showToast(getString(R.string.message_no_hardware_available))
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
              //  checkAPILevelAndProceed()
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                    }
                } else {
                    TODO("VERSION.SDK_INT < R")
                }
               // startActivityForResult(enrollIntent, REQUEST_CODE)
            }
        }
    }

}