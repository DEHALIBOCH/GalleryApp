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

        val birthdayNumbers = birthday.split("-").map { it.toInt() }

        if (birthdayNumbers[0] !in 1..31) {
            return ValidationResult(
                successful = false,
                errorMessage = "This is not valid day"
            )
        }

        if (birthdayNumbers[1] !in 1..12) {
            return ValidationResult(
                successful = false,
                errorMessage = "This is not valid month"
            )
        }

        if (birthdayNumbers[2] < 1900) {
            return ValidationResult(
                successful = false,
                errorMessage = "This is not valid year"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}