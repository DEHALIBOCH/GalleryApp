package kz.project.domain.model.user

/**
 * Класс User получаемый, непосредственно при запросах с api(получение текущего юзера, получение юзера по id)
 */
data class User(
    val id: Int,
    val email: String,
    val enabled: Boolean,
    val phone: String,
    val fullName: String,
    val username: String,
    val birthday: String?,
    val roles: List<String> = listOf("ROLE_USER"),
)