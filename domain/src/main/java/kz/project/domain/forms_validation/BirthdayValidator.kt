package kz.project.domain.forms_validation

/**
 * Валидатор для даты рождения
 */
interface BirthdayValidator {

    fun execute(birthday: String): ValidationResult
}