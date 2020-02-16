package com.padcmyanmar.padcx.jetpacksecuritydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        const val ENC_KEY = "ENCRYPT_KEY"
        const val PREFS_FILENAME = "ENCRYPTED_PREFS"
    }

    private val encryptedPrefs by lazy {
        EncryptedSharedPreferences.create(
            PREFS_FILENAME,
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /*
    private val encryptedFile by lazy {
        EncryptedFile.Builder(
            file,
            applicationContext,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }
    */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave.setOnClickListener {
            encryptString(tilEdtPrefs.text.toString())
        }

        btnLoad.setOnClickListener {
            tvStatus.text = decryptPrefsString() ?: "No Saved Value"
        }
    }

    private fun encryptString(value: String) {
        encryptedPrefs.edit {
            putString(ENC_KEY, value)
            apply()
        }
        tvStatus.text = "Value Encrypted"
    }

    private fun decryptPrefsString(): String? = encryptedPrefs.getString(ENC_KEY, null)
}
