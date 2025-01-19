package com.example.one_tap_sign_in.core.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoUtils {
    private val cipher = Cipher.getInstance(TRANSFORMATION)

    private val keyStore = KeyStore
        .getInstance(KEY_STORE_TYPE)
        .apply {
            load(null)
        }

    fun encrypt(plainTextBytes: ByteArray): ByteArray {
        val cipherTextBytes = cipher
            .apply { init(Cipher.ENCRYPT_MODE, getSecretKey()) }
            .doFinal(plainTextBytes)

        // IV here is always 12 bytes for GCM
        val cipherTextAndIvBytes = cipher.iv + cipherTextBytes
        val cipherTextAndIvEncodedBytes = Base64.getEncoder().encode(cipherTextAndIvBytes)

        return cipherTextAndIvEncodedBytes
    }

    fun decrypt(cipherTextAndIvEncodedBytes: ByteArray): ByteArray {
        val cipherTextAndIvBytes = Base64.getDecoder().decode(cipherTextAndIvEncodedBytes)
        val iv = cipherTextAndIvBytes.copyOfRange(0, 12)
        val cipherTextBytes = cipherTextAndIvBytes.copyOfRange(12, cipherTextAndIvBytes.size)

        val plainTextBytes = cipher
            .apply {
                init(
                    Cipher.DECRYPT_MODE,
                    getSecretKey(),
                    GCMParameterSpec(AUTH_TAG_LENGTH_BITS, iv)
                )
            }
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
                        .setKeySize(KEY_LENGTH_BITS)
                        .setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING_SCHEME)
                        .setRandomizedEncryptionRequired(true) // Ensures new random IV on every encryption
                        .build()
                )
            }
            .generateKey()
    }

    fun generateNonce(): String {
        val nonceBytes = ByteArray(16).apply {
            SecureRandom().nextBytes(this)
        }

        val encodedNonce = Base64.getUrlEncoder().withoutPadding().encodeToString(nonceBytes)
        return encodedNonce
    }

    companion object {
        private const val KEY_STORE_TYPE = "AndroidKeyStore"
        private const val KEY_ALIAS = "secret"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING_SCHEME = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING_SCHEME"

        private const val KEY_LENGTH_BITS = 256
        private const val AUTH_TAG_LENGTH_BITS = 128
    }
}
