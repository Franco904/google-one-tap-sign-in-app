package com.example.one_tap_sign_in.core.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoUtils {
    private val keyStore = KeyStore
        .getInstance(KEY_STORE_TYPE)
        .apply {
            load(null)
        }

    fun encrypt(plainTextBytes: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        val cipherTextBytes = cipher
            .apply { init(Cipher.ENCRYPT_MODE, getSecretKey()) }
            .doFinal(plainTextBytes)

        // IV here is always 12 bytes for GCM
        val cipherTextAndIvBytes = cipher.iv + cipherTextBytes
        val cipherTextAndIvBytesBase64 = Base64.getEncoder().encodeToString(cipherTextAndIvBytes)

        return cipherTextAndIvBytesBase64
    }

    fun decrypt(cipherTextAndIvBase64Bytes: ByteArray): ByteArray {
        val cipherTextAndIvBytes = Base64.getDecoder().decode(cipherTextAndIvBase64Bytes)
        val iv = cipherTextAndIvBytes.copyOfRange(0, 12)
        val cipherTextBytes = cipherTextAndIvBytes.copyOfRange(12, cipherTextAndIvBytes.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)

        val plainTextBytes = cipher
            .apply { init(Cipher.DECRYPT_MODE, getSecretKey(), IvParameterSpec(iv)) }
            .doFinal(cipherTextBytes)

        return plainTextBytes
    }

    private fun getSecretKey(): SecretKey {
        val existingSecretKey =
            (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey

        return existingSecretKey ?: generateSecretKey()
    }

    private fun generateSecretKey(): SecretKey {
        return KeyGenerator
            .getInstance(ALGORITHM)
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                    )
                        .setKeySize(256)
                        .setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING_SCHEME)
                        .setRandomizedEncryptionRequired(true) // Ensures new random IV on every encryption
                        .setUserAuthenticationRequired(false)
                        .build()
                )
            }
            .generateKey()
    }

    companion object {
        private const val KEY_STORE_TYPE = "AndroidKeyStore"
        private const val KEY_ALIAS = "secret"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING_SCHEME = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING_SCHEME"
    }
}
