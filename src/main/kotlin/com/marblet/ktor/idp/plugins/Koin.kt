package com.marblet.ktor.idp.plugins

import com.marblet.ktor.idp.configuration.RsaKeyConfig
import com.marblet.ktor.idp.domain.repository.AuthorizationCodeRepository
import com.marblet.ktor.idp.domain.repository.ClientRepository
import com.marblet.ktor.idp.domain.repository.ConsentRepository
import com.marblet.ktor.idp.domain.repository.UserInfoRepository
import com.marblet.ktor.idp.domain.repository.UserRepository
import com.marblet.ktor.idp.domain.service.AccessTokenConverter
import com.marblet.ktor.idp.domain.service.HashingService
import com.marblet.ktor.idp.domain.service.IdTokenConverter
import com.marblet.ktor.idp.domain.service.RefreshTokenConverter
import com.marblet.ktor.idp.infrastructure.repository.AuthorizationCodeRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.ClientRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.ConsentRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.UserInfoRepositoryImpl
import com.marblet.ktor.idp.infrastructure.repository.UserRepositoryImpl
import com.marblet.ktor.idp.infrastructure.service.AccessTokenConverterImpl
import com.marblet.ktor.idp.infrastructure.service.HashingServiceImpl
import com.marblet.ktor.idp.infrastructure.service.IdTokenConverterImpl
import com.marblet.ktor.idp.infrastructure.service.RefreshTokenConverterImpl
import io.ktor.server.application.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

val moduleList = module {
    // Configuration
    single { RsaKeyConfig() }

    // Repository
    single<AuthorizationCodeRepository> { AuthorizationCodeRepositoryImpl() }
    single<ClientRepository> { ClientRepositoryImpl() }
    single<ConsentRepository> { ConsentRepositoryImpl() }
    single<UserInfoRepository> { UserInfoRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    // Service
    single<AccessTokenConverter> { AccessTokenConverterImpl() }
    single<HashingService> { HashingServiceImpl() }
    single<IdTokenConverter> { IdTokenConverterImpl(get()) }
    single<RefreshTokenConverter> { RefreshTokenConverterImpl() }
}

fun Application.configureKoin() {
    startKoin {
        modules(moduleList)
    }
}
