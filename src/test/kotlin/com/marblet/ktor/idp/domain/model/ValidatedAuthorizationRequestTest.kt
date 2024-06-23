package com.marblet.ktor.idp.domain.model

import com.marblet.ktor.idp.domain.model.AuthorizationRequestCreateError.ClientNotExist
import com.marblet.ktor.idp.domain.model.AuthorizationRequestCreateError.RedirectUriInvalid
import com.marblet.ktor.idp.domain.model.AuthorizationRequestCreateError.ResponseTypeInvalid
import com.marblet.ktor.idp.domain.model.AuthorizationRequestCreateError.ScopeInvalid
import com.marblet.ktor.idp.domain.model.Prompt.NONE
import com.marblet.ktor.idp.domain.model.ResponseMode.FRAGMENT
import com.marblet.ktor.idp.domain.model.ResponseType.CODE
import com.marblet.ktor.idp.domain.model.ResponseType.CODE_IDTOKEN_TOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.IDTOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.TOKEN
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ValidatedAuthorizationRequestTest : FunSpec({
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
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    prompt = "none",
                    nonce = "nonce123",
                    responseMode = "fragment",
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.client.shouldBe(client)
            request.responseType.shouldBe(CODE)
            request.requestScopes.shouldBe(RequestScopes(setOf("openid", "email")))
            request.promptSet.shouldBe(PromptSet(setOf(NONE)))
            request.user.shouldBe(user)
            request.nonce.shouldBe("nonce123")
            request.responseMode.shouldBe(FRAGMENT)
        }
    }

    context("OAuth2ImplicitFlowTest") {
        test("generateRequest") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "test",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(TOKEN)
            request.requestScopes.shouldBe(RequestScopes(setOf("test")))
            request.responseMode.shouldBe(FRAGMENT)
        }

        test("generateRequestOfOpenidScope") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(TOKEN)
            request.requestScopes.shouldBe(RequestScopes(setOf("openid")))
        }
    }

    context("OIDCImplicitFlowTest") {
        test("generateRequest") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "id_token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(IDTOKEN)
            request.requestScopes.shouldBe(RequestScopes(setOf("openid")))
        }

        test("invalidScopeError") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "id_token token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "email",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ScopeInvalid)
        }
    }

    context("OIDCHybridFlowTest") {
        test("generateRequest") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code id_token token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            val request = actual.getOrNull()
            request.shouldNotBeNull()
            request.responseType.shouldBe(CODE_IDTOKEN_TOKEN)
            request.requestScopes.shouldBe(RequestScopes(setOf("openid", "email")))
            request.responseMode.shouldBe(FRAGMENT)
        }

        test("invalidScopeError") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code token",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "test",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ScopeInvalid)
        }
    }

    context("CommonErrorTest") {
        test("clientNullError") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = null,
                    user = null,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ClientNotExist)
        }

        test("invalidResponseTypeError") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "invalid",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ResponseTypeInvalid)
        }

        test("invalidRedirectUriError") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://invalid-uri.com"),
                    scope = "openid email",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(RedirectUriInvalid)
        }

        test("invalidScopeError") {
            val actual =
                ValidatedAuthorizationRequest.create(
                    client = client,
                    user = user,
                    responseTypeInput = "code",
                    redirectUri = RedirectUri("http://example.com"),
                    scope = "openid email invalid-scope",
                    prompt = null,
                    nonce = null,
                    responseMode = null,
                )

            actual.shouldBeLeft(ScopeInvalid)
        }
    }
})
