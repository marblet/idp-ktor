package com.marblet.ktor.idp.plugins

import com.marblet.ktor.idp.domain.repository.AuthorizationCodeRepository
import com.marblet.ktor.idp.domain.repository.ClientRepository
import com.marblet.ktor.idp.domain.repository.ConsentRepository
import com.marblet.ktor.idp.domain.repository.UserInfoRepository
import com.marblet.ktor.idp.domain.repository.UserRepository
import com.marblet.ktor.idp.infrastructure.repository.AuthorizationCodeRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.ClientRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.ConsentRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.UserInfoRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.UserRepositoryImpl
import io.ktor.server.application.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

val moduleList = module {
    // Repository
    single<AuthorizationCodeRepository> { AuthorizationCodeRepositoryImpl() }
    single<ClientRepository> { ClientRepositoryImpl() }
    single<ConsentRepository> { ConsentRepositoryImpl() }
    single<UserInfoRepository> { UserInfoRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
}

fun Application.configureKoin() {
    startKoin {
        // modules(moduleList)
        modules(moduleList)
    }
}
