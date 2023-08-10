package kz.project.gallery.presentation.viewmodel.signin_signup

import kz.project.domain.model.user.UserToPost

/**
 * Класс для удобной передачи полей ввода регистрации пользователя с фрагмента во вьюмодель
 */
data class RegistrationForm(
    val username: String,
    val email: String,
    val password: String,
    val confirmedPassword: String,
    val birthday: String,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val birthdayError: String? = null,
)

fun RegistrationForm.toUserToPost() = UserToPost(
    username = username,
    email = email,
    password = password,
    birthday = birthday,
)