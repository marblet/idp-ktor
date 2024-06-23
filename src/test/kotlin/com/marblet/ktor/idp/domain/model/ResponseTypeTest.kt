package com.marblet.ktor.idp.domain.model

import com.marblet.ktor.idp.domain.model.ResponseType.CODE
import com.marblet.ktor.idp.domain.model.ResponseType.CODE_IDTOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.CODE_IDTOKEN_TOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.CODE_TOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.IDTOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.IDTOKEN_TOKEN
import com.marblet.ktor.idp.domain.model.ResponseType.TOKEN
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class ResponseTypeTest : FunSpec({
    test("canFindEnumCorrespondingToValue") {
        val actual = ResponseType.find("code")

        actual.shouldBe(CODE)
    }

    test("returnNullIfNoEnumFound") {
        val actual = ResponseType.find("hoge")

        actual.shouldBeNull()
    }

    context("hasCodeReturnTrueIfEnumContainsCode") {
        withData(listOf(CODE, CODE_IDTOKEN, CODE_TOKEN, CODE_IDTOKEN_TOKEN)) {
            val actual = it.hasCode()

            actual.shouldBeTrue()
        }
    }

    context("hasCodeReturnFalseIfEnumNotContainCode") {
        withData(listOf(TOKEN, IDTOKEN, IDTOKEN_TOKEN)) {
            val actual = it.hasCode()

            actual.shouldBeFalse()
        }
    }

    context("hasTokenReturnTrueIfEnumContainsToken") {
        withData(listOf(TOKEN, IDTOKEN_TOKEN, CODE_TOKEN, CODE_IDTOKEN_TOKEN)) {
            val actual = it.hasToken()

            actual.shouldBeTrue()
        }
    }

    context("hasTokenReturnFalseIfEnumNotContainToken") {
        withData(listOf(CODE, IDTOKEN, CODE_IDTOKEN)) {
            val actual = it.hasToken()

            actual.shouldBeFalse()
        }
    }

    context("hasIdTokenReturnTrueIfEnumContainsIdToken") {
        withData(listOf(IDTOKEN, IDTOKEN_TOKEN, CODE_IDTOKEN, CODE_IDTOKEN_TOKEN)) {
            val actual = it.hasIdToken()

            actual.shouldBeTrue()
        }
    }

    context("hasIdTokenReturnFalseIfEnumNotContainIdToken") {
        withData(listOf(CODE, TOKEN, CODE_TOKEN)) {
            val actual = it.hasIdToken()

            actual.shouldBeFalse()
        }
    }

    context("requiresOpenidScopeReturnTrueIfTriggersHybridFlow") {
        withData(listOf(IDTOKEN, IDTOKEN_TOKEN, CODE_TOKEN, CODE_IDTOKEN, CODE_IDTOKEN_TOKEN)) {
            val actual = it.requiresOpenidScope()

            actual.shouldBeTrue()
        }
    }

    context("requiresOpenidScopeReturnFalseIfNotTriggerHybridFlow") {
        withData(listOf(CODE, TOKEN)) {
            val actual = it.requiresOpenidScope()

            actual.shouldBeFalse()
        }
    }
})
