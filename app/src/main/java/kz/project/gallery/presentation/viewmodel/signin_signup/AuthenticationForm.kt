package kz.project.gallery.presentation.viewmodel.signin_signup

/**
 * Класс для удобной передачи полей ввода авторизации через email и пароль
 */
data class AuthenticationForm(
    val email: String,
    val password: String,
    val emailError: String? = null,
    val passwordError: String? = null,
)