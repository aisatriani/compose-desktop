package data.model

@kotlinx.serialization.Serializable
data class User(var id: Int, var first_name: String, var last_name: String, var email: String, var avatar: String) {

}