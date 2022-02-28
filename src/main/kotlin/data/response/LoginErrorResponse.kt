package data.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginErrorResponse(val error: String)
