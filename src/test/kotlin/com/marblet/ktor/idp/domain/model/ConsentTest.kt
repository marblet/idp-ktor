package com.marblet.ktor.idp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class ConsentTest : StringSpec({
    "returnTrueIfAllScopesAreConsented" {
        val target = Consent(UserId("user-id"), ClientId("client-id"), ConsentedScopes(setOf("a", "b", "c")))
        val requestScopes = RequestScopes(setOf("a", "b"))

        val actual = target.satisfies(requestScopes)

        actual.shouldBeTrue()
    }

    "returnFalseIfAllSomeScopesAreNotConsented" {
        val target = Consent(UserId("user-id"), ClientId("client-id"), ConsentedScopes(setOf("a", "b")))
        val requestScopes = RequestScopes(setOf("a", "c"))

        val actual = target.satisfies(requestScopes)

        actual.shouldBeFalse()
    }
})
