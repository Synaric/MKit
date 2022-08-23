package com.synaric.art.security

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature

/**
 * 提供数据签名、数据校验，以及一个封装的[EncryptedSharedPreferences]用于本地加密SP的存取。
 * @property sharedPreferences SharedPreferences
 * @constructor
 */
@Suppress("unused")
class SecurityManager private constructor(context: Context) {

    companion object {

        private const val KeyStoreProvider = "AndroidKeyStore"

        private const val DefaultKeyAlias = "DefaultKey"

        private const val DatabaseKeyAlias = "DatabaseKeyAlias"

        private const val EncryptSPFilename = "DefaultEncryptSP"

        @Volatile
        private var Instance: SecurityManager? = null

        fun getInstance(context: Context): SecurityManager =
            Instance ?: synchronized(this) {
                Instance ?: SecurityManager(context)
            }
    }

    private lateinit var sharedPreferences: SharedPreferences

    init {
        generateKey(DefaultKeyAlias, KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY)
        generateKey(DatabaseKeyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        initEncryptSP(context)
    }

    private fun generateKey(alias: String, purposes: Int) {
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            KeyStoreProvider
        )
        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            purposes
        ).run {
            setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            build()
        }

        kpg.initialize(parameterSpec)
        kpg.generateKeyPair()
    }

    private fun initEncryptSP(applicationContext: Context) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val sharedPrefsFile: String = EncryptSPFilename
        sharedPreferences = EncryptedSharedPreferences.create(
            sharedPrefsFile,
            mainKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * 对数据进行签名。
     * @param data String 需要签名的数据。
     * @return String? 签名。
     */
    fun sign(data: String): String? {
        val dataArray = data.toByteArray()
        val ks: KeyStore = KeyStore.getInstance(KeyStoreProvider).apply {
            load(null)
        }
        val entry: KeyStore.Entry = ks.getEntry(DefaultKeyAlias, null)
        if (entry !is KeyStore.PrivateKeyEntry) {
            return null
        }
        val signature: ByteArray = Signature.getInstance("SHA256withECDSA").run {
            initSign(entry.privateKey)
            update(dataArray)
            sign()
        }

        return signature.toString()
    }

    /**
     * 验证数据的签名。
     * @param data String 需要验证签名的数据。
     * @param signature String 签名。
     * @return Boolean 是否验证通过。
     */
    fun verify(data: String, signature: String): Boolean {
        val dataArray = data.toByteArray()
        val signatureArray = signature.toByteArray()
        val ks = KeyStore.getInstance(KeyStoreProvider).apply {
            load(null)
        }
        val entry = ks.getEntry(DefaultKeyAlias, null) as? KeyStore.PrivateKeyEntry ?: return false
        val valid: Boolean = Signature.getInstance("SHA256withECDSA").run {
            initVerify(entry.certificate)
            update(dataArray)
            verify(signatureArray)
        }

        return valid
    }

    /**
     * 将数据写入加密后的SP。
     * @param onSave Function1<Editor, Unit> 在这个回调中使用[SharedPreferences.Editor]保存数据。
     * @return Unit
     */
    fun saveToEncryptSP(onSave: (SharedPreferences.Editor) -> Unit) {
        with (sharedPreferences.edit()) {
            onSave(this)
            commit()
        }
    }

    /**
     * 从加密后的SP读取数据。
     * @param onGet Function1<SharedPreferences, Unit> 在这个回调中读取数据。
     * @return Unit
     */
    fun getFromEncryptSP(onGet: (SharedPreferences) -> Unit) {
        with (sharedPreferences) {
            onGet(this)
        }
    }
}