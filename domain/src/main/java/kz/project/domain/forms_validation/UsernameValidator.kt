package kz.project.domain.forms_validation

/**
 * Валидатор для юзернейма пользователя
 */
interface UsernameValidator {

    fun execute(username: String): ValidationResult
}