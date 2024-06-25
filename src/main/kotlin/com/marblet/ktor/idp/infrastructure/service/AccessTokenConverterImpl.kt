package com.marblet.ktor.idp.infrastructure.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.marblet.ktor.idp.domain.model.AccessTokenPayload
import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.TokenScopes
import com.marblet.ktor.idp.domain.model.UserId
import com.marblet.ktor.idp.domain.service.AccessTokenConverter
import java.time.Instant
import java.time.ZoneId

class AccessTokenConverterImpl : AccessTokenConverter {
    companion object {
        // TODO load the secret from config
        private val algorithm = Algorithm.HMAC256("secret")
        private const val SCOPE_CLAIM_NAME = "sco"
    }

    override fun encode(payload: AccessTokenPayload): String {
        return JWT.create()
            .withSubject(payload.userId.value)
            .withIssuedAt(Instant.ofEpochSecond(payload.issuedAt.atZone(ZoneId.systemDefault()).toEpochSecond()))
            .withExpiresAt(Instant.ofEpochSecond(payload.expiration.atZone(ZoneId.systemDefault()).toEpochSecond()))
            .withAudience(payload.clientId.value)
            .withClaim(SCOPE_CLAIM_NAME, payload.scopes.toSpaceSeparatedString())
            .sign(algorithm)
    }

    override fun decode(accessToken: String): AccessTokenPayload? {
        val verifier = JWT.require(algorithm).build() // verify sign, iat, and exp
        try {
            val decodedJWT = verifier.verify(accessToken)
            return AccessTokenPayload(
                userId = UserId(decodedJWT.subject),
                clientId = ClientId(decodedJWT.audience[0]),
                scopes = TokenScopes.fromSpaceSeparatedString(decodedJWT.getClaim(SCOPE_CLAIM_NAME).asString()),
                issuedAt = decodedJWT.issuedAtAsInstant.atZone(ZoneId.systemDefault()).toLocalDateTime(),
                expiration = decodedJWT.expiresAtAsInstant.atZone(ZoneId.systemDefault()).toLocalDateTime(),
            )
        } catch (exception: TokenExpiredException) {
            // TODO Use Either
            return null
        } catch (exception: JWTDecodeException) {
            return null
        } catch (exception: JWTVerificationException) {
            return null
        }
    }
}
