package com.marblet.ktor.idp.configuration

import java.io.FileNotFoundException
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

class RsaKeyConfig {
    val rsaPrivateKey: RSAPrivateKey

    init {
        val privateKeyFileContent = this::class.java.classLoader.getResource("classpath:data/private.key")?.readText()
            ?: throw FileNotFoundException()
        val pem = privateKeyFileContent.split("\n")
            .filter { !it.contains("-----BEGIN PRIVATE KEY-----") }
            .filter { !it.contains("-----END PRIVATE KEY-----") }
            .joinToString("")
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(pem))
        this.rsaPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec) as RSAPrivateKey
    }
}
