package kz.project.domain.use_case.validation

import kz.project.domain.forms_validation.PasswordValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * UseCase для валидации пароля
 */
class ValidatePasswordUseCase @Inject constructor(
    private val passwordValidator: PasswordValidator
) {

    operator fun invoke(password: String): ValidationResult {
        return passwordValidator.execute(password)
    }
}