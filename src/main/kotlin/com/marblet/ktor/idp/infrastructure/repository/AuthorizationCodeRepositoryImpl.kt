package com.marblet.ktor.idp.infrastructure.repository

import com.marblet.ktor.idp.domain.model.AuthorizationCode
import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.ConsentedScopes
import com.marblet.ktor.idp.domain.model.RedirectUri
import com.marblet.ktor.idp.domain.model.UserId
import com.marblet.ktor.idp.domain.repository.AuthorizationCodeRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AuthorizationCodeRepositoryImpl : AuthorizationCodeRepository {
    override fun get(code: String): AuthorizationCode? {
        return transaction {
            AuthorizationCodes.selectAll().where {
                AuthorizationCodes.code eq code
            }.firstOrNull()?.let {
                AuthorizationCode(
                    code = it[AuthorizationCodes.code],
                    userId = UserId(it[AuthorizationCodes.userId]),
                    clientId = ClientId(it[AuthorizationCodes.clientId]),
                    scopes = ConsentedScopes.fromSpaceSeparatedString(it[AuthorizationCodes.scope]),
                    redirectUri = RedirectUri(it[AuthorizationCodes.redirectUri]),
                    expiration = it[AuthorizationCodes.expiration],
                    nonce = it[AuthorizationCodes.nonce],
                )
            }
        }
    }

    override fun insert(authorizationCode: AuthorizationCode) {
        AuthorizationCodes.insert {
            it[code] = authorizationCode.code
            it[userId] = authorizationCode.userId.value
            it[clientId] = authorizationCode.clientId.value
            it[scope] = authorizationCode.scopes.toSpaceSeparatedString()
            it[redirectUri] = authorizationCode.redirectUri.value
            it[expiration] = authorizationCode.expiration
            it[nonce] = authorizationCode.nonce
        }
    }

    override fun delete(authorizationCode: AuthorizationCode) {
        AuthorizationCodes.deleteWhere {
            code eq authorizationCode.code
        }
    }
}

object AuthorizationCodes : Table("authorization_codes") {
    val code = varchar("code", 32)
    val userId = varchar("user_id", 128) references Users.id
    val clientId = varchar("client_id", 128) references Clients.id
    val scope = text("scope")
    val redirectUri = text("redirect_uri")
    val expiration = datetime("expiration")
    val nonce = varchar("nonce", 256).nullable()

    override val primaryKey = PrimaryKey(code)
}
