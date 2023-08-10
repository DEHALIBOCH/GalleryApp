package kz.project.domain.model.user

data class UserToReceive(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val birthday: String? = null,
    val roles: List<String> = listOf("ROLE_USER"),
)