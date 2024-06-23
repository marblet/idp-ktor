package com.marblet.ktor.idp.domain.model

import com.marblet.ktor.idp.domain.service.HashingService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class HashedPasswordTest : StringSpec({
    "generateHashedPasswordFromRawPassword" {
        val rawPassword = RawPassword("password")
        val hashingService = HashingServiceStub()

        val actual = HashedPassword.hashRawPassword(rawPassword, hashingService)

        val expect = HashedPassword("password-hash")
        actual.shouldBe(expect)
    }

    "returnTrueWhenPasswordsMatch" {
        val rawPassword = RawPassword("password")
        val target = HashedPassword("password-hash")
        val hashingService = HashingServiceStub()

        val actual = target.matches(rawPassword, hashingService)

        actual.shouldBeTrue()
    }

    "returnFalseWhenPasswordsMismatch" {
        val rawPassword = RawPassword("invalid-password")
        val target = HashedPassword("password-hash")
        val hashingService = HashingServiceStub()

        val actual = target.matches(rawPassword, hashingService)

        actual.shouldBeFalse()
    }
})

class HashingServiceStub : HashingService {
    override fun hash(rawValue: String): String {
        return "$rawValue-hash"
    }

    override fun matches(
        rawValue: String,
        hashedValue: String,
    ): Boolean {
        return hash(rawValue) == hashedValue
    }
}
