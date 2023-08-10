package kz.project.data.forms_validation_impl

import kz.project.domain.forms_validation.EmailValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * Валидатор для email
 */
class EmailValidatorImpl @Inject constructor() : EmailValidator {

    /**
     * Валидирует email
     */
    override fun execute(email: String): ValidationResult {
        val regex = Regex("^([a-z0-9_.-]+)@([\\da-z.-]+).([a-z.]{2,6})\$")
        val isMatches = regex.matches(email)

        if (!isMatches) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email address has the wrong format"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}