package com.marblet.ktor.idp.domain.repository

import com.marblet.ktor.idp.domain.model.AuthorizationCode

interface AuthorizationCodeRepository {
    fun get(code: String): AuthorizationCode?

    fun insert(authorizationCode: AuthorizationCode)

    fun delete(authorizationCode: AuthorizationCode)
}
