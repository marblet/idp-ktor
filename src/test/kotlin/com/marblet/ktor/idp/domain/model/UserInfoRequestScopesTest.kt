package com.marblet.ktor.idp.domain.model

import com.marblet.ktor.idp.domain.model.UserInfoScope.ADDRESS
import com.marblet.ktor.idp.domain.model.UserInfoScope.EMAIL
import com.marblet.ktor.idp.domain.model.UserInfoScope.PHONE
import com.marblet.ktor.idp.domain.model.UserInfoScope.PROFILE
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class UserInfoRequestScopesTest : StringSpec({

    "generateInstanceExtractingUserInfoScopeFromTokenScopes" {
        val tokenScopes = TokenScopes(setOf("profile", "email", "address", "phone", "a", "b"))

        val actual = UserInfoRequestScopes.generate(tokenScopes)

        val expect = UserInfoRequestScopes(setOf(PROFILE, EMAIL, ADDRESS, PHONE))
        actual.shouldBe(expect)
    }

    "returnNullIfNoUserInfoScopeContained" {
        val tokenScopes = TokenScopes(setOf("a", "b"))

        val actual = UserInfoRequestScopes.generate(tokenScopes)

        actual.shouldBeNull()
    }

    "returnTrueIfInstanceContainsUserInfoScope" {
        val target = UserInfoRequestScopes.generate(TokenScopes(setOf("profile", "email")))

        val actual = target?.contains(PROFILE)

        actual.shouldNotBeNull().shouldBeTrue()
    }

    "returnFalseIfInstanceNotContainUserInfoScope" {
        val target = UserInfoRequestScopes.generate(TokenScopes(setOf("profile", "email")))

        val actual = target?.contains(ADDRESS)

        actual.shouldNotBeNull().shouldBeFalse()
    }
})
