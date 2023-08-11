package kz.project.data.forms_validation_impl

import kz.project.domain.forms_validation.BirthdayValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

class BirthdayValidatorImpl @Inject constructor() : BirthdayValidator {

    /**
     * Валидирует введенную дату
     */
    override fun execute(birthday: String): ValidationResult {
        if (birthday.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Birthday can't be empty"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}