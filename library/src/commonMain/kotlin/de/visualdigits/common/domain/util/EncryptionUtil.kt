package de.visualdigits.common.domain.util

typealias EncryptedString = String

interface CryptoBox {
    fun encrypt(value: String): String
    fun decrypt(value: String): String
}
