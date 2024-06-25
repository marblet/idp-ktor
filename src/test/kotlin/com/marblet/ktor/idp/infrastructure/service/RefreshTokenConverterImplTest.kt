package com.marblet.ktor.idp.infrastructure.service

import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.RefreshTokenPayload
import com.marblet.ktor.idp.domain.model.TokenScopes
import com.marblet.ktor.idp.domain.model.UserId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class RefreshTokenConverterImplTest : StringSpec({
    "converterCanEncodeAndDecodeRefreshToken" {
        val issuedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val refreshTokenPayload =
            RefreshTokenPayload(
                userId = UserId("user-id"),
                clientId = ClientId("client-id"),
                scopes = TokenScopes(setOf("a", "b", "c")),
                issuedAt = issuedAt,
                expiration = issuedAt.plusDays(28L),
            )
        val target = RefreshTokenConverterImpl()

        val jwt = target.encode(refreshTokenPayload)
        val actual = target.decode(jwt)

        actual.shouldBe(refreshTokenPayload)
    }

    "decoderReturnsNullWhenTokenExpired" {
        val issuedAt = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
        val expiration = LocalDateTime.of(2023, 2, 1, 0, 0, 0)
        val refreshTokenPayload =
            RefreshTokenPayload(
                userId = UserId("user-id"),
                clientId = ClientId("client-id"),
                scopes = TokenScopes(setOf("a", "b", "c")),
                issuedAt = issuedAt,
                expiration = expiration,
            )
        val target = RefreshTokenConverterImpl()

        val jwt = target.encode(refreshTokenPayload)
        val actual = target.decode(jwt)

        actual.shouldBeNull()
    }

    "decoderReturnsNullWhenTokenInvalid" {
        val target = RefreshTokenConverterImpl()

        val actual = target.decode("thisis.invalid.token")

        actual.shouldBeNull()
    }
})
