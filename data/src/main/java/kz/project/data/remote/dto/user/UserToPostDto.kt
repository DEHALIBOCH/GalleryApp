package kz.project.data.remote.dto.user


import com.google.gson.annotations.SerializedName

/**
 * Модель User, которую мы можем отправить в API, при создании или изменении юзера.
 * Обязательные поля для регистрации, являются не нуллабельными.
 */
data class UserToPostDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("birthday")
    val birthday: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("roles")
    val roles: List<String> = listOf("ROLE_USER"),
)