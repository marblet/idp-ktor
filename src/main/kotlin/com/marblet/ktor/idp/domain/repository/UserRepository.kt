package com.marblet.ktor.idp.domain.repository

import com.marblet.ktor.idp.domain.model.User

interface UserRepository {
    fun get(id: String): User?

    fun findByUsername(username: String): User?
}
