package data.datasource

import data.model.User
import data.response.Page
import data.response.SingleResponse

interface UserDataSource {
    suspend fun getUsers() : Page<User>
    suspend fun getUser(id: Int) : SingleResponse<User>
}