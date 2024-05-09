package com.biometricauth.biometrics

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.util.concurrent.Executors
import android.app.Activity  
import android.content.Intent

class BiometricModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {
    private var biometricPrompt: BiometricPrompt? = null
    private var biometricPromptInfo: BiometricPrompt.PromptInfo? = null

    init {
        reactContext.addActivityEventListener(this)
    }

    override fun getName(): String {
        return "BiometricModule"
    }

     override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        // This method is often left empty if not used
    }

    override fun onNewIntent(intent: Intent?) {
        // This method is often left empty if not used
    }

    @ReactMethod
    fun isBiometricAvailable(promise: Promise) {
        val context = reactApplicationContext
        val biometricManager = BiometricManager.from(context)
        val canAuthenticate = biometricManager.canAuthenticate()

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            promise.resolve(true)
        } else {
            promise.reject("E_BIOMETRIC_NOT_AVAILABLE", "Biometric authentication is not available.")
        }
    }

    @ReactMethod
    fun authenticate(promise: Promise) {
        val activity = currentActivity as? FragmentActivity
        if (activity == null) {
            promise.reject("E_ACTIVITY_NULL", "Activity is null.")
            return
        }

        if (biometricPrompt == null) {
            val executor = Executors.newSingleThreadExecutor()
            biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        promise.reject("E_AUTH_ERROR", errString.toString())
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        promise.resolve(true)
                    }

                    override fun onAuthenticationFailed() {
                        promise.reject("E_AUTH_FAILED", "Authentication failed.")
                    }
                }
            )
        }

        if (biometricPromptInfo == null) {
            biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate using biometrics")
                .setNegativeButtonText("Cancel")
                .build()
        }

        biometricPrompt?.authenticate(biometricPromptInfo!!)
    }
}
