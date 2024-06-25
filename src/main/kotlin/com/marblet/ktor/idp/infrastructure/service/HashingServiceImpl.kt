package com.marblet.ktor.idp.infrastructure.service

import com.marblet.ktor.idp.domain.service.HashingService
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

class HashingServiceImpl : HashingService {
    override fun hash(rawValue: String): String {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
            .encode(rawValue)
    }

    override fun matches(
        rawValue: String,
        hashedValue: String,
    ): Boolean {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
            .matches(rawValue, hashedValue)
    }
}
