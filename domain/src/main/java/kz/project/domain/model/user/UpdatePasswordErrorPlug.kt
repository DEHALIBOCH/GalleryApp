package kz.project.domain.model.user

/**
 * Затычка для возвращаемого типа при обновлении пароля пользователя, т.к. сервер всегда возвращает 500 ошибку,
 * но в деталях есть информация которую надо распарсить в onErrorResumeNext, а в Completable это сделать нельзя.
 */
class UpdatePasswordErrorPlug(
    val detail: String,
    val title: String,
)