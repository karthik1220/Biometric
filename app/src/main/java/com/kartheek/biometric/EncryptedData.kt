package com.kartheek.biometric

data class CiphertextWrapper(
    val ciphertext: ByteArray, val initializationVector: ByteArray
)
