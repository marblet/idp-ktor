package com.marblet.ktor.idp.domain.model

import com.marblet.ktor.idp.domain.repository.AuthorizationCodeRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldMatch
import java.time.LocalDateTime

class AuthorizationCodeTest : StringSpec({
    "generatedCodeLengthIs32" {
        val userId = UserId("user-1")
        val clientId = ClientId("client-1")
        val scopes = ConsentedScopes(setOf("a", "b"))
        val redirectUri = RedirectUri("test")
        val authorizationCodeRepository = DummyAuthorizationCodeRepository()

        val actual = AuthorizationCode.generate(
            userId,
            clientId,
            scopes,
            redirectUri,
            null,
            authorizationCodeRepository
        )

        actual.code.shouldHaveLength(32)
        authorizationCodeRepository.haveBeenCalledOnceWith(actual)
    }

    "generatedCodeIsAlphanumeric" {
        val userId = UserId("user-1")
        val clientId = ClientId("client-1")
        val scopes = ConsentedScopes(setOf("a", "b"))
        val redirectUri = RedirectUri("test")
        val authorizationCodeRepository = DummyAuthorizationCodeRepository()

        val actual = AuthorizationCode.generate(
            userId,
            clientId,
            scopes,
            redirectUri,
            null,
            authorizationCodeRepository
        )

        actual.code.shouldMatch("^[0-9a-zA-Z]+$")
        authorizationCodeRepository.haveBeenCalledOnceWith(actual)
    }

    "generatedCodeExpirationIsWithin10Minutes" {
        val userId = UserId("user-1")
        val clientId = ClientId("client-1")
        val scopes = ConsentedScopes(setOf("a", "b"))
        val redirectUri = RedirectUri("test")
        val authorizationCodeRepository = DummyAuthorizationCodeRepository()

        val actual = AuthorizationCode.generate(
            userId,
            clientId,
            scopes,
            redirectUri,
            null,
            authorizationCodeRepository
        )

        val currentTime = LocalDateTime.now()
        val expirationThreshold = LocalDateTime.now().plusMinutes(10)
        actual.expiration.shouldBeAfter(currentTime)
        actual.expiration.shouldBeBefore(expirationThreshold)
        authorizationCodeRepository.haveBeenCalledOnceWith(actual)
    }
})

class DummyAuthorizationCodeRepository : AuthorizationCodeRepository {
    private val history = mutableListOf<AuthorizationCode>()

    override fun insert(authorizationCode: AuthorizationCode) {
        history.add(authorizationCode)
    }

    override fun get(code: String): AuthorizationCode? {
        TODO("Not yet implemented")
    }

    override fun delete(authorizationCode: AuthorizationCode) {
        TODO("Not yet implemented")
    }

    fun haveBeenCalledOnceWith(authorizationCode: AuthorizationCode) {
        history.shouldBe(listOf(authorizationCode))
    }
}
