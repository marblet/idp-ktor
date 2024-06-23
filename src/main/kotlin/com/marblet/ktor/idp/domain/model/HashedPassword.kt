package com.marblet.ktor.idp.domain.model

import com.marblet.ktor.idp.domain.service.HashingService

data class HashedPassword(val value: String) {
    companion object {
        fun hashRawPassword(
            rawPassword: RawPassword,
            hashingService: HashingService,
        ) = HashedPassword(hashingService.hash(rawPassword.value))
    }

    fun matches(
        rawPassword: RawPassword,
        hashingService: HashingService,
    ): Boolean {
        return hashingService.matches(rawPassword.value, value)
    }
}
