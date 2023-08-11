package kz.project.domain.forms_validation

/**
 * Класс представляющий собой результат валидации полей
 */
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)