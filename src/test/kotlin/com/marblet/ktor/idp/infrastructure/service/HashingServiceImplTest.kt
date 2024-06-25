package com.marblet.ktor.idp.infrastructure.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class HashingServiceImplTest : StringSpec({
    "matchRawValueAndHashedValue" {
        val target = HashingServiceImpl()
        val rawValue = "password"
        val hashedValue = target.hash(rawValue)

        val actual = target.matches(rawValue, hashedValue)

        actual.shouldBeTrue()
    }

    "mismatchRawValueAndHashedValue" {
        val target = HashingServiceImpl()
        val rawValue = "password"
        val hashedValue = target.hash("invalid-password")

        val actual = target.matches(rawValue, hashedValue)

        actual.shouldBeFalse()
    }
})
