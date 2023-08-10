package kz.project.domain.forms_validation

/**
 * Валидатор для подтверждения пароля
 */
interface ConfirmPasswordValidator {

    fun execute(password: String, confirmedPassword: String): ValidationResult
}