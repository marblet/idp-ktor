package com.marblet.ktor.idp.domain.model

import com.marblet.ktor.idp.domain.model.GrantRequestCreateError.ClientNotExist
import com.marblet.ktor.idp.domain.model.GrantRequestCreateError.RedirectUriInvalid
import com.marblet.ktor.idp.domain.model.GrantRequestCreateError.ResponseTypeInvalid
import com.marblet.ktor.idp.domain.model.GrantRequestCreateError.ScopeInvalid
import com.marblet.ktor.idp.domain.model.GrantRequestCreateError.UserNotFound
import com.marblet.ktor.idp.domain.model.ResponseMode.FRAGMENT
import com.marblet.ktor.idp.domain.model.ResponseMode.QUERY
import com.marblet.ktor.idp.domain.model.ResponseType.CODE_IDTOKEN_TOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.IDTOKEN
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ValidatedGrantRequestTest : FunSpec({
    val client =
        Client(
            clientId = ClientId("test-client-id"),
            secret = "test_secret",
            redirectUris = setOf("http://example.com"),
            name = "test client",
            scopes = ClientScopes(setOf("openid", "email", "test")),
        )

    val user =
        User(
            id = UserId("test-user-id"),
            username = "test user",
            password = HashedPassword("password"),
        )

    context("AuthorizationCodeFlowTest") {
        test("generateRequest") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    nonce = "nonce123",
                    responseMode = "query",
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.client.shouldBe(client)
            request.responseType.shouldBe(ResponseType.CODE)
            request.consentedScopes.shouldBe(ConsentedScopes(setOf("openid", "email")))
            request.user.shouldBe(user)
            request.nonce.shouldBe("nonce123")
            request.responseMode.shouldBe(QUERY)
        }
    }

    context("OAuth2ImplicitFlowTest") {
        test("generateRequest") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "test",
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(ResponseType.TOKEN)
            request.consentedScopes.shouldBe(ConsentedScopes(setOf("test")))
            request.responseMode.shouldBe(FRAGMENT)
        }

        test("generateRequestWithOpenidScope") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email test",
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(ResponseType.TOKEN)
            request.consentedScopes.shouldBe(ConsentedScopes(setOf("openid", "email", "test")))
        }

        test("generateRequestOfOpenidScope") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid",
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.consentedScopes.shouldBe(ConsentedScopes(setOf("openid")))
        }
    }

    context("OIDCImplicitFlowTest") {
        test("generateRequest") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "id_token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid",
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(IDTOKEN)
            request.consentedScopes.shouldBe(ConsentedScopes(setOf("openid")))
        }

        fun invalidScopeError() {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "id_token token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "email",
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ScopeInvalid)
        }
    }

    context("OIDCHybridFlowTest") {
        test("generateRequest") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code id_token token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(CODE_IDTOKEN_TOKEN)
            request.consentedScopes.shouldBe(ConsentedScopes(setOf("openid", "email")))
        }

        test("invalidScopeError") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "test",
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ScopeInvalid)
        }
    }

    context("CommonErrorTest") {
        test("createNullError") {
            val actual =
                ValidatedGrantRequest.create(
                    client = null,
                    user = user,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ClientNotExist)
        }

        test("userNullError") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = null,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(UserNotFound)
        }

        test("invalidResponseTypeError") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "invalid",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ResponseTypeInvalid)
        }

        test("invalidRedirectUriError") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://invalid-uri.com"),
                    scope = "openid email",
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(RedirectUriInvalid)
        }

        test("invalidScopeError") {
            val actual =
                ValidatedGrantRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid invalid-scope",
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ScopeInvalid)
        }
    }
})
