package kz.project.data.remote.dto.token


import com.google.gson.annotations.SerializedName

data class AccessTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("scope")
    val scope: Any?,
    @SerializedName("token_type")
    val tokenType: String
)