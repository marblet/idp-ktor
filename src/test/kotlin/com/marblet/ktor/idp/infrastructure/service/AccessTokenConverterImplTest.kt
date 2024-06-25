package com.marblet.ktor.idp.infrastructure.service

import com.marblet.ktor.idp.domain.model.AccessTokenPayload
import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.TokenScopes
import com.marblet.ktor.idp.domain.model.UserId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AccessTokenConverterImplTest : StringSpec({
    "converterCanEncodeAndDecodeAccessToken" {
        val issuedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val accessTokenPayload =
            AccessTokenPayload(
                userId = UserId("user-id"),
                clientId = ClientId("client-id"),
                scopes = TokenScopes(setOf("a", "b", "c")),
                issuedAt = issuedAt,
                expiration = issuedAt.plusSeconds(3600L),
            )
        val target = AccessTokenConverterImpl()

        val jwt = target.encode(accessTokenPayload)
        val actual = target.decode(jwt)

        actual.shouldBe(accessTokenPayload)
    }

    "decoderReturnsNullWhenTokenExpired" {
        val issuedAt = LocalDateTime.of(2024, 1, 26, 22, 37, 0)
        val accessTokenPayload =
            AccessTokenPayload(
                userId = UserId("user-id"),
                clientId = ClientId("client-id"),
                scopes = TokenScopes(setOf("a", "b", "c")),
                issuedAt = issuedAt,
                expiration = issuedAt.plusSeconds(3600L),
            )
        val target = AccessTokenConverterImpl()

        val jwt = target.encode(accessTokenPayload)
        val actual = target.decode(jwt)

        actual.shouldBeNull()
    }

    "decoderReturnsNullWhenTokenInvalid" {
        val target = AccessTokenConverterImpl()

        val actual = target.decode("thisis.invalid.token")

        actual.shouldBeNull()
    }
})
