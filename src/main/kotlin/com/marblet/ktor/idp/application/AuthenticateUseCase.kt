package com.marblet.ktor.idp.application

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.marblet.ktor.idp.domain.model.AuthorizationError
import com.marblet.ktor.idp.domain.model.UnauthenticatedUser
import com.marblet.ktor.idp.domain.repository.UserRepository
import com.marblet.ktor.idp.domain.service.HashingService

class AuthenticateUseCase(
    private val userRepository: UserRepository,
    private val hashingService: HashingService,
) {
    fun authenticate(unauthenticatedUser: UnauthenticatedUser): Either<Error, Response> {
        val user = userRepository.findByUsername(unauthenticatedUser.username) ?: return Error.UserNotExist.left()
        if (!user.validate(unauthenticatedUser, hashingService)) {
            return Error.UserNotExist.left()
        }
        return Response(mapOf("login" to user.id.value)).right()
    }

    sealed class Error(val error: AuthorizationError, val description: String) {
        data object UserNotExist : Error(AuthorizationError.INVALID_REQUEST, "username and/or password are invalid.")
    }

    data class Response(val cookies: Map<String, String>)
}
