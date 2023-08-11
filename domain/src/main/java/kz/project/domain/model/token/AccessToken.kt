package kz.project.domain.model.token

data class AccessToken(
    val accessToken: String,
    val refreshToken: String,
)