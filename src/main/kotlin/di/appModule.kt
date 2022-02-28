package di

import data.datasource.UserDataSource
import data.datasource.cache.UserCacheDataSource
import data.datasource.remote.UserRemoteDataSourceImpl
import data.repository.LoginRepository
import data.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import presenter.BerandaPresenter
import presenter.LoginPresenter
import presenter.UserPresenter

val oppModule = module {
    single { BerandaPresenter() }
    single { HttpClient(CIO){
        install(Logging)
        install(JsonFeature){
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    } }

    single<UserDataSource>{ UserRemoteDataSourceImpl(get()) }
    single { UserCacheDataSource() }
    single { UserRepository(get(),get()) }
    single { UserPresenter(get()) }
    factory { LoginRepository(get()) }
    factory { LoginPresenter(get()) }
}