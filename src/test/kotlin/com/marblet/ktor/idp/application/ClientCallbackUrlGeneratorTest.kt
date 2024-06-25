package com.marblet.ktor.idp.application

import com.marblet.ktor.idp.domain.model.RedirectUri
import com.marblet.ktor.idp.domain.model.ResponseMode.FRAGMENT
import com.marblet.ktor.idp.domain.model.ResponseMode.QUERY
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ClientCallbackUrlGeneratorTest : StringSpec({
    val redirectUri = RedirectUri("https://example.com/hoge?key=value")

    "returnRedirectUriAppendingQueryParametersWhenResponseModeQuery" {
        val target = ClientCallbackUrlGenerator()

        val actual =
            target.generate(
                redirectUri = redirectUri,
                code = "test-code",
                accessToken = null,
                idToken = null,
                state = "state123",
                responseMode = QUERY,
            )

        actual.shouldBe("https://example.com/hoge?key=value&code=test-code&state=state123")
    }

    "returnRedirectUriAppendingFragmentsWhenTokenExists" {
        val target = ClientCallbackUrlGenerator()

        val actual =
            target.generate(
                redirectUri = redirectUri,
                code = "test-code",
                accessToken = "test-access-token",
                idToken = null,
                state = "state123",
                responseMode = FRAGMENT,
            )

        actual.shouldBe(
            "https://example.com/hoge?key=value#access_token=test-access-token" +
                "&token_type=Bearer&code=test-code&state=state123"
        )
    }

    "returnRedirectUriAppendingFragmentsWhenIdTokenExists" {
        val target = ClientCallbackUrlGenerator()

        val actual =
            target.generate(
                redirectUri = redirectUri,
                code = "test-code",
                accessToken = null,
                idToken = "test-id-token",
                state = "state123",
                responseMode = FRAGMENT,
            )

        actual.shouldBe("https://example.com/hoge?key=value#id_token=test-id-token&code=test-code&state=state123")
    }

    "returnRedirectUriAppendingFragmentsWhenTokenAndIdTokenExists" {
        val target = ClientCallbackUrlGenerator()

        val actual =
            target.generate(
                redirectUri = redirectUri,
                code = null,
                accessToken = "test-access-token",
                idToken = "test-id-token",
                state = null,
                responseMode = FRAGMENT,
            )

        actual.shouldBe(
            "https://example.com/hoge?key=value#access_token=test-access-token&token_type=Bearer" +
                "&id_token=test-id-token"
        )
    }
})
