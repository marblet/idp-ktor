package com.marblet.ktor.idp.infrastructure.repository

import com.marblet.ktor.idp.domain.model.UserId
import com.marblet.ktor.idp.domain.model.UserInfo
import com.marblet.ktor.idp.domain.repository.UserInfoRepository
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserInfoRepositoryImpl : UserInfoRepository {
    override fun get(userId: UserId): UserInfo? {
        return transaction {
            Userinfos.selectAll().where {
                Userinfos.userId eq userId.value
            }.firstOrNull()?.let {
                UserInfo(
                    userId = userId,
                    name = it[Userinfos.name],
                    email = it[Userinfos.email],
                    phoneNumber = it[Userinfos.phoneNumber],
                    address = it[Userinfos.address],
                )
            }
        }
    }
}

object Userinfos : Table("userinfos") {
    val userId = varchar("user_id", 128) references Users.id
    val name = varchar("name", 256).nullable()
    val email = varchar("email", 256).nullable()
    val phoneNumber = varchar("phone_number", 20).nullable()
    val address = varchar("address", 256).nullable()

    override val primaryKey = PrimaryKey(userId)
}
