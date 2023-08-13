package kz.project.data.remote.dto.error

/**
 * Класс для прокидывания ошибки дальше по иерархии обработки в presentation слой
 */
// TODO доделать обработку ошибок
class ApiException(message: String?) : Exception(message)

// TODO добавить обработку ошибок регистрации
enum class ErrorType {

    /**
     * Необходима, т.к. сервер возвращает 500 код даже при успешной смене пароля
     */
    PASSWORD_UPDATE_SUCCESSFUL,

    /**
     * Не правильный старый пароль
     */
    PASSWORD_UPDATE_OLD_PASSWORD_INCORRECT,
}