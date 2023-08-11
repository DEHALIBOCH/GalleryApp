package kz.project.data.forms_validation_impl

import kz.project.domain.forms_validation.ConfirmPasswordValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * Валидатор для подтверждения пароля
 */
class ConfirmPasswordValidatorImpl @Inject constructor() : ConfirmPasswordValidator {

    /**
     * Валидирует подтверждение пароля, т.е. пароль и подтвержденный пароль должны совпадать
     */
    override fun execute(password: String, confirmedPassword: String): ValidationResult {
        if (confirmedPassword.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Confirm password can't be empty"
            )
        }
        if (password != confirmedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}