package kz.project.domain.use_case.validation

import kz.project.domain.forms_validation.UsernameValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * UseCase для валидации username
 */
class ValidateUsernameUseCase @Inject constructor(
    private val usernameValidator: UsernameValidator
) {

    operator fun invoke(username: String): ValidationResult {
        return usernameValidator.execute(username)
    }
}