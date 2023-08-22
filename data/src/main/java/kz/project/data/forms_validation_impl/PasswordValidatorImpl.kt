package kz.project.data.forms_validation_impl

import kz.project.domain.forms_validation.PasswordValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * Валидатор для пароля
 */
class PasswordValidatorImpl @Inject constructor() : PasswordValidator {

    /**
     * Валидирует пароль, согласно API минимальна длина пароля должна быть 5 символов
     */
    override fun execute(password: String): ValidationResult {
        if (password.length < 5) {
            return ValidationResult(
                successful = false,
                errorMessage = "Minimum password length is 5 characters"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}