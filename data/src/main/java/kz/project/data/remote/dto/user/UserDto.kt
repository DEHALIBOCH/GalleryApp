package kz.project.data.remote.dto.user


import com.google.gson.annotations.SerializedName

/**
 * Класс User получаемый, непосредственно при запросах с api(получение текущего юзера, получение юзера по id)
 */
data class UserDto(
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("enabled")
    val enabled: Boolean?,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("roles")
    val roles: List<String>?,
    @SerializedName("username")
    val username: String?
)