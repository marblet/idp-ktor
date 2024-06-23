package com.marblet.ktor.idp.domain.repository

import com.marblet.ktor.idp.domain.model.UserId
import com.marblet.ktor.idp.domain.model.UserInfo

interface UserInfoRepository {
    fun get(userId: UserId): UserInfo?
}
