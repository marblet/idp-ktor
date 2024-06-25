package com.marblet.ktor.idp.infrastructure.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.marblet.ktor.idp.configuration.RsaKeyConfig
import com.marblet.ktor.idp.domain.model.IdTokenPayload
import com.marblet.ktor.idp.domain.service.IdTokenConverter
import java.time.Instant
import java.time.ZoneId

class IdTokenConverterImpl(
    private val rsaKeyConfig: RsaKeyConfig
) : IdTokenConverter {

    companion object {
        private const val NONCE_KEY = "nonce"
    }

    private val algorithm = Algorithm.RSA256(null, this.rsaKeyConfig.rsaPrivateKey)

    override fun encode(payload: IdTokenPayload): String {
        algorithm.signingKeyId
        return JWT.create()
            .withKeyId("FyTxRzagrBgUeRC3neg3FbryhLj3Yf")
            .withIssuer("http://localhost:8080")
            .withSubject(payload.userId.value)
            .withAudience(payload.clientId.value)
            .withIssuedAt(Instant.ofEpochSecond(payload.issuedAt.atZone(ZoneId.systemDefault()).toEpochSecond()))
            .withExpiresAt(Instant.ofEpochSecond(payload.expiration.atZone(ZoneId.systemDefault()).toEpochSecond()))
            .also { jwt -> payload.nonce?.let { jwt.withClaim(NONCE_KEY, it) } }
            .sign(algorithm)
    }
}
