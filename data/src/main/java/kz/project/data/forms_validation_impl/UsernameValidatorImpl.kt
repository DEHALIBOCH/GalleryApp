package kz.project.data.forms_validation_impl

import kz.project.domain.forms_validation.UsernameValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * Валидатор для юзернейма пользователя
 */
class UsernameValidatorImpl @Inject constructor() : UsernameValidator {

    /**
     * Валидирует юзернейм, согласно api он не должен быть пустым, т.е. должен содержать хотя-бы 1
     * символ
     */
    override fun execute(username: String): ValidationResult {
        if (username.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Username can't be empty"   //TODO посмотреть как в domain слое можно достать stringRes
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}