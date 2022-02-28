package data.datasource.remote

import data.datasource.UserDataSource
import data.model.User
import data.response.Page
import data.response.SingleResponse
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent

class UserRemoteDataSourceImpl (private val httpClient: HttpClient) : UserDataSource, KoinComponent {
    override suspend fun getUsers(): Page<User> {
        return httpClient.get("https://reqres.in/api/users")
    }

    override suspend fun getUser(id: Int): SingleResponse<User> {
        return httpClient.get("https://reqres.in/api/users/1")
    }
}