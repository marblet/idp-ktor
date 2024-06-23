package com.marblet.ktor.idp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PromptSetTest : StringSpec({

    "fromPromptNone" {
        val actual = PromptSet.from("none")

        actual.shouldBe(PromptSet(setOf(Prompt.NONE)))
    }

    "fromPromptLoginAndConsent" {
        val actual = PromptSet.from("login consent")

        actual.shouldBe(PromptSet(setOf(Prompt.LOGIN, Prompt.CONSENT)))
    }

    "fromPromptSelectAccount" {
        val actual = PromptSet.from("select_account")

        actual.shouldBe(PromptSet(setOf(Prompt.SELECT_ACCOUNT)))
    }

    "fromNull" {
        val actual = PromptSet.from(null)

        actual.shouldBe(PromptSet(setOf()))
    }
})
