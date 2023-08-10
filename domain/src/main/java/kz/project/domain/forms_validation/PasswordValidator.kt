package kz.project.domain.forms_validation

/**
 * Валидатор для пароля
 */
interface PasswordValidator {

    fun execute(password: String): ValidationResult
}