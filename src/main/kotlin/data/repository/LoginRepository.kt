package data.repository

import data.request.LoginRequest
import data.response.LoginResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class LoginRepository(private val httpClient: HttpClient) {
    suspend fun login(loginRequest: LoginRequest) : LoginResponse{
        return httpClient.post<LoginResponse>("https://reqres.in/api/login"){
            contentType(ContentType.Application.Json)
            body = loginRequest
        }
    }
}