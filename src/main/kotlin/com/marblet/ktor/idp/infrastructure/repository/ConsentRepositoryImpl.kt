package com.marblet.ktor.idp.infrastructure.repository

import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.Consent
import com.marblet.ktor.idp.domain.model.ConsentedScopes
import com.marblet.ktor.idp.domain.model.UserId
import com.marblet.ktor.idp.domain.repository.ConsentRepository
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

class ConsentRepositoryImpl : ConsentRepository {
    override fun get(
        userId: UserId,
        clientId: ClientId,
    ): Consent? {
        return transaction {
            Consents.selectAll().where {
                (Consents.userId eq userId.value) and (Consents.clientId eq clientId.value)
            }.firstOrNull()?.let {
                Consent(
                    userId = UserId(it[Consents.userId]),
                    clientId = ClientId(it[Consents.clientId]),
                    scopes = ConsentedScopes.fromSpaceSeparatedString(it[Consents.scopes]),
                )
            }
        }
    }

    override fun upsert(consent: Consent) {
        Consents.upsert {
            it[userId] = consent.userId.value
            it[clientId] = consent.clientId.value
            it[scopes] = consent.scopes.toSpaceSeparatedString()
        }
    }
}

object Consents : Table("consents") {
    val userId = varchar("user_id", 128)
    val clientId = varchar("client_id", 128)
    val scopes = text("scopes")

    override val primaryKey = PrimaryKey(userId, clientId)
}
