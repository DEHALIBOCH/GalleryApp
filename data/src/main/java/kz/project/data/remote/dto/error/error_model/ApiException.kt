package kz.project.data.remote.dto.error.error_model

/**
 * Класс для пробрасывания дальше, ошибки пришедшей с api
 */
class ApiException(message: String?): Exception(message)