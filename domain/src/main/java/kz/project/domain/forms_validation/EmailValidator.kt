package kz.project.domain.forms_validation

/**
 * Валидатор для email
 */
interface EmailValidator {

    fun execute(email: String) : ValidationResult
}